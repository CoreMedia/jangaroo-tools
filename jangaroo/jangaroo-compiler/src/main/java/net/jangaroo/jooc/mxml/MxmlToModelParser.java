package net.jangaroo.jooc.mxml;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.backend.ApiModelGenerator;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.json.Code;
import net.jangaroo.jooc.json.Json;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.util.PreserveLineNumberHandler;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.jangaroo.jooc.util.PreserveLineNumberHandler.getLineNumber;


public final class MxmlToModelParser {

  public static final String MXML_DECLARATIONS = "Declarations";
  public static final String MXML_SCRIPT = "Script";
  public static final String MXML_METADATA = "Metadata";
  public static final String MXML_ID_ATTRIBUTE = "id";

  private static final String EXT_CONFIG_META_NAME = "ExtConfig";
  private static final String TARGET_ANNOTATION_PARAMETER_NAME = "target";
  private static final String EXT_CONFIG_PACKAGE = "ext.config";
  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final String EXT_CONTAINER_CONFIG_QNAME = "ext.config.container";
  private static final String EXT_CONTAINER_DEFAULTS_PROPERTY = "defaults";
  private static final String EXT_CONTAINER_DEFAULT_TYPE_PROPERTY = "defaultType";

  private final JangarooParser jangarooParser;
  private CompilationUnitModel compilationUnitModel;
  private JsonObject jsonObject;
  private JsonObject cfgDefaults;
  private ParamModel configParamModel;
  private Map<String, Json> privateVars;

  public MxmlToModelParser(JangarooParser jangarooParser) {
    this.jangarooParser = jangarooParser;
  }

  /**
   * Parses the MXML file into a CompilationUnitModel
   * @param in the input source to parse
   * @return the parsed model
   * @throws java.io.IOException if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  public CompilationUnitModel parse(InputSource in) throws IOException, SAXException {
    String qName = CompilerUtils.qNameFromRelativPath(in.getRelativePath());
    compilationUnitModel = new CompilationUnitModel(CompilerUtils.packageName(qName),
            new ClassModel(CompilerUtils.className(qName)));
    privateVars = new LinkedHashMap<String, Json>();

    BufferedInputStream inputStream = null;
    try {
      inputStream = new BufferedInputStream(in.getInputStream());
      parse(inputStream);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return compilationUnitModel;
  }

  /**
   * Parse the input stream content into a model.
   * Close the input stream after reading.
   *
   * @param inputStream the input stream
   * @throws java.io.IOException  if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  private void parse(InputStream inputStream) throws IOException, SAXException {
    Document document = buildDom(inputStream);
    Element objectNode = document.getDocumentElement();

    jsonObject = new JsonObject();
    cfgDefaults = null;
    String superClassName = createClassNameFromNode(objectNode);

    if (superClassName == null) {
      throw Jooc.error("Could not resolve super class from node " + objectNode.getNamespaceURI() + ":" + objectNode.getLocalName());
    }
    if (superClassName.equals(compilationUnitModel.getQName())) {
      throw Jooc.error("Cyclic inheritance error: super class and this component are the same!. There is something wrong!");
    }
    compilationUnitModel.addImport("ext.Ext");
    ClassModel classModel = compilationUnitModel.getClassModel();
    classModel.setSuperclass(superClassName);
    compilationUnitModel.addImport(superClassName);

    Element superConfigElement = createFields(objectNode);
    if (configParamModel == null) {
      throw Jooc.error("Jangaroo MXML file must contain public constructor with one optional parameter of config class type.");
    }
    if (superConfigElement != null) {
      fillModelAttributes(jsonObject, superConfigElement);
    }

    String bodyCode = classModel.getBodyCode();
    if (cfgDefaults != null && !cfgDefaults.isEmpty()) {
      Pattern constructorPattern = Pattern.compile("\\s*public\\s+function\\s+" + classModel.getName() + "\\s*\\(\\s*([A-Za-z_][A-Za-z_0-9]*)\\s*:\\s*([A-Za-z_][A-Za-z_0-9.]*)\\s*=\\s*null\\s*\\)\\s*\\{");
      Matcher constructorMatcher = constructorPattern.matcher(bodyCode);
      if (!constructorMatcher.find()) {
        throw Jooc.error("Jangaroo MXML must have constructor.");
      }
      int constructorBodyStart = constructorMatcher.end();
      String cfgDefaultsCode = String.format("%n      %s = %s;", configParamModel.getName(),
              formatTypedExmlApply(configParamModel, cfgDefaults));
      bodyCode = bodyCode.substring(0, constructorBodyStart) + cfgDefaultsCode + bodyCode.substring(constructorBodyStart);
    }

    Pattern superCallPattern = Pattern.compile("super\\(([A-Za-z_][A-Za-z_0-9]*)\\)");
    Matcher superCallMatcher = superCallPattern.matcher(bodyCode);
    if (!superCallMatcher.find()) {
      throw Jooc.error("Jangaroo MXML file must contain constructor with super(config) call.");
    }

    StringBuilder preSuperCode = new StringBuilder();
    for (Map.Entry<String, Json> varJsonEntry : privateVars.entrySet()) {
      if (varJsonEntry.getValue() != null) {
        preSuperCode.append(String.format("%s = %s;%n      ", varJsonEntry.getKey(),
                varJsonEntry.getValue().toString(2, 6)));
      }
    }
    int beforeSuperCall = superCallMatcher.start();

    compilationUnitModel.addImport("net.jangaroo.ext.Exml");
    String superConfigParam = formatTypedExmlApply(configParamModel, jsonObject);
    bodyCode = superCallMatcher.replaceFirst(String.format("super(%s)", superConfigParam.replaceAll("[$]", "\\\\\\$")));
    bodyCode = bodyCode.substring(0, beforeSuperCall) + preSuperCode + bodyCode.substring(beforeSuperCall);
    classModel.setBodyCode(bodyCode);
  }

  private static String formatTypedExmlApply(ParamModel configParamModel, JsonObject someJsonObject) {
    return String.format("%s(net.jangaroo.ext.Exml.apply(%s, %s))", configParamModel.getType(), someJsonObject.toString(2, 6), configParamModel.getName());
  }

  private void fillModelAttributes(JsonObject jsonObject, Element componentNode) throws IOException {
    CompilationUnitModel configClassModel = getCompilationUnitModel(componentNode);
    NamedNodeMap attributes = componentNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String attributeName = attribute.getLocalName();
      if (MXML_ID_ATTRIBUTE.equals(attributeName)) {
        // TODO: assign value to field!
        continue;
      }
      if (Exmlc.EXML_NAMESPACE_URI.equals(attribute.getNamespaceURI()) && "access".equals(attributeName)) {
        continue; // ignore "access" attribute here; it is handled by createFields().
      }
      String attributeValue = attribute.getValue();
      MemberModel configAttribute = findPropertyModel(configClassModel.getClassModel(), attributeName);
      if (configAttribute != null) { 
        if (!configAttribute.isWritable()) {
          throw new CompilerError("Attempt to assign read-only property " + attributeName);
        }
        if (configAttribute.isProperty()) {
          Iterator<AnnotationModel> annotations = ((PropertyModel) configAttribute).getSetter().getAnnotations(EXT_CONFIG_META_NAME).iterator();
          if (annotations.hasNext()) {
            Iterator<AnnotationPropertyModel> properties = annotations.next().getProperties().iterator();
            if (properties.hasNext()) {
              attributeName = properties.next().getStringValue();
            }
          }
        }
      }
      jsonObject.set(attributeName, getAttributeValue(attributeValue, configAttribute == null ? null : configAttribute.getType()));
    }
    fillModelAttributesFromSubElements(jsonObject, componentNode, configClassModel);
  }

  public static Object getAttributeValue(String attributeValue, String type) {
    String trimmedAttributeValue = attributeValue.trim();
    if (MxmlUtils.isBindingExpression(trimmedAttributeValue)) {
      return JsonObject.code(MxmlUtils.getBindingExpression(trimmedAttributeValue));
    } else {
      AS3Type as3Type = type == null ? AS3Type.ANY : AS3Type.typeByName(type);
      if (AS3Type.ANY.equals(as3Type)) {
        as3Type = CompilerUtils.guessType(attributeValue);
      }
      if (as3Type != null) {
        switch (as3Type) {
          case STRING:
            return attributeValue.replaceAll("\\\\\\{", "{"); // unescape escaped curly brace, \{ -> {
          case BOOLEAN:
            return Boolean.parseBoolean(attributeValue);
          case NUMBER:
            return Double.parseDouble(attributeValue);
          case UINT:
          case INT:
            return Long.parseLong(attributeValue);
          case ARRAY:
            return new JsonArray(attributeValue);
        }
      }
    }
    // code expression, Object or specific type. We don't care (for now).
    return attributeValue;
  }

  private static final Map<String, Code> CONFIG_MODE_TO_AT_VALUE = new HashMap<String, Code>(); static {
    CONFIG_MODE_TO_AT_VALUE.put("append",  JsonObject.code("net.jangaroo.ext.Exml.APPEND"));
    CONFIG_MODE_TO_AT_VALUE.put("prepend", JsonObject.code("net.jangaroo.ext.Exml.PREPEND"));
  }

  private void fillModelAttributesFromSubElements(JsonObject jsonObject, Element componentNode, CompilationUnitModel configClass) throws IOException {
    List<Element> childNodes = MxmlUtils.getChildElements(componentNode);
    for (Element element : childNodes) {
      String elementName = element.getLocalName();
      if (componentNode.getNamespaceURI().equals(element.getNamespaceURI()) && "mixins".equals(elementName)) {
        for (Element mixinElement : MxmlUtils.getChildElements(element)) {
          fillModelAttributes(jsonObject, mixinElement);
        }
        continue;
      }

      boolean isConfigTypeArray = isConfigTypeArray(configClass, elementName);
      String configMode = isConfigTypeArray ? element.getAttributeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME) : "";
      // Special case: if an EXML element representing a config property has attributes, it is treated as
      // having an untyped object value. Exception: it is an Array-typed property and the sole attribute is "mode".
      int attributeCount = element.getAttributes().getLength();
      if (attributeCount > 1 || attributeCount == 1 && configMode.length() == 0) {
        // it's an untyped complex object with attributes and sub-properties
        parseJavaScriptObjectProperty(jsonObject, element);
      } else {
        // derive the corresponding "at" value from the specified config mode (if any):
        Code atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
        if (atValue != null) {
          isConfigTypeArray = true;
          if (!isMxmlConfigClass(configClass)) {
            throw new CompilerError("Non-MXML class " + configClass.getQName() +
                    " does not support config modes: " + getLineNumber(element));
          }
        }
        String directTextContent = getDirectTextContent(element);
        if (directTextContent != null) {
          MemberModel configAttribute = findPropertyModel(configClass.getClassModel(), elementName);
          final Object attributeValue = getAttributeValue(directTextContent, configAttribute == null ? null : configAttribute.getType());
          jsonObject.set(elementName, attributeValue);
        } else {
          // it seems to be an array or an object
          fillJsonObjectProperty(jsonObject, elementName, isConfigTypeArray, MxmlUtils.getChildElements(element));
          ifContainerDefaultsThenExtractXtype(jsonObject, configClass, elementName);
        }

        // if any "at" value is specified, set the extra mode attribute (...$at):
        if (atValue != null) {
          jsonObject.set(elementName + CONFIG_MODE_AT_SUFFIX, atValue);
        }
        // empty properties are omitted if the type is not fixed to Array
      }
    }
  }

  private boolean isMxmlConfigClass(CompilationUnitModel configClass) {
    // heuristic: config classes that support property "mode" have an [ExtConfig] annotation with an empty [x|p|gc|]type
    ExtConfigAnnotation extConfigAnnotation = getExtConfigAnnotation(configClass);
    return extConfigAnnotation != null &&
            (extConfigAnnotation.typeKey == null || configClass.getQName().equals(extConfigAnnotation.typeValue));
  }

  private ExtConfigAnnotation getExtConfigAnnotation(CompilationUnitModel configClass) {
    Iterator<AnnotationModel> annotationModelIterator = configClass.getClassModel().getAnnotations(EXT_CONFIG_META_NAME).iterator();
    if (annotationModelIterator.hasNext()) {
      AnnotationModel annotationModel = annotationModelIterator.next();
      ExtConfigAnnotation result = new ExtConfigAnnotation();
      for (AnnotationPropertyModel property : annotationModel.getProperties()) {
        String propertyName = property.getName();
        if (TARGET_ANNOTATION_PARAMETER_NAME.equals(propertyName)) {
          result.target = property.getStringValue();
        } else {
          result.typeKey = propertyName;
          result.typeValue = property.getStringValue();
          if (result.typeValue == null) {
            result.typeValue = configClass.getQName();
          }
        }
      }
      return result;
    }
    return null;
  }

  private static String getDirectTextContent(Element element) {
    final NodeList innerChildNodes = element.getChildNodes();
    for (int i = 0; i < innerChildNodes.getLength(); i++) {
      final Node innerChildNode = innerChildNodes.item(i);
      if (innerChildNode.getNodeType() == Node.TEXT_NODE) {
        String directTextContent = ((Text) innerChildNode).getWholeText();
        if (directTextContent.trim().length() > 0) {
          return directTextContent;
        }
      }
    }
    return null;
  }

  private void ifContainerDefaultsThenExtractXtype(JsonObject jsonObject, CompilationUnitModel configClass, String elementName) throws IOException {
    // special case: extract xtype from <defaults> as <defaultType>!
    if (EXT_CONTAINER_DEFAULTS_PROPERTY.equals(elementName) && isContainerConfig(configClass)) {
      Object value = jsonObject.get(elementName);
      if (value instanceof JsonObject) {
        JsonObject jsonObjectValue = (JsonObject) value;
        // there are two ways an xtype can be specified:
        // a) as an EXML config class wrapping the JSON object:
        String xtype = jsonObjectValue.getWrapperClass();
        if (xtype != null) {
          jsonObjectValue.settingWrapperClass(null);
        } else {
          // b) as an "xtype" attribute:
          xtype = (String) jsonObjectValue.remove("xtype");
        }
        if (xtype != null) {
          jsonObject.set(EXT_CONTAINER_DEFAULT_TYPE_PROPERTY, xtype);
        }
      }
    }
  }

  private boolean isContainerConfig(CompilationUnitModel configClass) throws IOException {
    return configClass != null &&
            (EXT_CONTAINER_CONFIG_QNAME.equals(configClass.getQName()) ||
                    isContainerConfig(getCompilationUnitModel(configClass.getClassModel().getSuperclass())));
  }

  private boolean isConfigTypeArray(CompilationUnitModel configClass, String propertyName) throws IOException {
    MemberModel configAttribute = findPropertyModel(configClass.getClassModel(), propertyName);
    return configAttribute != null && "Array".equals(configAttribute.getType());
  }

  private void fillJsonObjectProperty(JsonObject jsonObject, String propertyName, boolean configTypeArray, List<Element> childElements) throws IOException {
    Object value = parseValue(configTypeArray, childElements);
    jsonObject.set(propertyName, value);
  }

  private Object parseValue(boolean configTypeArray, List<Element> childElements) throws IOException {
    Object value;
    List<Object> childObjects = parseChildObjects(childElements);
    if (childObjects.size() > 1 || configTypeArray) {
      // TODO: Check for type violation
      // We must write an array.
      value = new JsonArray(childObjects.toArray());
    } else if (childObjects.size() == 1) {
      // The property is either unspecified, untyped, or object-typed
      // and it contains a single child element. Use that element as the
      // property value.
      value = childObjects.get(0);
    } else {
      value = null;
    }
    return value;
  }

  private void parseJavaScriptObjectProperty(JsonObject jsonObject, Element propertyElement) {
    JsonObject propertyObject = new JsonObject();

    setUntypedAttributes(propertyElement, propertyObject);
    for (Element child : MxmlUtils.getChildElements(propertyElement)) {
      parseJavaScriptObjectProperty(propertyObject, child);
    }

    jsonObject.set(propertyElement.getLocalName(), propertyObject);
  }

  private List<Object> parseChildObjects(List<Element> elements) throws IOException {
    List<Object> childObjects = new ArrayList<Object>();
    for (Element arrayItemNode : elements) {
      String configClassName = createClassNameFromNode(arrayItemNode);
      if (configClassName == null) {
        throw new CompilerError(String.format("Cannot derive class name from <%s:%s>.", arrayItemNode.getNamespaceURI(), arrayItemNode.getLocalName()));
      }
      Object value;
      if ("Object".equals(configClassName) || "net.jangaroo.ext.object".equals(configClassName)) {
        value = parseExmlObjectNode(arrayItemNode);
      } else {
        CompilationUnitModel configClass = getCompilationUnitModel(configClassName);

        JsonObject arrayItemJsonObject = new JsonObject();
        ExtConfigAnnotation extConfigAnnotation = getExtConfigAnnotation(configClass);
        if (extConfigAnnotation != null) {
          if (extConfigAnnotation.typeKey != null && EXT_CONFIG_PACKAGE.equals(configClass.getPackage())) {
            // Ext classes are always loaded. We can use the type string directly.
            arrayItemJsonObject.set(extConfigAnnotation.typeKey, extConfigAnnotation.typeValue);
          } else if (extConfigAnnotation.typeKey != null && !"gctype".equals(extConfigAnnotation.typeKey)) {
            arrayItemJsonObject.settingWrapperClass(configClass.getQName());
            compilationUnitModel.addImport(configClass.getQName());
            compilationUnitModel.addImport(extConfigAnnotation.target);
          } else {
            // Everything not a component, plugin or layout must be created immediately
            // by using net.jangaroo.ext.create() with its configClass and the config:
            arrayItemJsonObject.settingConfigClass(configClass.getQName());
            compilationUnitModel.addImport(configClass.getQName());
            compilationUnitModel.addImport(extConfigAnnotation.target);
            compilationUnitModel.addImport(JsonObject.NET_JANGAROO_EXT_CREATE);
          }
        }

        fillModelAttributes(arrayItemJsonObject, arrayItemNode);
        value = arrayItemJsonObject;
      }
      if (value != null) {
        childObjects.add(value);
      }
    }
    return childObjects;
  }

  private Object parseExmlObjectNode(Node exmlObjectNode) {
    String textContent = exmlObjectNode.getTextContent();
    if (textContent != null && textContent.length() > 0) {
      String trimmedText = textContent.trim();
      if (MxmlUtils.isBindingExpression(trimmedText)) {
        return JsonObject.code(MxmlUtils.getBindingExpression(trimmedText));
      }
      return textContent; 
    } else {
      if (!exmlObjectNode.hasAttributes()) {
        return JsonObject.code("{}"); // an empty Object
      }
      JsonObject object = new JsonObject();
      setUntypedAttributes(exmlObjectNode, object);
      return object;
    }
  }

  private void setUntypedAttributes(Node exmlObjectNode, JsonObject object) {
    NamedNodeMap attributes = exmlObjectNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String attributeName = attribute.getLocalName();
      String attributeValue = attribute.getValue();
      object.set(attributeName, getAttributeValue(attributeValue, null));
    }
  }

  private Element createFields(Element objectNode) throws IOException {
    ClassModel classModel = compilationUnitModel.getClassModel();
    Element superConfigElement = null;
    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_DECLARATIONS.equals(elementName)) {
          for (Element declaration : MxmlUtils.getChildElements(element)) {
            createValue(declaration);
          }
          continue;
        } else if (MXML_SCRIPT.equals(elementName)) {
          String scriptCode = getTextContent(element);
          // parse private vars and constructor params from script code, so that MXML elements with id
          // can be assigned to these existing variables instead of defining new fields:
          if (configParamModel == null) {
            // remove "native" constructor modifier and add body calling super(config):
            Pattern nativeConstructorPattern = Pattern.compile("(\\s*public)\\s+native(\\s+function\\s+" + classModel.getName() + "\\s*\\(\\s*([A-Za-z_][A-Za-z_0-9]*)\\s*:\\s*([A-Za-z_][A-Za-z_0-9.]*)\\s*=\\s*null\\s*\\)\\s*);");
            Matcher nativeConstructorMatcher = nativeConstructorPattern.matcher(scriptCode);
            scriptCode = nativeConstructorMatcher.replaceFirst(String.format("$1$2 {%n      super($3);    }"));
            Pattern constructorPattern = Pattern.compile("\\s*public\\s+function\\s+" + classModel.getName() + "\\s*\\(\\s*([A-Za-z_][A-Za-z_0-9]*)\\s*:\\s*([A-Za-z_][A-Za-z_0-9.]*)\\s*=\\s*null\\s*\\)\\s*\\{");
            Matcher constructorMatcher = constructorPattern.matcher(scriptCode);
            if (constructorMatcher.find()) {
              configParamModel = new ParamModel(
                      constructorMatcher.group(1),
                      constructorMatcher.group(2),
                      "null");
            }
          }

          Pattern exmlVarPattern = Pattern.compile("private\\s+var\\s+([A-Za-z_][A-Za-z_0-9]*)");
          Matcher matcher = exmlVarPattern.matcher(scriptCode);
          while (matcher.find()) {
            privateVars.put(matcher.group(1), null);
          }

          classModel.addBodyCode(scriptCode);
          continue;
        } else if (MXML_METADATA.equals(elementName)) {
          classModel.addAnnotationCode(getTextContent(element));
          continue;
        }
        // else it must be a top-level type, treat like any other element.
      }
      String elementClassName = createClassNameFromNode(element);
      String id = element.getAttribute(MXML_ID_ATTRIBUTE);
      if (id.isEmpty()) {
        superConfigElement = element;
      } else {
        compilationUnitModel.addImport(elementClassName);
        if (configParamModel != null && configParamModel.getName().equals(id)) {
          // TODO: check consistency of configParamMode.getType() and elementClassName!
          cfgDefaults = new JsonObject();
          fillModelAttributes(cfgDefaults, element);
        } else {
          Json valueJsonObject = createValue(element);
          if (privateVars.containsKey(id)) {
            privateVars.put(id, valueJsonObject);
          } else {
            classModel.addMember(new FieldModel(id, elementClassName, valueJsonObject.toString(2, 4)));
          }
        }
      }
    }
    return superConfigElement;
  }

  // ======================================== auxiliary methods ========================================

  private Json createValue(Element element) throws IOException {
    String className = createClassNameFromNode(element);
    if ("Array".equals(className)) {
      List<Object> childObjects = parseChildObjects(MxmlUtils.getChildElements(element));
      return new JsonArray(childObjects.toArray());
    }
    JsonObject value = new JsonObject();
    fillModelAttributes(value, element);
    return value;
  }

  private CompilationUnitModel getCompilationUnitModel(String fullClassName) throws IOException {
    if (fullClassName == null) {
      return null;
    }
    CompilationUnit compilationUnit = jangarooParser.getCompilationUnit(fullClassName);
    if (compilationUnit == null) {
      throw Jooc.error("Undefined type: " + fullClassName);
    }
    return new ApiModelGenerator(false).generateModel(compilationUnit); // TODO: cache!
  }

  private CompilationUnitModel getCompilationUnitModel(Element element) throws IOException {
    return getCompilationUnitModel(createClassNameFromNode(element));
  }

  private MemberModel findPropertyModel(ClassModel classModel, String propertyName) throws IOException {
    MemberModel propertyModel = null;
    ClassModel superClassModel = getSuperClassModel(classModel);
    if (superClassModel != null) {
      propertyModel = findPropertyModel(superClassModel, propertyName);
    }
    if (propertyModel == null) {
      MemberModel memberModel = classModel.getMember(propertyName);
      if (memberModel != null && memberModel.isWritable()) {
        propertyModel = memberModel;
      }
    }
    return propertyModel;
  }

  private ClassModel getSuperClassModel(ClassModel classModel) throws IOException {
    String superclass = classModel.getSuperclass();
    if (superclass != null) {
      CompilationUnitModel superCompilationUnitModel = getCompilationUnitModel(superclass);
      if (superCompilationUnitModel != null && superCompilationUnitModel.getPrimaryDeclaration() instanceof ClassModel) {
        return superCompilationUnitModel.getClassModel();
      }
    }
    return null;
  }

  private String createClassNameFromNode(Node objectNode) {
    String name = objectNode.getLocalName();
    String uri = objectNode.getNamespaceURI();
    if (uri != null) {
      if (Exmlc.EXML_NAMESPACE_URI.equals(uri) && Exmlc.EXML_OBJECT_NODE_NAME.equals(name)) {
        return "Object";
      }
      String packageName = MxmlUtils.parsePackageFromNamespace(uri);
      if (packageName != null) {
        return CompilerUtils.qName(packageName, name);
      }
    }
    return null;
  }

  private static String getTextContent(Element element) {
    return element.getChildNodes().getLength() == 1 && element.getFirstChild().getNodeType() == Node.TEXT_NODE ? ((Text) element.getFirstChild()).getData() : "";
  }

  private Document buildDom(InputStream inputStream) throws SAXException, IOException {
    SAXParser parser;
    final Document doc;
    try {
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      parser = saxFactory.newSAXParser();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      doc = factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }

  private static class ExtConfigAnnotation {
    String target;
    String typeKey;
    String typeValue;
  }
}
