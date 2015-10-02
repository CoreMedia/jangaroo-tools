package net.jangaroo.exml.compiler;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.cli.ExmlcCommandLineParser;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.json.JsonObject;
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
import net.jangaroo.jooc.cli.CommandLineParseException;
import net.jangaroo.utils.CharacterRecordingHandler;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A transpiler from EXML to MXML.
 */
public class ExmlToMxml {

  public static final String MXML_URI = "http://ns.adobe.com/mxml/2009";
  private ConfigClassRegistry configClassRegistry;

  public ExmlToMxml(ExmlConfiguration exmlConfiguration) throws IOException {
    configClassRegistry = new ConfigClassRegistry(exmlConfiguration);
  }

  private void transpileAll() throws IOException, TransformerException, ParserConfigurationException, SAXException {
    for (File exmlSource : configClassRegistry.getConfig().getSourceFiles()) {
      ExmlSourceFile exmlSourceFile = configClassRegistry.getExmlSourceFile(exmlSource);
      ExmlModel exmlModel = new ExmlToModelParser(configClassRegistry).parse(exmlSource);
      exmlToMxml(exmlSourceFile, exmlModel);
    }
  }

  private void exmlToMxml(ExmlSourceFile exmlSourceFile, ExmlModel exmlModel) throws IOException, TransformerException, ParserConfigurationException,
          SAXException {
    ExmlToConfigClassParser.parseFileWithHandler(exmlSourceFile.getSourceFile(), new ExmlToMxmlHandler(exmlSourceFile, exmlModel, System.out));
  }

  public class ExmlToMxmlHandler extends CharacterRecordingHandler implements LexicalHandler {
    private final PrintStream out;
    private PrintStream currentOut;
    private ByteArrayOutputStream cfgDefaultRecorder;
    private int lastColumn = 2;
    private Locator locator;
    private final Map<String,String> prefixMappings = new LinkedHashMap<String, String>();
    private final Map<String,String> configDefaultValues = new LinkedHashMap<String, String>();
    private final Map<String,String> configDefaultSubElements = new LinkedHashMap<String, String>();
    private boolean pendingTagClose = false;
    private String configClassPrefix;
    private Set<String> imports;
    private List<Declaration> constants;

    private final Deque<String> elementPath = new LinkedList<String>();
    private final ExmlSourceFile exmlSourceFile;
    private final ExmlModel exmlModel;
    private String currentConfigName;
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
        key = "fx";
        uriValue = MXML_URI;
      } else if (uriValue.startsWith("exml:") && !"exml:untyped".equals(uriValue)) {
        String packageName = uriValue.substring(5);
        if (packageName.equals(CompilerUtils.packageName(exmlSourceFile.getConfigClassName()))) {
          configClassPrefix = key;
        }
        uriValue = packageName + ".*";
      }
      prefixMappings.put(key, uriValue);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      String configClassQName = exmlSourceFile.getConfigClassName();
      String configClassName = CompilerUtils.className(configClassQName);
      if (configClassPrefix == null) {
        configClassPrefix = "cfg";
        prefixMappings.put(configClassPrefix, CompilerUtils.packageName(configClassQName) + ".*");
      }
      boolean isConfigElement = false;
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
          qName = handleRootNode(qName, atts);
        } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
          qName = handleAnnotation(qName, atts);
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          qName = handleCfg(atts);
        } else if (Exmlc.EXML_CONSTANT_NODE_NAME.equals(localName)) {
          qName = handleConstant(atts, configClassQName);
        } else if (Exmlc.EXML_IMPORT_NODE_NAME.equals(localName)) {
          qName = handleImport(atts);
        } else if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName)) {
          qName = null;
          cfgDefaultRecorder = new ByteArrayOutputStream();
          try {
            currentOut = new PrintStream(cfgDefaultRecorder, true, "UTF-8");
          } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
          }
        } else if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
          qName = null;
        }
      } else if (elementPath.size() == 1) {
        String thePackage = ExmlUtils.parsePackageFromNamespace(uri);
        if (thePackage == null) {
          throw new ExmlcException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package", locator.getLineNumber(), locator.getColumnNumber());
        }
        isConfigElement = true;
        qName = configClassPrefix + ":" + configClassName;

        if (isPublicApi) {
          out.printf("%n  <fx:Metadata>[%s]</fx:Metadata>", Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
        }

        if (!imports.isEmpty() || !constants.isEmpty()) {
          currentOut.printf("%n  <fx:Script><![CDATA[%n");
          for (String anImport : imports) {
            currentOut.printf("    import %s;%n", anImport);
          }
          currentOut.println();
          for (Declaration constant : constants) {
            currentOut.printf("    public static const %s:%s = %s.%s;%n", constant.getName(), constant.getType(),
                    configClassName, constant.getName());
          }
          currentOut.print("  ]]></fx:Script>");
        }
      }
      if (!elementPath.isEmpty() && elementPath.size() % 2 == 0) {
        String lastQName = elementPath.peek();
        if (lastQName != null) {
          String[] parts = lastQName.split(":");
          String prefix = parts.length == 1 ? "" : parts[0] + ":";
          qName = prefix + localName;
        }
      }
      Map<String,String> attributes = new LinkedHashMap<String, String>();
      for (Map.Entry<String, String> prefixMapping : prefixMappings.entrySet()) {
        String key = prefixMapping.getKey();
        String uriValue = prefixMapping.getValue();
        attributes.put(key.isEmpty() ? "xmlns" : "xmlns:" + key, uriValue);
      }
      prefixMappings.clear();
      if (isConfigElement) {
        attributes.put("id", "config");
        attributes.putAll(configDefaultValues);
        configDefaultValues.clear();
      }
      for (int i = 0; i < atts.getLength(); ++i) {
        String attributeName = atts.getQName(i);
        if ("id".equals(attributeName)) {
          attributeName = "extId";
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
        String whitespace = " ";
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
          currentOut.printf("%s%s=%s", whitespace, attribute.getKey(),
                  CompilerUtils.quote(attribute.getValue().replaceAll("<", "&lt;")));
          whitespace = "\n" + StringUtils.repeat(" ", lastColumn + qName.length());
        }
        pendingTagClose = true;
      }
      elementPath.push(qName);
      lastColumn = locator.getColumnNumber();
      if (isConfigElement) {
        flush();
        for (Map.Entry<String, String> configDefaultSubElement : configDefaultSubElements.entrySet()) {
          String propertyQName = configDefaultSubElement.getKey();
          if (!qName.equals(localName)) {
            propertyQName = qName.substring(0, qName.indexOf(':') + 1) + propertyQName;
          }
          currentOut.printf("%n    <%s>%n      %s%n    </%s>", propertyQName, configDefaultSubElement.getValue(), propertyQName);
        }
        configDefaultSubElements.clear();
      }
    }

    private String handleImport(Attributes atts) {
      String qName;// do not render import elements
      qName = null;
      String importedClassName = atts.getValue(Exmlc.EXML_IMPORT_CLASS_ATTRIBUTE);
      if (importedClassName != null) {
        imports.add(importedClassName);
      }
      return qName;
    }

    private String handleConstant(Attributes atts, String configClassQName) {
      String qName;// do not render constant elements
      qName = null;
      final String name = atts.getValue(Exmlc.EXML_DECLARATION_NAME_ATTRIBUTE);
      String type = atts.getValue(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE);
      if (type == null) {
        type = "String";
      }
      imports.add(configClassQName);
      constants.add(new Declaration(name, configClassQName + "." + name, type));
      return qName;
    }

    private String handleCfg(Attributes atts) {
      String qName;// do not render config elements
      qName = null;
      currentConfigName = atts.getValue("name");
      String configDefault = atts.getValue("default");
      if (configDefault != null && !configDefault.isEmpty()) {
        configDefaultValues.put(currentConfigName, configDefault);
      }
      return qName;
    }

    private String handleAnnotation(String qName, Attributes atts) {
      AnnotationAt annotationAt = AnnotationAt.BOTH; // default for "at" is "both"
      for (int i = 0; i < atts.getLength(); i++) {
        if (Exmlc.EXML_ANNOTATION_AT_ATTRIBUTE.equals(atts.getLocalName(i))) {
          // found "at" attribute: parse it (might throw ExmlcException)
          annotationAt = Exmlc.parseAnnotationAtValue(atts.getValue(i));
          break;
        }
      }
      if (annotationAt != AnnotationAt.CONFIG) {
        qName = "fx:Metadata";
      }
      return qName;
    }

    private String handleRootNode(String qName, Attributes atts) {
      String asDoc = exmlModel.getDescription();
      if (asDoc != null && !asDoc.trim().isEmpty()) {
        currentOut.println("<!---");
        currentOut.println("  " + asDoc);
        currentOut.println("-->");
      }
      ConfigClass superConfigClass = exmlSourceFile.getConfigClass().getSuperClass();
      if (superConfigClass != null) {
        String superClassName = superConfigClass.getComponentClassName();
        for (int i = 0; i < atts.getLength(); i++) {
          //baseClass attribute has been specified, so the super class of the component is actually that
          if (Exmlc.EXML_BASE_CLASS_ATTRIBUTE.equals(atts.getLocalName(i))) {
            superClassName = atts.getValue(i);
            if (superClassName.indexOf('.') == -1) {
              // fully-qualify by same package:
              superClassName = CompilerUtils.qName(CompilerUtils.packageName(exmlSourceFile.getTargetClassName()),
                      superClassName);
            }
          } else if (Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(atts.getLocalName(i))) {
            PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(atts.getValue(i));
            if (publicApiMode != PublicApiMode.FALSE) {
              isPublicApi = true;
            }
          }
        }
        qName = "baseClass:" + CompilerUtils.className(superClassName);
        prefixMappings.put("baseClass", CompilerUtils.packageName(superClassName) + ".*");
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
          currentOut.printf("</%s>", qName);
        }
      }
      startRecordingCharacters();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
          String characters = popRecordedCharacters();
          if (characters != null) {
            //configClass.addAnnotation(characters);
          }
        } else if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName)) {
          configDefaultSubElements.put(currentConfigName, cfgDefaultRecorder.toString());
          currentOut = out;
          // TODO: need to close cfgDefaultRecorder?
          cfgDefaultRecorder = null;
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          currentConfigName = null;
        }
//        if (elementPath.isEmpty() && configClass.getSuperClassName() == null) {
//          // if nothing else is specified, extend default config class depending on the config class type:
//          configClass.setSuperClassName(configClass.getType().getDefaultSuperConfigClassName());
//        }
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
      currentOut.print("<![CDATA[");
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void endCDATA() throws SAXException {
      flush();
      currentOut.print("]]>");
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
        currentOut.print(recordedCharacters);
      }
      startRecordingCharacters();
    }
  }

  public static int run(String[] argv) {
    ExmlcCommandLineParser parser = new ExmlcCommandLineParser();
    ExmlConfiguration exmlConfiguration;
    try {
      exmlConfiguration = parser.parse(argv);
    } catch (CommandLineParseException e) {
      System.err.println(e.getMessage()); // NOSONAR this is a commandline tool
      return e.getExitCode();
    }

    if (exmlConfiguration != null) {
      try {
        new ExmlToMxml(exmlConfiguration).transpileAll();
      } catch (Exception e) {
        e.printStackTrace();
        return -1;
      }
    }
    return 0;
  }

  public static void main(String[] argv) {
    int result = run(argv);
    if (result != 0) {
      System.exit(result);
    }
  }

}
