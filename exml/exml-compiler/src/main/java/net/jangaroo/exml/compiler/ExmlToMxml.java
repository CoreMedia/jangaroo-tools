package net.jangaroo.exml.compiler;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.generator.MxmlLibraryManifestGenerator;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.model.ExmlSourceFile;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.exml.parser.ExmlToModelParser;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.backend.ApiModelGenerator;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.mxml.CatalogComponentsParser;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CharacterRecordingHandler;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A tool that converts EXML source code into MXML and ActionScript source code.
 */
public class ExmlToMxml {
  private static final String LOCAL_NAMESPACE = "local";

  private ConfigClassRegistry configClassRegistry;
  private Properties migrationMap = new Properties();
  private ClassLoader resourceClassLoader = ExmlToMxml.class.getClassLoader();
  private MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();

  public ExmlToMxml(ConfigClassRegistry configClassRegistry) {
    this.configClassRegistry = configClassRegistry;

    if (configClassRegistry.getConfig().getExtAsJar() != null) {
      try {
        resourceClassLoader = new URLClassLoader(new URL[]{
                new URL("jar:" + configClassRegistry.getConfig().getExtAsJar().toURI().toString() + "!/")
        });
      } catch (IOException e) {
        throw new ExmlcException("Unable to configure resource class loader", e);
      }
    }
  }

  public File[] convert() {
    try {
      this.migrationMap.load(resourceClassLoader.getResourceAsStream("ext-as-3.4-migration-map.properties"));
    } catch (IOException e) {
      throw new ExmlcException("Unable to load migration map", e);
    }

    new CatalogComponentsParser(mxmlComponentRegistry).parse(resourceClassLoader.getResourceAsStream("catalog.xml"));

    try {
      new MxmlLibraryManifestGenerator(configClassRegistry).createManifestFile();
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

  private File exmlToMxml(ExmlSourceFile exmlSourceFile) throws IOException, SAXException {
    ExmlModel exmlModel = new ExmlToModelParser(configClassRegistry).parse(exmlSourceFile.getSourceFile());
    File sourceFile = exmlSourceFile.getSourceFile();
    File outputFile = CompilerUtils.fileFromQName(exmlSourceFile.getTargetClassName(), configClassRegistry.getConfig().getSourcePath().get(0), ".mxml");
    FileUtils.forceMkdir(outputFile.getParentFile());
    PrintStream writer = new PrintStream(new FileOutputStream(outputFile), true, net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);
    ExmlToConfigClassParser.parseFileWithHandler(sourceFile, new ExmlToMxmlHandler(exmlSourceFile, exmlModel, writer));
    return outputFile;
  }

  private class ExmlToMxmlHandler extends CharacterRecordingHandler implements LexicalHandler {
    private static final String OPEN_FX_SCRIPT = "%n  <fx:Script><![CDATA[%n";
    private static final String CLOSE_FX_SCRIPT = "  ]]></fx:Script>";
    private static final String PUBLIC_STATIC_CONST_DECLARATION_FORMAT = "    public static const %s:%s = %s;%n%n";

    private final PrintStream out;
    private PrintStream currentOut;
    private boolean insideCdata;
    private boolean insideExmlObject;
    private ByteArrayOutputStream elementRecorder;
    private int lastColumn = 1;
    private Locator locator;
    private Map<String,String> prefixMappings = new LinkedHashMap<String, String>();
    private List<String> metaData = new ArrayList<String>();
    private boolean inMetaData;
    private boolean inConfigDescription;
    private boolean inConfigDefault;
    private String originalRootName;
    private String originalRootUri;
    private String exmlPrefix;
    private final Map<String,String> configDefaultSubElements = new LinkedHashMap<String, String>();
    private final Map<String,String> configDefaultTypes = new LinkedHashMap<String, String>();
    private boolean pendingTagClose = false;
    private Set<String> imports;
    private List<Declaration> vars;
    private LinkedList<Declaration> configs;
    private Set<String> varsWithXmlValue;

    private final Deque<PathElement> elementPath = new LinkedList<PathElement>();
    private final ExmlSourceFile exmlSourceFile;
    private final ExmlModel exmlModel;
    private String currentConfigName;
    private String currentVarName;
    private boolean isPublicApi;
    private String baseClass;
    private int nsCount = 0;
    private boolean printedRootElement;

    public ExmlToMxmlHandler(ExmlSourceFile exmlSourceFile, ExmlModel exmlModel, PrintStream out) {
      this.exmlSourceFile = exmlSourceFile;
      this.exmlModel = exmlModel;
      currentOut = this.out = out;
    }

    @Override
    public void startDocument() throws SAXException {
      currentOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      imports = new LinkedHashSet<String>();
      vars = new ArrayList<Declaration>();
      varsWithXmlValue = new HashSet<String>();
      configs = new LinkedList<Declaration>();
      startRecordingCharacters();

      // add local namespace last to match the local package
      prefixMappings.put(LOCAL_NAMESPACE, createPackageNamespace(exmlModel.getPackageName()));

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
      String qName = originalQName;
      Map<String,String> attributes = new LinkedHashMap<String, String>();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
          handleRootNode(atts);
          originalRootName = originalQName;
          originalRootUri = uri;
          qName = null;
        } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
          handleAnnotation(atts);
          inMetaData = true;
          qName = null;
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          qName = handleCfg(atts);
        } else if (Exmlc.EXML_CONSTANT_NODE_NAME.equals(localName)) {
          // ExmlModel is used for constants
          qName = null;
        } else if (Exmlc.EXML_VAR_NODE_NAME.equals(localName)) {
          qName = handleVar(atts);
        } else if (Exmlc.EXML_DECLARATION_VALUE_NODE_NAME.equals(localName)) {
          qName = handleInnerElement();
        } else if (Exmlc.EXML_IMPORT_NODE_NAME.equals(localName)) {
          qName = handleImport(atts);
        } else if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName)) {
          inConfigDefault = true;
          qName = handleInnerElement();
        } else if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
          inConfigDescription = currentConfigName != null;
          qName = null;
        } else if (Exmlc.EXML_OBJECT_NODE_NAME.equals(localName)) {
          // suppress empty <exml:object>s; they only contain code which is simply wrapped by { ... } in MXML:
          if (atts.getLength() == 0 && printedRootElement) {
            qName = null;
          }
          insideExmlObject = true;
        }
      }
      boolean isSubElement = elementPath.size() > 1;
      boolean isPropertyElement = isSubElement && elementPath.size() % 2 == 0;
      if (isPropertyElement) {
        String lastQName = elementPath.peek().newName;
        if (lastQName != null) {
          String prefix = parsePrefixAndLocalName(lastQName)[0];
          String targetName = getTargetClassAttributeName(uri, elementPath.peek().originalName, localName);
          if (targetName.length() > 0) {
            qName = qName(prefix, targetName);
          } else {
            // attribute should be removed
            qName = null;
          }
        }
      } else if (elementPath.size() > 0 && !ExmlUtils.isExmlNamespace(uri)) {
        qName = getTargetClassElementQName(uri, qName);
      }
      if (isNewRoot(uri, originalQName)) {
        addNamespaceMappings(attributes);
      }
      if (qName != null && currentVarName != null) {
        attributes.put("id", currentVarName);
        currentVarName = null;
      }
      boolean indentAttributes = true;
      if (qName != null && currentConfigName != null && !"Array".equals(configs.getLast().getType())) {
        attributes.put("id", currentConfigName);
        indentAttributes = false;
      }
      if (inConfigDefault && qName != null && !configDefaultTypes.containsKey(currentConfigName)) {
        configDefaultTypes.put(currentConfigName, originalQName);
      }
      for (int i = 0; i < atts.getLength(); ++i) {
        String attributeName = atts.getQName(i);
        if (Exmlc.EXML_BASE_CLASS_ATTRIBUTE.equals(attributeName) ||
                Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(attributeName)) {
          continue;
        }
        if (isPropertyElement && Exmlc.EXML_MODE_ATTRIBUTE.equals(attributeName)) {
          attributeName = qName(exmlPrefix, Exmlc.EXML_MODE_ATTRIBUTE);
        } else if (!isPropertyElement) {
          attributeName = getTargetClassAttributeName(uri, originalQName, attributeName);
        }
        if ("id".equals(attributeName)) {
          attributeName = "id_";
        }

        if (attributeName.length() > 0) {
          attributes.put(attributeName, atts.getValue(i));
        }
      }
      if (!elementPath.isEmpty() && elementPath.peek().newName == null) {
        if (!inConfigDefault || (ExmlUtils.isExmlNamespace(uri) && Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName))) {
          popRecordedCharacters();
        }
      }
      if (inConfigDescription || inMetaData) {
        startRecordingCharacters();
      }
      if ("exml:object".equals(qName)) {
        qName = "fx:Object";
      }
      if (isObjectLevel() && qName != null) {
        qName = getMappedComponentName(uri, qName, originalQName);
      }
      if (qName != null) {
        if (isNewRoot(uri, originalQName) && baseClass != null) {
          qName = baseClass;
        }
        printStartTag(uri, originalQName, qName, attributes, indentAttributes);
      }
      if (isNewRoot(uri, originalQName)) {
        flushPendingTagClose();
        imports.add(exmlSourceFile.getConfigClassName());
        addImportsForConfigs();
        addImportsForConstants();
        printHeader();
        printedRootElement = true;
      }
      elementPath.push(new PathElement(originalQName, qName));
      lastColumn = locator.getColumnNumber();
    }

    private void addNamespaceMappings(Map<String, String> attributes) {
      Map<String, String> mxmlPrefixMappings = new LinkedHashMap<String, String>();
      mxmlPrefixMappings.put("fx", MxmlUtils.MXML_NAMESPACE_URI);
      exmlPrefix = findPrefix(Exmlc.EXML_NAMESPACE_URI);
      if (exmlPrefix == null) {
        System.err.println("[WARN] ExmlToMxml: no EXML namespace found!");
        exmlPrefix = "exml";
        mxmlPrefixMappings.put("exml", Exmlc.EXML_NAMESPACE_URI);
      }

      mxmlPrefixMappings.putAll(prefixMappings);

      for (Map.Entry<String, String> prefixMapping : mxmlPrefixMappings.entrySet()) {
        String key = prefixMapping.getKey();
        attributes.put(key.isEmpty() ? "xmlns" : "xmlns:" + key, prefixMapping.getValue());
      }
    }

    private boolean isObjectLevel() {
      return elementPath.size() % 2 == 1;
    }

    private void printStartTag(String uri, String originalQName, String qName, Map<String, String> attributes, boolean indentAttributes) {
      flush();
      String originalName = isNewRoot(uri, originalQName) ? originalRootName : originalQName;
      int column = isNewRoot(uri, originalQName) ? 1 : lastColumn;
      int indent = column + originalName.length();
      int qNameLengthDelta = originalName.length() - qName.length();
      String firstIndent = qNameLengthDelta == 0 || attributes.size() <= 1
              ? ""
              : qNameLengthDelta > 0
              ? StringUtils.repeat(" ", qNameLengthDelta)
              : String.format("%n%s", StringUtils.repeat(" ", indent));
      currentOut.printf("<%s%s", qName, firstIndent);
      printAttributes(attributes, indentAttributes ? indent + 1 : 0);
      pendingTagClose = true;
    }

    private void printHeader() throws SAXException {
      printMetadata();
      printOpenScript();
      printImports();
      printConstants();
      printVars();
      printInitializer();
      printConstructor();
      printConfigVars();
      printCloseScript();
      printDeclarations();
    }

    private void printImports() {
      for (String anImport : imports) {
        currentOut.printf("    import %s;%n", anImport);
      }
      if (!imports.isEmpty()) {
        currentOut.println();
      }
    }

    private void printConstants() {
      if (Exmlc.EXML_COMPONENT_NODE_NAME.equals(parsePrefixAndLocalName(originalRootName)[1])) {
        currentOut.printf(
                PUBLIC_STATIC_CONST_DECLARATION_FORMAT,
                "xtype", "String", CompilerUtils.quote(exmlModel.getConfigClass().getFullName()));
      }
      for (Declaration constant : exmlModel.getConfigClass().getConstants()) {
        printASDoc(constant.getDescription());
        currentOut.printf(
                PUBLIC_STATIC_CONST_DECLARATION_FORMAT,
                constant.getName(), constant.getType(), constant.getValue()
        );
      }
    }

    private void addImportsForConfigs() throws SAXException {
      for (Declaration config : configs) {
        String defaultType = configDefaultTypes.get(config.getName());
        if (defaultType != null && !config.getType().equals(defaultType)) {
          addImport(config.getType());
        }
        if (!hasDefaultConstructor(config.getType())) {
          addImport(config.getType());
        }
      }
    }

    private void addImportsForConstants() throws SAXException {
      for (Declaration constant : exmlModel.getConfigClass().getConstants()) {
        addImport(constant.getType());
      }
    }

    private boolean hasDefaultConstructor(String name) throws SAXException {
      String qname = resolveQName(name);
      if (AS3Type.typeByName(qname) != null) {
        return true;
      }

      CompilationUnit compilationUnit = configClassRegistry.getJangarooParser().getCompilationUnit(qname);
      if (compilationUnit != null) {
        try {
          CompilationUnitModel compilationUnitModel = new ApiModelGenerator(false).generateModel(compilationUnit);
          if (compilationUnitModel.getClassModel().isInterface()) {
            return false;
          }
          MethodModel constructor = compilationUnitModel.getClassModel().getConstructor();
          if (constructor != null) {
            List<ParamModel> params = constructor.getParams();
            return params.size() == 0 || params.get(0).isOptional() || params.get(0).isRest() || params.get(0).getName().equals("config");
          }
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
      return true;
    }

    private String getConfigClassFromTargetClass(String name) throws SAXException {
      CompilationUnit compilationUnit = configClassRegistry.getJangarooParser().getCompilationUnit(resolveQName(name));
      if (compilationUnit != null) {
        try {
          CompilationUnitModel compilationUnitModel = new ApiModelGenerator(false).generateModel(compilationUnit);
          MethodModel constructor = compilationUnitModel.getClassModel().getConstructor();
          if (constructor != null && constructor.getParams().size() > 0) {
            String type = constructor.getParams().get(0).getType();
            if (type != null && CompilerUtils.packageName(type).isEmpty()) {
              for (AstNode astNode : compilationUnit.getDirectives()) {
                if (astNode instanceof ImportDirective) {
                  ImportDirective importDirective = (ImportDirective) astNode;
                  if (CompilerUtils.className(importDirective.getQualifiedName()).equals(type)) {
                    return importDirective.getQualifiedName();
                  }
                }
              }
            }
            return type;
          }
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
      return null;
    }

    private String resolveQName(String name) {
      if (!name.contains(".")) {
        for (String anImport : imports) {
          if (CompilerUtils.className(anImport).equals(name)) {
            return anImport;
          }
        }
      }
      return name;
    }

    private String resolveQNameFromModel(String name) {
      if (!name.contains(".")) {
        for (String anImport : exmlModel.getImports()) {
          if (CompilerUtils.className(anImport).equals(name)) {
            return anImport;
          }
        }
        String localQName = CompilerUtils.qName(exmlModel.getPackageName(), name);
        if (configClassRegistry.getJangarooParser().getCompilationUnit(localQName) != null) {
          return localQName;
        }
      }
      return name;
    }

    private boolean isNewRoot(String uri, String qName) {
      if (qName == null || elementPath.size() != 1) {
        return false;
      }
      String localName = parsePrefixAndLocalName(qName)[1];
      return !ExmlUtils.isExmlNamespace(uri) || Exmlc.EXML_OBJECT_NODE_NAME.equals(localName);
    }

    private void printConfigVars() throws SAXException {
      for (Declaration config : configs) {
        String defaultType = configDefaultTypes.get(config.getName());
        if (!"Array".equals(config.getType()) && (!hasDefaultConstructor(config.getType()) || (defaultType != null && !config.getType().equals(defaultType)))) {
          currentOut.printf("%n");
          if (!hasDefaultConstructor(config.getType())) {
            printASDoc(config.getDescription());
          }
          currentOut.printf("    [Bindable]%n");
          currentOut.printf("    public var %s:%s", config.getName(), config.getType());
          if (config.getValue() != null) {
            currentOut.printf(" = %s", MxmlUtils.getBindingExpression(config.getValue()).trim());
          }
          currentOut.printf(";%n");
        }
      }
    }

    private void printConstructor() {
      String configClassName = CompilerUtils.className(exmlSourceFile.getConfigClassName());
      currentOut.printf("    public native function %s(config:%s = null);%n", exmlModel.getClassName(), configClassName);
    }

    private void printInitializer() {
      String configClassName = CompilerUtils.className(exmlSourceFile.getConfigClassName());
      if (varsWithXmlValue.size() != vars.size()) {
        currentOut.printf("    // called by generated constructor code%n");
        currentOut.printf("    private function __initialize__(config:%s):void {%n", configClassName);
        for (Declaration var : vars) {
          if (!(varsWithXmlValue.contains(var.getName()))) {
            currentOut.printf("      %s = %s;%n", var.getName(), formatValue(var.getValue(), var.getType()));
          }
        }
        currentOut.printf("    }%n%n");
      }
    }

    private void printVars() {
      String configClassName = CompilerUtils.className(exmlSourceFile.getConfigClassName());
      currentOut.printf("    private var config:%s;%n", configClassName);

      for (Declaration var : vars) {
        String type = var.getType();
        if (type == null || type.isEmpty()) {
          type = "*";
        }
        currentOut.printf("    private var %s:%s;%n", var.getName(), type);
      }
      currentOut.println();
    }

    private void printDeclarations() throws SAXException {
      boolean hasDeclarations = false;
      String targetClassQName = exmlSourceFile.getTargetClassName();
      String targetClassName = CompilerUtils.className(targetClassQName);

      for (Declaration var : vars) {
        if (varsWithXmlValue.contains(var.getName()) && !"".equals(var.getValue())) {
          if (!hasDeclarations) {
            printOpenDeclarations();
            hasDeclarations = true;
          }
          currentOut.printf("%n    %s", var.getValue());
        }
      }

      for (Declaration config : configs) {
        if (!hasDefaultConstructor(config.getType())) {
          continue;
        }

        QName qName = findTypeAndPrefix(config.getType());
        String type = qName.getLocalPart();
        String prefix = qName.getPrefix();

        if (!hasDeclarations) {
          printOpenDeclarations();
          hasDeclarations = true;
        } else {
          currentOut.printf("%n");
        }
        if (config.getDescription() != null) {
          currentOut.printf("%n    ");
          printComment(config.getDescription());
        }
        currentOut.printf("%n    ");

        if (configDefaultSubElements.containsKey(config.getName()) && !"Array".equals(config.getType())) {
          currentOut.printf(configDefaultSubElements.get(config.getName()));
        } else {
          currentOut.printf("<%s id=\"%s\"", qName(prefix, type), config.getName());

          String value = config.getValue();
          if (value != null) {
            currentOut.printf(">%s</%s>", value, qName(prefix, type));
          } else if (configDefaultSubElements.containsKey(config.getName())) {
            // Array
            currentOut.printf(">%n      ");
            currentOut.printf(configDefaultSubElements.get(config.getName()));
            currentOut.printf("%n    </%s>", qName(prefix, type));
          } else {
            currentOut.printf("/>");
          }
        }
      }

      if (hasDeclarations) {
        printCloseDeclarations();
      } else {
        currentOut.println();
      }
    }

    private void printOpenDeclarations() {
      currentOut.printf("%n  <fx:Declarations>");
    }

    private void printCloseDeclarations() {
      currentOut.printf("%n  </fx:Declarations>%n");
    }

    private QName findTypeAndPrefix(String type) throws SAXException {
      type = "*".equals(type) ? "Object" : type;
      String prefix;
      List<QName> qNames = mxmlComponentRegistry.getQNamesByClassName(resolveQName(type));
      if (qNames != null) {
        QName qName = findQName(CompilerUtils.className(type), qNames);
        prefix = findPrefix(qName.getNamespaceURI());
        type = qName.getLocalPart();
      } else {
        prefix = findPrefixForType(type);
        type = CompilerUtils.className(type);
      }
      return new QName(null, type, prefix == null ? "" : prefix);
    }

    private void printASDoc(String text) {
      if (text != null) {
        currentOut.printf("    /**%n");
        for (String line : text.trim().split("[\n\r]")) {
          currentOut.printf("     * %s%n", line.trim());
        }
        currentOut.printf("     */%n");
      }
    }

    private void printMetadata() {
      if (isPublicApi) {
        currentOut.printf("%n  <fx:Metadata>[%s]</fx:Metadata>", Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
      }
      for (String md : metaData) {
        currentOut.printf("%n  <fx:Metadata>[%s]</fx:Metadata>", md);
      }
    }

    private void printOpenScript() {
      currentOut.printf(OPEN_FX_SCRIPT);
    }

    private void printCloseScript() {
      currentOut.printf(CLOSE_FX_SCRIPT);
    }

    private String getMappedComponentName(String uri, String qName, String originalQName) {
      ConfigClass configClass = getConfigClass(uri, originalQName);
      if (configClass != null) {
        String componentClassName = configClass.getComponentClassName();
        String mappedClassName = (String) migrationMap.get(componentClassName);
        List<QName> qNames = mxmlComponentRegistry.getQNamesByClassName(mappedClassName != null ? mappedClassName : componentClassName);
        if (qNames != null) {
          return findQName(originalQName, qNames).getLocalPart();
        }
      }
      return qName;
    }

    private QName findQName(String originalQName, List<QName> qNames) {
      for (QName name : qNames) {
        if (name.getLocalPart().toLowerCase().equals(originalQName.toLowerCase())) {
          return name;
        }
      }
      return qNames.get(qNames.size() - 1);
    }

    private String findPrefixForPackage(String packageName, boolean configUriOnly) {
      if (packageName.isEmpty()) {
        return "fx";
      }
      String prefix = findPrefix(Exmlc.EXML_CONFIG_URI_PREFIX + packageName);
      return prefix == null && !configUriOnly ? findPrefix(createPackageNamespace(packageName)) : prefix;
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
        value = convertNewLines(CompilerUtils.denormalizeAttributeValue(value));
        if (!ExmlUtils.isCodeExpression(value)) {
          // escape all opening curly braces, as MXML also recognizes them anywhere inside a string:
          value = value.replaceAll("\\{", "\\\\{");
        }
        value = escapeXml(value);
        String quotes;
        if (value.contains("\"") && !value.contains("'")) {
          quotes = "'";
        } else {
          quotes = "\"";
          value = value.replaceAll("\"", "&quot;");
        }
        currentOut.printf("%s%s=%s%s%s", whitespace, attribute.getKey(), quotes, value, quotes);
        if (indent > 0) {
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
      return new Declaration(name, value, type);
    }

    private void addImport(String type) {
      String packageName = CompilerUtils.packageName(type);
      if (!packageName.isEmpty()) {
        if (findPrefixForPackage(packageName, true) == null) {
          imports.add(type);
        }
      }
    }

    private String handleVar(Attributes atts) {
      Declaration declaration = createDeclaration(atts);
      vars.add(declaration);
      addImport(declaration.getType());
      currentVarName = declaration.getName();
      return null; // do not render var elements
    }

    private String handleCfg(Attributes atts) throws SAXException {
      Declaration declaration = createDeclaration(atts);
      currentConfigName = declaration.getName();
      if (declaration.getType() != null) {
        // even if this config does not add anything to the target class,
        // its type must be imported, because other code may take advantage of this import.
        addImport(declaration.getType());
        String mappedClassName = getMappedClassName(declaration.getType());
        if (mappedClassName != null) {
          declaration = new Declaration(declaration.getName(), declaration.getValue(), mappedClassName);
        }
      }
      configs.add(declaration);
      if (hasDefaultConstructor(declaration.getType())) {
        addPrefixMapping(declaration.getType());
      }
      String configDefault = atts.getValue("default");
      if (configDefault != null && !configDefault.isEmpty()) {
        declaration.setValue(configDefault);
      }
      return null; // do not render config elements
    }

    private String addPrefixMapping(String type) throws SAXException {
      String prefix = findPrefixForType(type);
      if (prefix == null) {
        prefix = createPrefixMapping(createPackageNamespace(CompilerUtils.packageName(resolveQName(type))));
      }
      return prefix;
    }

    private String createPrefixMapping(String namespace) throws SAXException {
      String prefix = "ns" + (++nsCount);
      prefixMappings.put(prefix, namespace);
      return prefix;
    }

    private String findPrefixForType(String type) throws SAXException {
      String qName = resolveQName(type);
      String packageName = CompilerUtils.packageName(qName);
      String prefix = findPrefixForPackage(packageName, false);
      if (prefix == null) {
        ConfigClass configClass = configClassRegistry.getConfigClassByName(qName);
        if (configClass != null) {
          qName = configClass.getComponentClassName();
          packageName = CompilerUtils.packageName(qName);
          prefix = findPrefixForPackage(packageName, false);
        } else {
          String guessedConfigClassName = getConfigClassFromTargetClass(qName);
          if (guessedConfigClassName != null) {
            configClass = configClassRegistry.getConfigClassByName(guessedConfigClassName);
            if (configClass != null && configClass.getComponentClassName().equals(qName)) {
              packageName = CompilerUtils.packageName(guessedConfigClassName);
              prefix = findPrefixForPackage(packageName, false);
            }
          }
        }
      }
      return prefix;
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

    private void handleRootNode(Attributes atts) throws SAXException {
      String asDoc = exmlModel.getDescription();
      if (asDoc != null && !asDoc.trim().isEmpty()) {
        printComment(asDoc);
        currentOut.println();
      }

      baseClass = atts.getValue(Exmlc.EXML_BASE_CLASS_ATTRIBUTE);
      if (baseClass != null && !baseClass.isEmpty()) {
        // baseClass attribute has been specified, so the super class of the component is actually that:
        baseClass = qName(addPrefixMapping(resolveQNameFromModel(baseClass)), CompilerUtils.className(baseClass));
      }

      String publicApiValue = atts.getValue(Exmlc.EXML_PUBLIC_API_ATTRIBUTE);
      if (publicApiValue != null && !publicApiValue.isEmpty()) {
        PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(publicApiValue);
        if (publicApiMode == PublicApiMode.TRUE) {
          isPublicApi = true;
        }
      }
    }

    private void printComment(String text) {
      currentOut.print("<!--- " + convertNewLines(text).replaceAll("--", "&#45;&#45;") + " -->");
    }

    private String formatValue(String value, String type) {
      return value == null ? null
              : JsonObject.valueToString(ExmlToModelParser.getAttributeValue(value, type), 2, 4);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      flushPendingTagClose();
      String cdata = new String(ch, start, length);
      if (!(elementPath.size() == 2 && elementPath.peek().newName == null && isNotEmptyText(cdata.trim())) || inMetaData) {
        if (insideCdata) {
          cdata = escapeXml(cdata);
        }
        super.characters(cdata.toCharArray(), 0, cdata.length());
      }
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (elementPath.size() == 1) {
        // ignoring the root element
        return;
      }
      qName = elementPath.pop().newName;
      if (inMetaData) {
        inMetaData = false;
        metaData.add(popRecordedCharacters());
      } else if (inConfigDescription) {
        inConfigDescription = false;
        configs.getLast().setDescription(popRecordedCharacters());
      }
      if (qName != null) {
        if (pendingTagClose) {
          currentOut.print("/>");
          pendingTagClose = false;
        } else {
          flush();
          if (elementPath.size() == 1) {
            currentOut.printf("%n");
          }
          currentOut.printf("</%s>", qName);
        }
      } else if (insideExmlObject) {
        flush();
      }
      insideExmlObject = false; // <exml:object>s cannot be nested.
      startRecordingCharacters();
      if (ExmlUtils.isExmlNamespace(uri)) {
        if (Exmlc.EXML_CFG_DEFAULT_NODE_NAME.equals(localName) ||
                Exmlc.EXML_DECLARATION_VALUE_NODE_NAME.equals(localName)) {
          String value = elementRecorder.toString();
          if (currentConfigName != null) {
            configDefaultSubElements.put(currentConfigName, value);
          } else if (!vars.isEmpty()) {
            Declaration varDeclaration = vars.get(vars.size() - 1);
            varDeclaration.setValue(value);
            varsWithXmlValue.add(varDeclaration.getName());
          }
          currentOut.close();
          currentOut = out;
          elementRecorder = null;
          inConfigDefault = false;
        } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
          currentConfigName = null;
        } else if (Exmlc.EXML_VAR_NODE_NAME.equals(localName)) {
          currentVarName = null;
        }
      }
      lastColumn = locator.getColumnNumber();
    }

    private String getTargetClassElementQName(String uri, String qName) {
      ConfigClass configClass = getConfigClass(uri, qName);
      if (configClass != null) {
        String targetClassName = configClass.getComponentClassName();
        if (targetClassName != null) {
          if (migrationMap.containsKey(targetClassName)) {
            targetClassName = (String) migrationMap.get(targetClassName);
          }
          return formatQName(parsePrefixAndLocalName(qName)[0], CompilerUtils.className(targetClassName));
        }
      }
      return null;
    }

    private String getTargetClassAttributeName(String uri, String qName, String attributeName) {
      ConfigClass configClass = getConfigClass(uri, qName);
      while (configClass != null) {
        String classAndAttribute = configClass.getFullName() + "#" + attributeName;
        if (migrationMap.containsKey(classAndAttribute)) {
          String targetName = (String) migrationMap.get(classAndAttribute);
          return targetName.substring(targetName.indexOf('#') + 1);
        } else {
          configClass = configClass.getSuperClass();
        }
      }
      return attributeName;
    }

    private String getMappedClassName(String type) {
      String mappedClassName = null;
      ConfigClass configClass = configClassRegistry.getConfigClassByName(resolveQName(type));
      if (configClass != null) {
        mappedClassName = configClass.getComponentClassName();
        if (migrationMap.containsKey(mappedClassName)) {
          mappedClassName = (String) migrationMap.get(mappedClassName);
        }
      }
      return mappedClassName;
    }

    private ConfigClass getConfigClass(String uri, String qName) {
      String configClassName = getClassName(uri, qName);
      return configClassName == null ? null : configClassRegistry.getConfigClassByName(configClassName);
    }

    private String getClassName(String uri, String qName) {
      String packageName = ExmlUtils.parsePackageFromNamespace(uri);
      return packageName == null ? null : CompilerUtils.qName(packageName, parsePrefixAndLocalName(qName)[1]);
    }

    @Override
    public void endDocument() throws SAXException {
      if (!printedRootElement && ExmlUtils.isExmlNamespace(originalRootUri)) {
        ConfigClassType configClassType = ConfigClassType.fromExmlRootNodeName(parsePrefixAndLocalName(originalRootName)[1]);
        String superClassName = configClassType.getDefaultSuperConfigClassName();
        String localName = configClassType == ConfigClassType.CLASS ? Exmlc.EXML_OBJECT_NODE_NAME : CompilerUtils.className(superClassName);
        String prefix = configClassType == ConfigClassType.CLASS ? findPrefix(originalRootUri) : findPrefixForType(superClassName);
        if (prefix == null) {
          prefix = createPrefixMapping("exml:ext.config");
        }
        String namespace = prefixMappings.get(prefix);
        startElement(namespace, localName, qName(prefix, localName), new AttributesImpl());
        endElement(namespace, localName, qName(prefix, localName));
      }
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
      insideCdata = true;
      lastColumn = locator.getColumnNumber();
    }

    @Override
    public void endCDATA() throws SAXException {
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

    private String qName(String prefix, String localName) {
      return prefix + (prefix.isEmpty() ? "" : ":") + localName;
    }

    private void flush() {
      flushPendingTagClose();
      String recordedCharacters = popRecordedCharacters();
      if (recordedCharacters != null) {
        String output = convertNewLines(recordedCharacters);
        if (insideExmlObject) {
          if (!output.trim().isEmpty()) {
            output = MxmlUtils.createBindingExpression(output);
          }
        }
        currentOut.print(output);
      }
      startRecordingCharacters();
    }
  }

  private static String convertNewLines(String output) {
    return output.replaceAll("\n", System.getProperty("line.separator"));
  }

  private static String[] parsePrefixAndLocalName(String qName) {
    int colonPos = qName.indexOf(':');
    String prefix = colonPos == -1 ? "" : qName.substring(0, colonPos);
    String localName = qName.substring(colonPos + 1);
    return new String[]{prefix, localName};
  }

  private static String formatQName(String prefix, String localName) {
    return prefix == null || prefix.isEmpty() ? localName : String.format("%s:%s", prefix, localName);
  }

  public static boolean isNotEmptyText(String str) {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (Character.isLetterOrDigit(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private static class PathElement {
    String originalName;
    String newName;

    public PathElement(String originalName, String newName) {
      this.originalName = originalName;
      this.newName = newName;
    }
  }
}
