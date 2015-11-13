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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
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
    try {
      createManifestFile();
    } catch (IOException e) {
      throw new ExmlcException("Unable to create manifest.xml file: " + e.getMessage(), e);
    }

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
    if (!configClassRegistry.getConfig().isKeepExmlFiles()) {
      // clean up EXML files:
      for (ExmlSourceFile exmlSourceFile : exmlSourceFiles) {
        if (!exmlSourceFile.getSourceFile().delete()) {
          System.err.println("Failed to delete EXML source file " + exmlSourceFile.getSourceFile().getPath());
        }
      }
    }
    return mxmlFiles.toArray(new File[mxmlFiles.size()]);
  }

  private File createManifestFile() throws FileNotFoundException, UnsupportedEncodingException {
    // create catalog.xml component library:
    File outputFile = new File(configClassRegistry.getConfig().getOutputDirectory(), "manifest.xml");
    System.out.printf("Creating manifest file %s...%n", outputFile.getPath());
    PrintStream out = new PrintStream(new FileOutputStream(outputFile), true, net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);

    Collection<ConfigClass> sourceConfigClasses = configClassRegistry.getSourceConfigClasses();
    out.println("<?xml version=\"1.0\"?>");
    out.println("<componentPackage>");
    for (ConfigClass configClass : sourceConfigClasses) {
      out.printf("  <component class=\"%s\"/>%n", configClass.getFullName());
//      out.printf("  <component %s/>%n", configClass.getType().isCreatedViaConfigObject()
//              ? String.format("class=\"%s\"", configClass.getFullName())
//              : String.format("id=\"%s\" class=\"%s\"", configClass.getName(), configClass.getComponentClassName()));
    }
    out.println("</componentPackage>");
    out.close();
    return outputFile;
  }

  private File exmlToMxml(ExmlSourceFile exmlSourceFile) throws IOException, SAXException {
    ExmlModel exmlModel = new ExmlToModelParser(configClassRegistry).parse(exmlSourceFile.getSourceFile());
    File sourceFile = exmlSourceFile.getSourceFile();
    File outputFile = CompilerUtils.fileFromQName(exmlSourceFile.getTargetClassName(), configClassRegistry.getConfig().getOutputDirectory(), ".mxml");
    FileUtils.forceMkdir(outputFile.getParentFile());
    PrintStream writer = new PrintStream(new FileOutputStream(outputFile), true, net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);
    ExmlToConfigClassParser.parseFileWithHandler(sourceFile, new ExmlToMxmlHandler(exmlSourceFile, exmlModel, writer));
    return outputFile;
  }

  private class ExmlToMxmlHandler extends CharacterRecordingHandler implements LexicalHandler {
    private static final String OPEN_FX_SCRIPT = "%n  <fx:Script><![CDATA[%n";
    private static final String CLOSE_FX_SCRIPT = "  ]]></fx:Script>";

    private final PrintStream out;
    private PrintStream currentOut;
    private boolean insideScript;
    private boolean insideCdata;
    private boolean insideExmlObject;
    private ByteArrayOutputStream elementRecorder;
    private int lastColumn = 1;
    private Locator locator;
    private Map<String,String> prefixMappings = new LinkedHashMap<String, String>();
    private String exmlPrefix;
    private String configClassPrefix;
    private Map<String,String> configDefaultValues;
    private final Map<String,String> configDefaultSubElements = new LinkedHashMap<String, String>();
    private boolean pendingTagClose = false;
    private Set<String> imports;
    private List<Declaration> constants;
    private List<Declaration> vars;
    private Set<String> varsWithXmlValue;

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
      addImport(exmlSourceFile.getConfigClassName());
      constants = new ArrayList<Declaration>();
      vars = new ArrayList<Declaration>();
      varsWithXmlValue = new HashSet<String>();
      startRecordingCharacters();
      super.startDocument();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
      this.locator = locator;
    }

    @Override
    public void startPrefixMapping(String key, String uriValue) throws SAXException {
      prefixMappings.put(key, uriValue);
    }

    public void startElement(String uri, String localName, final String originalQName, Attributes atts) throws SAXException {
      String configClassQName = exmlSourceFile.getConfigClassName();
      String qName = originalQName;
      Map<String,String> attributes = new LinkedHashMap<String, String>();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
          qName = handleRootNode(qName, atts, attributes);
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
          openScript();
          for (String anImport : imports) {
            currentOut.printf("    import %s;%n", anImport);
          }
          if (!constants.isEmpty()) {
            if (!imports.isEmpty()) {
              currentOut.println();
            }
            for (Declaration constant : constants) {
              currentOut.printf("%n    public static const %s:%s = %s;%n",
                      constant.getName(), constant.getType(), constant.getValue());
            }
          }
        }
        printConstructorAndConfigAndVars();
        closeScript();
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
        currentVarName = null;
      }
      for (int i = 0; i < atts.getLength(); ++i) {
        String attributeName = atts.getQName(i);
        if ("id".equals(attributeName)) {
          attributeName = "id_";
        } else if (isPropertyElement && "mode".equals(attributeName)) {
          attributeName = exmlPrefix + ":mode";
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
        int indent = lastColumn + originalQName.length();
        int qNameLengthDelta = originalQName.length() - qName.length();
        String firstIndent = qNameLengthDelta == 0 || attributes.size() <= 1
                ? ""
                : qNameLengthDelta > 0
                ? StringUtils.repeat(" ", qNameLengthDelta)
                : String.format("%n%s", StringUtils.repeat(" ", indent));
        currentOut.printf("<%s%s", qName, firstIndent);
        printAttributes(attributes, indent + 1);
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

    private void printConstructorAndConfigAndVars() {
      String configClassQName = exmlSourceFile.getConfigClassName();
      String configClassName = CompilerUtils.className(configClassQName);
      openScript();
      if (!vars.isEmpty()) {
        for (Declaration var : vars) {
          String type = var.getType();
          if (type == null || type.isEmpty()) {
            type = "*";
          }
          currentOut.printf ("    private var %s:%s;%n", var.getName(), type);
        }
        currentOut.println();
      }
      if (varsWithXmlValue.size() == vars.size()) {
        currentOut.printf("    public native function %s(config:%s = null);%n", exmlModel.getClassName(), configClassQName);
      } else {
        currentOut.printf("    public function %s(config:%s = null) {%n", exmlModel.getClassName(), configClassQName);
        for (Declaration var : vars) {
          if (!(varsWithXmlValue.contains(var.getName()))) {
            currentOut.printf("      %s = %s;%n", var.getName(), formatValue(var.getValue(), var.getType()));
          }
        }
        currentOut.printf("      super(config); // magic!%n");
        currentOut.printf("    }%n");
      }
      closeScript();
      String configElementQName = String.format("%s:%s", configClassPrefix, configClassName);
      currentOut.printf("%n  <%s", configElementQName);
      Map<String, String> configDefaultValues = getConfigDefaultValues();
      printAttributes(configDefaultValues, "  < ".length() + configElementQName.length());
      this.configDefaultValues = null;
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
      for (Declaration var : vars) {
        if (varsWithXmlValue.contains(var.getName())) {
          currentOut.printf("%n  %s", var.getValue());
        }
      }
    }

    private void openScript() {
      if (insideScript) {
        currentOut.println();
      } else {
        currentOut.printf(OPEN_FX_SCRIPT);
        insideScript = true;
      }
    }

    private void closeScript() {
      if (insideScript) {
        currentOut.print(CLOSE_FX_SCRIPT);
        insideScript = false;
      }
    }

    private String findPrefixForPackage(String packageName) {
      if (packageName.isEmpty()) {
        return "fx";
      }
      return findPrefix(Exmlc.EXML_CONFIG_URI_PREFIX + packageName);
    }

    private String findPrefix(String namespace) {
      for (Map.Entry<String, String> prefixMapping : prefixMappings.entrySet()) {
        if (namespace.equals(prefixMapping.getValue())) {
          return prefixMapping.getKey();
        }
      }
      return null;
    }

    private String createPackageNamespace(String packageName) {
      return CompilerUtils.qName(packageName, "*");
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
      if (currentVarName != null) {
        Declaration currentVar = vars.get(vars.size() - 1);
        if ("Array".equals(currentVar.getType())) {
          return "fx:Array";
        }
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
        if (findPrefixForPackage(packageName) == null) {
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

    private String handleRootNode(String qName, Attributes atts, Map<String, String> attributes) {
      String asDoc = exmlModel.getDescription();
      if (asDoc != null && !asDoc.trim().isEmpty()) {
        currentOut.println("<!---" + convertNewLines(asDoc).replaceAll("--", "&#45;&#45;") + "-->");
      }
      Map<String, String> mxmlPrefixMappings = new LinkedHashMap<String, String>();
      mxmlPrefixMappings.put("fx", MxmlUtils.MXML_NAMESPACE_URI);
      exmlPrefix = findPrefix(Exmlc.EXML_NAMESPACE_URI);
      if (exmlPrefix == null) {
        System.err.println("[WARN] ExmlToMxml: no EXML namespace found!");
        exmlPrefix = "exml";
        mxmlPrefixMappings.put("exml", Exmlc.EXML_NAMESPACE_URI);
      }

      String baseClassNamespace = null;
      String baseClass = atts.getValue(Exmlc.EXML_BASE_CLASS_ATTRIBUTE);
      if (baseClass == null || baseClass.isEmpty()) {
        // baseClass attribute has not been specified, so the super class is derived from the config class:
        ConfigClass superConfigClass = exmlSourceFile.getConfigClass().getSuperClass();
        if (superConfigClass != null) {
          String superClassName = superConfigClass.getComponentClassName();
          qName = "extends:" + CompilerUtils.className(superClassName);
          mxmlPrefixMappings.put("extends", createPackageNamespace(CompilerUtils.packageName(superClassName)));
        }
      } else {
        // baseClass attribute has been specified, so the super class of the component is actually that:
        String baseClassPackage;
        if (baseClass.indexOf('.') == -1) {
          // fully-qualify by same package:
          baseClassPackage = CompilerUtils.packageName(exmlSourceFile.getTargetClassName());
        } else {
          baseClassPackage = CompilerUtils.packageName(baseClass);
          baseClass = CompilerUtils.className(baseClass);
        }
        qName = "baseClass:" + baseClass;
        baseClassNamespace = createPackageNamespace(baseClassPackage);
      }
      String publicApiValue = atts.getValue(Exmlc.EXML_PUBLIC_API_ATTRIBUTE);
      if (publicApiValue != null && !publicApiValue.isEmpty()) {
        PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(publicApiValue);
        if (publicApiMode != PublicApiMode.FALSE) {
          isPublicApi = true;
        }
      }
      String configClassPackage = CompilerUtils.packageName(exmlSourceFile.getConfigClassName());
      configClassPrefix = findPrefixForPackage(configClassPackage);
      if (configClassPrefix == null) {
        configClassPrefix = "cfg";
        mxmlPrefixMappings.put(configClassPrefix, createPackageNamespace(configClassPackage));
      }

      mxmlPrefixMappings.putAll(prefixMappings);

      // add baseClass namespace last to match code style that baseClass attribute comes last:
      if (baseClassNamespace != null) {
        mxmlPrefixMappings.put("baseClass", baseClassNamespace);
      }

      for (Map.Entry<String, String> prefixMapping : mxmlPrefixMappings.entrySet()) {
        String key = prefixMapping.getKey();
        String uriValue = prefixMapping.getValue();
        attributes.put(key.isEmpty() ? "xmlns" : "xmlns:" + key, uriValue);
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
            varsWithXmlValue.add(varDeclaration.getName());
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
    public void endDocument() throws SAXException {
      // add last new-line to end up with a correct text file format:
      currentOut.println();
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
      currentOut.print("<!--" + convertNewLines(new String(ch, start, length)) + "-->");
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
          if (!output.trim().isEmpty()) {
            output = MxmlUtils.createBindingExpression(output);
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
