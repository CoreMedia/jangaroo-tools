package net.jangaroo.exml.compiler;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.model.ExmlSourceFile;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.exml.parser.ExmlToModelParser;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CharacterRecordingHandler;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A tool that converts EXML source code into MXML and ActionScript source code.
 */
public class ExmlToMxml {

  private ConfigClassRegistry configClassRegistry;

  public ExmlToMxml(ConfigClassRegistry configClassRegistry) {
    this.configClassRegistry = configClassRegistry;
  }

  public File[] convert() {
    Map<String, ExmlSourceFile> exmlSourceFilesByConfigClassName = configClassRegistry.getExmlSourceFilesByConfigClassName();
    List<File> mxmlFiles = new ArrayList<File>();
    Collection<ExmlSourceFile> exmlSourceFiles;
    List<File> sourceFiles = configClassRegistry.getConfig().getSourceFiles();
    if (sourceFiles.isEmpty()) {
      exmlSourceFiles = exmlSourceFilesByConfigClassName.values();
    } else {
      exmlSourceFiles = new ArrayList<ExmlSourceFile>();
      for (File exmlFile : sourceFiles) {
        exmlSourceFiles.add(configClassRegistry.getExmlSourceFile(exmlFile));
      }
    }
    for (ExmlSourceFile exmlSourceFile : exmlSourceFiles) {
      System.out.printf("Converting EXML file %s...%n", exmlSourceFile.getSourceFile());
      File configClassFile = exmlSourceFile.generateConfigClass();
      System.out.printf("  Generated config class %s into file %s.%n", exmlSourceFile.getConfigClassName(), configClassFile.getPath());
      if (exmlSourceFile.hasSourceTargetClass()) {
        System.out.printf("  Target class %s is implemented in ActionScript: no need to generate MXML target class.", exmlSourceFile.getTargetClassName());
      } else {
        try {
          File mxmlFile = exmlToMxml(exmlSourceFile);
          mxmlFiles.add(mxmlFile);
          System.out.printf("  Generated MXML target class %s into file %s.%n", exmlSourceFile.getTargetClassName(), mxmlFile.getPath());
        } catch (Exception e) {
          throw new ExmlcException("Unable to convert to MXML: " + e.getMessage(), exmlSourceFile.getSourceFile(), e);
        }
      }
    }
    // clean up EXML files:
    for (ExmlSourceFile exmlSourceFile : exmlSourceFiles) {
      if (!exmlSourceFile.getSourceFile().delete()) {
        System.err.println("Failed to delete EXML source file " + exmlSourceFile.getSourceFile().getPath());
      }
    }
    return mxmlFiles.toArray(new File[mxmlFiles.size()]);
  }

  private File exmlToMxml(ExmlSourceFile exmlSourceFile) throws IOException, SAXException {
    ExmlModel exmlModel = new ExmlToModelParser(configClassRegistry).parse(exmlSourceFile.getSourceFile());
    File sourceFile = exmlSourceFile.getSourceFile();
    File outputFile = CompilerUtils.fileFromQName(exmlSourceFile.getTargetClassName(), configClassRegistry.getConfig().getOutputDirectory(), ".mxml");
    PrintStream writer = new PrintStream(new FileOutputStream(outputFile), true, net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);
    ExmlToConfigClassParser.parseFileWithHandler(sourceFile, new ExmlToMxmlHandler(exmlSourceFile, exmlModel, writer));
    return outputFile;
  }

  private class ExmlToMxmlHandler extends CharacterRecordingHandler implements LexicalHandler {
    private static final String CLOSE_FX_SCRIPT = "  ]]></fx:Script>";

    private final PrintStream out;
    private PrintStream currentOut;
    private boolean insideCdata;
    private boolean insideExmlObject;
    private ByteArrayOutputStream elementRecorder;
    private int lastColumn = 2;
    private Locator locator;
    private final Map<String,String> prefixMappings = new LinkedHashMap<String, String>();
    private String untypedPrefix;
    private Map<String,String> configDefaultValues;
    private final Map<String,String> configDefaultSubElements = new LinkedHashMap<String, String>();
    private boolean pendingTagClose = false;
    private String configClassPrefix;
    private Set<String> imports;
    private List<Declaration> constants;
    private List<Declaration> vars;

    private final Deque<String> elementPath = new LinkedList<String>();
    private final ExmlSourceFile exmlSourceFile;
    private final ExmlModel exmlModel;
    private String currentConfigName;
    private String currentVarName;
    private boolean isPublicApi;

    public ExmlToMxmlHandler(ExmlSourceFile exmlSourceFile, ExmlModel exmlModel, PrintStream out) {
      this.exmlSourceFile = exmlSourceFile;
      this.exmlModel = exmlModel;
      currentOut = this.out = out;
    }

    @Override
    public void startDocument() throws SAXException {
      currentOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      imports = new LinkedHashSet<String>();
      constants = new ArrayList<Declaration>();
      vars = new ArrayList<Declaration>();
      startRecordingCharacters();
      super.startDocument();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
      this.locator = locator;
    }

    @Override
    public void startPrefixMapping(String key, String uriValue) throws SAXException {
      if (key.equals("exml")) {
        prefixMappings.put("fx", MxmlUtils.MXML_NAMESPACE_URI);
        prefixMappings.put("exml", "net.jangaroo.ext");
        if (untypedPrefix != null) {
          // already declared through exml:untyped replacement: bail out!
          return;
        }
        untypedPrefix = key = "u";
        uriValue = MxmlUtils.MXML_UNTYPED_NAMESPACE;
      } else if ("exml:untyped".equals(uriValue)) {
        // replace exml:untyped by mxml:untyped
        if (untypedPrefix != null) {
          prefixMappings.remove(untypedPrefix);
        }
        untypedPrefix = key;
        uriValue = MxmlUtils.MXML_UNTYPED_NAMESPACE;
      } else if (uriValue.startsWith("exml:")) {
        String packageName = uriValue.substring(5);
        if (packageName.equals(CompilerUtils.packageName(exmlSourceFile.getConfigClassName()))) {
          configClassPrefix = key;
        }
        uriValue = packageName;
      }
      prefixMappings.put(key, uriValue);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      String configClassQName = exmlSourceFile.getConfigClassName();
      String configClassName = CompilerUtils.className(configClassQName);
      if (configClassPrefix == null) {
        configClassPrefix = "cfg";
        prefixMappings.put(configClassPrefix, CompilerUtils.packageName(configClassQName));
      }
      Map<String,String> attributes = new LinkedHashMap<String, String>();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
          qName = handleRootNode(qName, atts);
          for (Map.Entry<String, String> prefixMapping : prefixMappings.entrySet()) {
            String key = prefixMapping.getKey();
            String uriValue = prefixMapping.getValue();
            if (!uriValue.contains(":")) {
              uriValue += ".*";
            }
            attributes.put(key.isEmpty() ? "xmlns" : "xmlns:" + key, uriValue);
          }
        } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
          qName = handleAnnotation(atts);
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          qName = handleCfg(atts);
        } else if (Exmlc.EXML_CONSTANT_NODE_NAME.equals(localName)) {
          qName = handleConstant(atts, configClassQName);
        } else if (Exmlc.EXML_VAR_NODE_NAME.equals(localName)) {
          qName = handleVar(atts);
        } else if (Exmlc.EXML_DECLARATION_VALUE_NODE_NAME.equals(localName)) {
          qName = handleInnerElement();
        } else if (Exmlc.EXML_IMPORT_NODE_NAME.equals(localName)) {
          qName = handleImport(atts);
        } else if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName)) {
          qName = handleInnerElement();
        } else if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
          qName = null;
        } else if (Exmlc.EXML_OBJECT_NODE_NAME.equals(localName)) {
          insideExmlObject = true;
        }
      } else if (elementPath.size() == 1) {
        String thePackage = ExmlUtils.parsePackageFromNamespace(uri);
        if (thePackage == null) {
          throw new ExmlcException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package", locator.getLineNumber(), locator.getColumnNumber());
        }
        if (!imports.isEmpty() || !constants.isEmpty()) {
          currentOut.printf("%n  <fx:Script><![CDATA[%n");
          for (String anImport : imports) {
            currentOut.printf("    import %s;%n", anImport);
          }
          if (!constants.isEmpty()) {
            if (!imports.isEmpty()) {
              currentOut.println();
            }
            for (Declaration constant : constants) {
              currentOut.printf("    public static const %s:%s = %s;%n",
                      constant.getName(), constant.getType(), constant.getValue());
            }
          }
          currentOut.print(CLOSE_FX_SCRIPT);
        }
        printConfigObjectAndVars(configClassName);
      }
      boolean isPropertyElement = !elementPath.isEmpty() && elementPath.size() % 2 == 0;
      if (isPropertyElement) {
        String lastQName = elementPath.peek();
        if (lastQName != null) {
          String[] parts = lastQName.split(":");
          String prefix = parts.length == 1 ? "" : parts[0] + ":";
          qName = prefix + localName;
        }
      }
      if (qName != null && currentVarName != null) {
        attributes.put("id", currentVarName);
        attributes.put(untypedPrefix + ":scope", "constructor");
        currentVarName = null;
      }
      for (int i = 0; i < atts.getLength(); ++i) {
        String attributeName = atts.getQName(i);
        if ("id".equals(attributeName)) {
          attributeName = "extId";
        } else if (isPropertyElement && "mode".equals(attributeName)) {
          attributeName = untypedPrefix + ":mode";
        } else if (Exmlc.EXML_BASE_CLASS_ATTRIBUTE.equals(attributeName) ||
                Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(attributeName)) {
          continue;
        }

        attributes.put(attributeName, atts.getValue(i));
      }
      if (!elementPath.isEmpty() && elementPath.peek() == null) {
        popRecordedCharacters();
      }
      if (qName != null) {
        flush();
        currentOut.printf("<%s", qName);
        printAttributes(attributes, lastColumn + qName.length());
        pendingTagClose = true;
      }
      elementPath.push(qName);
      lastColumn = locator.getColumnNumber();
      if ("fx:Metadata".equals(qName)) {
        flushPendingTagClose();
        out.print("[");
      } else if (elementPath.size() == 1 && isPublicApi) {
        flushPendingTagClose();
        out.printf("%n  <fx:Metadata>[%s]</fx:Metadata>", Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
      }
    }

    private void printConfigObjectAndVars(String configClassName) {
      String configElementQName = String.format("%s:%s", configClassPrefix, configClassName);
      currentOut.printf("%n  <%s", configElementQName);
      printAttributes(getConfigDefaultValues(), "  < ".length() + configElementQName.length());
      configDefaultValues = null;
      if (configDefaultSubElements.isEmpty()) {
        currentOut.print("/>");
      } else {
        currentOut.print(">");
        for (Map.Entry<String, String> configDefaultSubElement : configDefaultSubElements.entrySet()) {
          String propertyQName = String.format("%s:%s", configClassPrefix, configDefaultSubElement.getKey());
          currentOut.printf("%n    <%s>%n      %s%n    </%s>", propertyQName,
                  configDefaultSubElement.getValue(), propertyQName);
        }
        currentOut.printf("%n  </%s>", configElementQName);
        configDefaultSubElements.clear();
      }
      if (!vars.isEmpty()) {
        boolean insideScript = false;
        for (Declaration var : vars) {
          if (var.getDescription() == null) {
            if (!insideScript) {
              currentOut.printf("%n  <fx:Script u:scope=\"constructor\"><![CDATA[%n");
              insideScript = true;
            }
            currentOut.printf("    var %s:%s = %s;%n", var.getName(), var.getType(),
                    formatValue(var.getValue(), var.getType()));
          } else {
            if (insideScript) {
              currentOut.print(CLOSE_FX_SCRIPT);
              insideScript = false;
            }
            currentOut.printf("%n  %s", var.getValue());
          }
        }
        if (insideScript) {
          currentOut.print(CLOSE_FX_SCRIPT);
        }
      }
    }

    private String findPrefix(String packageName) {
      if (packageName.isEmpty()) {
        return "fx";
      }
      for (Map.Entry<String, String> prefixMapping : prefixMappings.entrySet()) {
        if (packageName.equals(prefixMapping.getValue())) {
          return prefixMapping.getKey();
        }
      }
      return null;
    }

    private void printAttributes(Map<String, String> attributes, int indent) {
      String whitespace = " ";
      for (Map.Entry<String, String> attribute : attributes.entrySet()) {
        String value = attribute.getValue();
        if (!ExmlUtils.isCodeExpression(value)) {
          // escape all opening curly braces, as MXML also recognizes them anywhere inside a string:
          value = value.replaceAll("\\{", "\\\\{");
        }
        currentOut.printf("%s%s=%s", whitespace, attribute.getKey(),
                String.format("\"%s\"", escapeXml(value).replaceAll("\"", "&quot;")));
        if (" ".equals(whitespace)) {
          whitespace = String.format("%n%s", StringUtils.repeat(" ", indent));
        }
      }
    }

    private String escapeXml(String xmlString) {
      return xmlString.replaceAll("&", "&amp;").replaceAll("<", "&lt;");
    }

    private String handleInnerElement() {
      elementRecorder = new ByteArrayOutputStream();
      try {
        currentOut = new PrintStream(elementRecorder, false, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
      return null; // do not render default element (but its contents!)
    }

    private String handleImport(Attributes atts) {
      String importedClassName = atts.getValue(Exmlc.EXML_IMPORT_CLASS_ATTRIBUTE);
      if (importedClassName != null) {
        imports.add(importedClassName);
      }
      return null; // do not render import elements
    }

    private Declaration createDeclaration(Attributes atts) {
      String name = atts.getValue(Exmlc.EXML_DECLARATION_NAME_ATTRIBUTE);
      String type = atts.getValue(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE);
      String value = atts.getValue(Exmlc.EXML_DECLARATION_VALUE_ATTRIBUTE);
      Declaration declaration = new Declaration(name, value, type);
      addImport(declaration.getType()); // may be a guessed type!
      return declaration;
    }

    private void addImport(String type) {
      String packageName = CompilerUtils.packageName(type);
      if (!packageName.isEmpty()) {
        if (findPrefix(packageName) == null) {
          imports.add(type);
        }
      }
    }

    private String handleVar(Attributes atts) {
      Declaration declaration = createDeclaration(atts);
      vars.add(declaration);
      currentVarName = declaration.getName();
      return null; // do not render var elements
    }

    private String handleConstant(Attributes atts, String configClassQName) {
      addImport(configClassQName);
      Declaration declaration = createDeclaration(atts);
      declaration.setValue(configClassQName + "." + declaration.getName());
      constants.add(declaration);
      return null; // do not render constant elements
    }

    private String handleCfg(Attributes atts) {
      currentConfigName = atts.getValue("name");
      String type = atts.getValue("type");
      if (type != null) {
        // even if this config does not add anything to the target class,
        // its type must be imported, because other code may take advantage of this import.
        addImport(type);
      }
      String configDefault = atts.getValue("default");
      if (configDefault != null && !configDefault.isEmpty()) {
        getConfigDefaultValues().put(currentConfigName, configDefault);
      }
      return null; // do not render config elements
    }

    private Map<String, String> getConfigDefaultValues() {
      if (configDefaultValues == null) {
        configDefaultValues = new LinkedHashMap<String, String>();
        configDefaultValues.put("id", "config");
        configDefaultValues.put(untypedPrefix + ":scope", "constructorParam");
      }
      return configDefaultValues;
    }

    private String handleAnnotation(Attributes atts) {
      AnnotationAt annotationAt = AnnotationAt.BOTH; // default for "at" is "both"
      for (int i = 0; i < atts.getLength(); i++) {
        if (Exmlc.EXML_ANNOTATION_AT_ATTRIBUTE.equals(atts.getLocalName(i))) {
          // found "at" attribute: parse it (might throw ExmlcException)
          annotationAt = Exmlc.parseAnnotationAtValue(atts.getValue(i));
          break;
        }
      }
      return annotationAt == AnnotationAt.CONFIG ? null : "fx:Metadata";
    }

    private String handleRootNode(String qName, Attributes atts) {
      String asDoc = exmlModel.getDescription();
      if (asDoc != null && !asDoc.trim().isEmpty()) {
        currentOut.println("<!---");
        currentOut.println("  " + convertNewLines(asDoc).replaceAll("--", "&#45;&#45;"));
        currentOut.println("-->");
      }
      ConfigClass superConfigClass = exmlSourceFile.getConfigClass().getSuperClass();
      if (superConfigClass != null) {
        String superClassName = superConfigClass.getComponentClassName();
        for (int i = 0; i < atts.getLength(); i++) {
          //baseClass attribute has been specified, so the super class of the component is actually that
          String attLocalName = atts.getLocalName(i);
          String attValue = atts.getValue(i);
          if (Exmlc.EXML_BASE_CLASS_ATTRIBUTE.equals(attLocalName)) {
            superClassName = attValue;
            if (superClassName.indexOf('.') == -1) {
              // fully-qualify by same package:
              superClassName = CompilerUtils.qName(CompilerUtils.packageName(exmlSourceFile.getTargetClassName()),
                      superClassName);
            }
          } else if (Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(attLocalName)) {
            PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(attValue);
            if (publicApiMode != PublicApiMode.FALSE) {
              isPublicApi = true;
            }
          }
        }
        qName = "baseClass:" + CompilerUtils.className(superClassName);
        prefixMappings.put("baseClass", CompilerUtils.packageName(superClassName));
      }
      return qName;
    }

    private String formatValue(String value, String type) {
      return value == null ? null
              : JsonObject.valueToString(ExmlToModelParser.getAttributeValue(value, type), 2, 4);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      flushPendingTagClose();
      super.characters(ch, start, length);
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      qName = elementPath.pop();
      if (qName != null) {
        if (pendingTagClose) {
          currentOut.print("/>");
          pendingTagClose = false;
        } else {
          flush();
          if ("fx:Metadata".equals(qName)) {
            out.print("]");
          }
          currentOut.printf("</%s>", qName);
        }
      }
      insideExmlObject = false; // <exml:object>s cannot be nested.
      startRecordingCharacters();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName) ||
                Exmlc.EXML_DECLARATION_VALUE_NODE_NAME.equals(localName)) {
          String value = elementRecorder.toString();
          if (currentConfigName != null) {
            configDefaultSubElements.put(currentConfigName, value);
          } else {
            Declaration varDeclaration = vars.get(vars.size() - 1);
            varDeclaration.setValue(value);
            varDeclaration.setDescription("MXML"); // HACK: marker for "render as MXML"
          }
          currentOut.close();
          currentOut = out;
          elementRecorder = null;
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          currentConfigName = null;
        } else if (Exmlc.EXML_VAR_NODE_NAME.equals(localName)) {
          currentVarName = null;
        }
      }
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
      
    }

    @Override
    public void endDTD() throws SAXException {

    }

    @Override
    public void startEntity(String name) throws SAXException {

    }

    @Override
    public void endEntity(String name) throws SAXException {

    }

    @Override
    public void startCDATA() throws SAXException {
      flush();
      insideCdata = true;
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void endCDATA() throws SAXException {
      flush();
      insideCdata = false;
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
      flush();
      currentOut.print("<!--" + new String(ch, start, length) + "-->");
      lastColumn = locator.getColumnNumber();
    }

    private void flushPendingTagClose() {
      if (pendingTagClose) {
        currentOut.print(">");
        pendingTagClose = false;
      }
    }

    private void flush() {
      flushPendingTagClose();
      String recordedCharacters = popRecordedCharacters();
      if (recordedCharacters != null) {
        String output = convertNewLines(recordedCharacters);
        if (insideCdata) {
          output = escapeXml(output);
        }
        if (insideExmlObject) {
          String code = output.trim();
          if (!code.isEmpty()) {
            output = MxmlUtils.createBindingExpression(code);
          }
        }
        currentOut.print(output);
      }
      startRecordingCharacters();
    }

    private String convertNewLines(String output) {
      return output.replaceAll("\n", System.getProperty("line.separator"));
    }
  }
}
