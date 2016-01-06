package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.jooc.json.Code;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.util.PreserveLineNumberHandler;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.ApplyExpr;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.jangaroo.jooc.util.PreserveLineNumberHandler.getLineNumber;

public final class ExmlToModelParser {
  private static final String EXT_CONFIG_PACKAGE = "ext.config";
  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final String EXT_CONTAINER_CONFIG_QNAME = "ext.config.container";
  private static final String EXT_CONTAINER_DEFAULTS_PROPERTY = "defaults";
  private static final String EXT_CONTAINER_DEFAULT_TYPE_PROPERTY = "defaultType";

  private final ConfigClassRegistry registry;

  public ExmlToModelParser(ConfigClassRegistry registry) {
    this.registry = registry;
  }

  /**
   * Parses the exml file into an ExmlModel
   * @param file the file to parse
   * @return the parsed model
   * @throws IOException if the input stream could not be read
   * @throws SAXException if the XML was not well-formed
   */
  public ExmlModel parse(File file) throws IOException, SAXException {
    ExmlModel model = new ExmlModel();
    String qName = CompilerUtils.qNameFromFile(registry.getConfig().findSourceDir(file), file);
    String className = CompilerUtils.className(qName);
    model.setClassName(ExmlUtils.createComponentClassName(className));
    ConfigClass configClassByName = registry.getConfigClassByName(registry.getConfig().getConfigClassPackage() + "." + ConfigClass.createConfigClassName(className));
    model.setConfigClass(configClassByName);
    model.setPackageName(CompilerUtils.packageName(qName));

    BufferedInputStream inputStream = null;
    try {
      inputStream = new BufferedInputStream(new FileInputStream(file));
      parse(inputStream, model);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return model;
  }

  /**
   * Parse the input stream content into a model.
   * Close the input stream after reading.
   *
   * @param inputStream the input stream
   * @param model the model
   * @throws IOException  if the input stream could not be read
   * @throws SAXException if the XML was not well-formed
   */
  private void parse(InputStream inputStream, ExmlModel model) throws IOException, SAXException {
    Document document = buildDom(inputStream);
    Node root = document.getFirstChild();
    validateRootNode(root);

    NamedNodeMap attributes = root.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      //baseClass attribute has been specified, so the super class of the component is actually that
      if (Exmlc.EXML_BASE_CLASS_ATTRIBUTE.equals(attribute.getLocalName())) {
        model.setSuperClassName(attribute.getValue());
      } else if (Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(attribute.getLocalName())) {
        PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(attribute.getValue());
        switch (publicApiMode) {
          case TRUE:   model.addAnnotation(Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
                       // fall through!
          case CONFIG: model.getConfigClass().addAnnotation(Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
        }
      }
    }

    NodeList childNodes = root.getChildNodes();
    Element componentNode = null;
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        if (ExmlUtils.isExmlNamespace(node.getNamespaceURI())) {
          Element element = (Element)node;
          if (Exmlc.EXML_IMPORT_NODE_NAME.equals(node.getLocalName())) {
            String importedClassName = element.getAttribute(Exmlc.EXML_IMPORT_CLASS_ATTRIBUTE);
            if (importedClassName == null || importedClassName.equals("")) {
              int lineNumber = getLineNumber(componentNode);
              throw new ExmlcException("<exml:import> element must contain a non-empty class attribute", lineNumber);
            }
            model.addImport(importedClassName);
          } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(node.getLocalName())) {
            AnnotationAt annotationAt = Exmlc.parseAnnotationAtValue(element.getAttribute(Exmlc.EXML_ANNOTATION_AT_ATTRIBUTE));
            if (annotationAt != AnnotationAt.CONFIG) {
              model.addAnnotation(element.getTextContent());
            }
          } else if (Exmlc.EXML_CONSTANT_NODE_NAME.equals(node.getLocalName())) {
            String constantTypeName = element.getAttribute(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE);
            model.addImport(constantTypeName);
          } else if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(node.getLocalName())) {
            model.setDescription(node.getTextContent());
          } else if (Exmlc.EXML_VAR_NODE_NAME.equals(node.getLocalName())) {
            Declaration var = createDeclaration(element, model);
            if (!model.getVars().contains(var)) {
              model.addVar(var);
            }
          } else if (Exmlc.EXML_CFG_NODE_NAME.equals(node.getLocalName())) {
            String cfgName = element.getAttribute(Exmlc.EXML_CFG_NAME_ATTRIBUTE);
            String cfgType = element.getAttribute(Exmlc.EXML_CFG_TYPE_ATTRIBUTE);
            String cfgDefault = element.getAttribute(Exmlc.EXML_CFG_DEFAULT_ATTRIBUTE);
            Element defaultValueElement = findChildElement(element,
                    Exmlc.EXML_NAMESPACE_URI, Exmlc.EXML_CFG_DEFAULT_NODE_NAME);
            if (cfgDefault.length() != 0 && defaultValueElement != null) {
              throw new ExmlcException("<exml:cfg> default value must be specified as either an attribute or a sub-element, not both for config '" + cfgName + "'.", getLineNumber(element));
            }
            if (cfgDefault.length() > 0) {
              model.getCfgDefaults().set(cfgName, getAttributeValue(cfgDefault, cfgType));
              model.addImport(cfgType);
            } else if (defaultValueElement != null) {
              model.getCfgDefaults().set(cfgName, parseValue(model, "Array".equals(cfgType), getChildElements(defaultValueElement)));
            }
          }
        } else {
          if (componentNode != null) {
            int lineNumber = getLineNumber(componentNode);
            throw new ExmlcException("root node of EXML contained more than one component definition", lineNumber);
          }
          componentNode = (Element)node;
        }
      }
    }
    if (componentNode == null) {
      return;
    }

    String superFullClassName = createFullConfigClassNameFromNode(componentNode);
    if (superFullClassName.equals(model.getConfigClass().getFullName())) {
      int lineNumber = getLineNumber(componentNode);
      throw  new ExmlcException("Cyclic inheritance error: super class and this component are the same!. There is something wrong!", lineNumber);
    }
    ConfigClass superConfigClass = getConfigClassByName(superFullClassName, componentNode);
    String superComponentClassName = superConfigClass.getComponentClassName();
    if (model.getSuperClassName() == null) {
      model.setSuperClassName(superComponentClassName);
    }
    //but we still need the import
    model.addImport(superComponentClassName);

    fillModelAttributes(model, model.getJsonObject(), componentNode, superConfigClass);
  }

  private Declaration createDeclaration(Element element, ExmlModel model) {
    final String name = element.getAttribute(Exmlc.EXML_DECLARATION_NAME_ATTRIBUTE);
    String type = element.getAttribute(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE);
    if (type == null || type.isEmpty()) {
      type = AS3Type.ANY.toString();
    }
    final NodeList valueElements = element.getElementsByTagNameNS(Exmlc.EXML_NAMESPACE_URI, Exmlc.EXML_DECLARATION_VALUE_NODE_NAME);
    final Object valueObject;
    boolean addTypeCast = false;
    if (valueElements.getLength() > 0) {
      if (element.hasAttribute(Exmlc.EXML_DECLARATION_VALUE_ATTRIBUTE)) {
        throw new ExmlcException("The value of <exml:var> or <exml:const> '\" + name + \"' must be specified as either an attribute or a sub-element, not both.", getLineNumber(element));
      }
      Element valueElement = (Element)valueElements.item(0);
      final List<Element> valueChildElements = getChildElements(valueElement);
      if (valueChildElements.isEmpty()) {
        valueObject = getAttributeValue(valueElement.getTextContent(), type);
      } else {
        valueObject = parseValue(model, "Array".equals(type), valueChildElements);
        addTypeCast = !AS3Type.ANY.toString().equals(type) && !ApplyExpr.isCoerceFunction(type);
      }
    } else {
      valueObject = getAttributeValue(element.getAttribute(Exmlc.EXML_DECLARATION_VALUE_ATTRIBUTE), type);
    }
    String value = JsonObject.valueToString(valueObject, 2, 4);
    if (addTypeCast) {
      value = type + "(" + value + ")";
    }
    return new Declaration(name, value, type);
  }

  private String createFullConfigClassNameFromNode(Node componentNode) {
    String name = componentNode.getLocalName();
    String uri = componentNode.getNamespaceURI();
    String packageName = uri == null ? null : ExmlUtils.parsePackageFromNamespace(uri);
    if (packageName == null) {
      int lineNumber = getLineNumber(componentNode);
      throw new ExmlcException("namespace '" + uri + "' of element '" + name + "' in EXML file does not denote a config package", lineNumber);
    }
    return packageName + "." + name;
  }

  private void fillModelAttributes(ExmlModel model, JsonObject jsonObject, Element componentNode,
                                   ConfigClass configClass) {
    NamedNodeMap attributes = componentNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String attributeName = attribute.getLocalName();
      String attributeValue = attribute.getValue();
      ConfigAttribute configAttribute = getCfgByName(configClass, attributeName);
      jsonObject.set(attributeName, getAttributeValue(attributeValue, configAttribute == null ? null : configAttribute.getType()));
    }
    fillModelAttributesFromSubElements(model, jsonObject, componentNode, configClass);
  }

  public static Object getAttributeValue(String attributeValue, String type) {
    if (ExmlUtils.isCodeExpression(attributeValue)) {
      return JsonObject.code(ExmlUtils.getCodeExpression(CompilerUtils.denormalizeAttributeValue(attributeValue)));
    } else {
      AS3Type as3Type = type == null ? AS3Type.ANY : AS3Type.typeByName(type);
      if (AS3Type.ANY.equals(as3Type)) {
        as3Type = CompilerUtils.guessType(attributeValue);
      }
      if (as3Type != null) {
        switch (as3Type) {
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

  private void fillModelAttributesFromSubElements(ExmlModel model, JsonObject jsonObject, Element componentNode, ConfigClass configClass) {
    List<Element> childNodes = getChildElements(componentNode);
    for (Element element : childNodes) {
      String elementName = element.getLocalName();
      if (ExmlUtils.isExmlNamespace(element.getNamespaceURI()) && "mixins".equals(elementName)) {
        for (Element mixinElement : getChildElements(element)) {
          String mixinClassName = createFullConfigClassNameFromNode(mixinElement);
          ConfigClass mixinConfigClass = getConfigClassByName(mixinClassName, mixinElement);
          fillModelAttributes(model, jsonObject, mixinElement, mixinConfigClass);
        }
        continue;
      }

      boolean isConfigTypeArray = isConfigTypeArray(configClass, elementName);
      String configMode = isConfigTypeArray ? element.getAttribute(CONFIG_MODE_ATTRIBUTE_NAME) : "";
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
          if (!configClass.isExmlGenerated()) {
            throw new ExmlcException("Non-EXML class " + configClass.getComponentClassName() +
                    " does not support config modes.", getLineNumber(element));
          }
        }
        String directTextContent = getDirectTextContent(element);
        if (directTextContent != null) {
          ConfigAttribute configAttribute = getCfgByName(configClass, elementName);
          final Object attributeValue = getAttributeValue(directTextContent, configAttribute == null ? null : configAttribute.getType());
          jsonObject.set(elementName, attributeValue);
        } else {
          // it seems to be an array or an object
          fillJsonObjectProperty(model, jsonObject, elementName, isConfigTypeArray, getChildElements(element));
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

  private void ifContainerDefaultsThenExtractXtype(JsonObject jsonObject, ConfigClass configClass, String elementName) {
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
          xtype = (String) jsonObjectValue.remove(ConfigClassType.COMPONENT.getType());
        }
        if (xtype != null) {
          jsonObject.set(EXT_CONTAINER_DEFAULT_TYPE_PROPERTY, xtype);
        }
      }
    }
  }

  private boolean isContainerConfig(ConfigClass configClass) {
    return configClass != null &&
            (EXT_CONTAINER_CONFIG_QNAME.equals(configClass.getFullName()) ||
                    isContainerConfig(configClass.getSuperClass()));
  }

  private boolean isConfigTypeArray(ConfigClass configClass, String propertyName) {
    ConfigAttribute configAttribute = getCfgByName(configClass, propertyName);
    return configAttribute != null && "Array".equals(configAttribute.getType());
  }

  private void fillJsonObjectProperty(ExmlModel model, JsonObject jsonObject, String propertyName, boolean configTypeArray, List<Element> childElements) {
    Object value = parseValue(model, configTypeArray, childElements);
    jsonObject.set(propertyName, value);
  }

  private Object parseValue(ExmlModel model, boolean configTypeArray, List<Element> childElements) {
    Object value;
    List<Object> childObjects = parseChildObjects(model, childElements);
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

  private ConfigClass getConfigClassByName(String className, Node errorNode) {
    ConfigClass configClass = registry.getConfigClassByName(className);
    if (configClass == null) {
      int lineNumber = getLineNumber(errorNode);
      throw new ExmlcException("unknown type '" + className + "'", lineNumber);
    }
    return configClass;
  }

  private void parseJavaScriptObjectProperty(JsonObject jsonObject, Element propertyElement) {
    JsonObject propertyObject = new JsonObject();

    setUntypedAttributes(propertyElement, propertyObject);
    for (Element child : getChildElements(propertyElement)) {
      parseJavaScriptObjectProperty(propertyObject, child);
    }

    jsonObject.set(propertyElement.getLocalName(), propertyObject);
  }

  private static Element findChildElement(Element element, String namespace, String nodeName) {
    for (Element child : getChildElements(element)) {
      if (namespace.equals(child.getNamespaceURI()) && nodeName.equals(child.getLocalName())) {
        return child;
      }
    }
    return null;
  }

  private static List<Element> getChildElements(Element element) {
    List<Element> result = new ArrayList<Element>();
    NodeList propertyChildNotes = element.getChildNodes();
    for (int j = 0; j < propertyChildNotes.getLength(); j++) {
      Node childNode = propertyChildNotes.item(j);
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        result.add((Element) childNode);
      }
    }
    return result;
  }

  private ConfigAttribute getCfgByName(ConfigClass configClass, String attributeName) {
    ConfigClass current = configClass;
    while (current != null) {
      ConfigAttribute configAttribute = current.getCfgByName(attributeName);
      if (configAttribute != null) {
        return configAttribute;
      }
      String superClassName = current.getSuperClassName();
      if (superClassName == null || superClassName.equals("Object")) {
        break;
      }
      current = registry.getConfigClassByName(superClassName);
    }
    return null;
  }

  private List<Object> parseChildObjects(ExmlModel model, List<Element> elements) {
    List<Object> childObjects = new ArrayList<Object>();
    for (Element arrayItemNode : elements) {
      Object value;
      if (ExmlUtils.isExmlNamespace(arrayItemNode.getNamespaceURI()) && Exmlc.EXML_OBJECT_NODE_NAME.equals(arrayItemNode.getLocalName())) {
        value = parseExmlObjectNode(arrayItemNode);
      } else {
        String arrayItemClassName = createFullConfigClassNameFromNode(arrayItemNode);
        ConfigClass configClass = getConfigClassByName(arrayItemClassName, arrayItemNode);

        JsonObject arrayItemJsonObject = new JsonObject();
        if (configClass.getType().getExtTypeAttribute() != null && EXT_CONFIG_PACKAGE.equals(configClass.getPackageName())) {
          // Ext classes are always loaded. We can use the type string directly.
          arrayItemJsonObject.set(configClass.getType().getExtTypeAttribute(), configClass.getTypeValue());
        } else if (configClass.getType().isCreatedViaConfigObject()) {
          arrayItemJsonObject.settingWrapperClass(configClass.getFullName());
          model.addImport(configClass.getFullName());
          model.addImport(configClass.getComponentClassName());
        } else {
          // Everything not a component, plugin or layout must be created immediately
          // by using net.jangaroo.ext.create() with its configClass and the config:
          arrayItemJsonObject.settingConfigClass(configClass.getFullName());
          model.addImport(configClass.getFullName());
          model.addImport(configClass.getComponentClassName());
          model.addImport(JsonObject.NET_JANGAROO_EXT_CREATE);
        }

        fillModelAttributes(model, arrayItemJsonObject, arrayItemNode, configClass);
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
      return JsonObject.code(textContent.trim());
    } else {
      if (!exmlObjectNode.hasAttributes()) {
        return null;
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

  private void validateRootNode(Node root) {
    int lineNumber = getLineNumber(root);
    if (!ExmlUtils.isExmlNamespace(root.getNamespaceURI())) {
      throw new ExmlcException("root node of EXML file must belong to namespace '" + Exmlc.EXML_NAMESPACE_URI + "', but was '" + root.getNamespaceURI() + "'", lineNumber);
    }
    if (!Exmlc.EXML_ROOT_NODE_NAMES.contains(root.getLocalName())) {
      throw new ExmlcException("Root node of EXML file must be one of " + Arrays.toString(Exmlc.EXML_ROOT_NODE_NAMES.toArray()) + ", but was " + root.getLocalName() + ".", lineNumber);
    }
  }

  private Document buildDom(InputStream inputStream) throws SAXException, IOException {
    SAXParser parser;
    final Document doc;
    try {
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      parser = saxFactory.newSAXParser();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      doc = factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }
}
