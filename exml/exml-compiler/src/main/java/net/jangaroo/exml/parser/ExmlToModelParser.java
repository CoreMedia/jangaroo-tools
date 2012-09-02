package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.exml.xml.PreserveLineNumberHandler;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.List;

public final class ExmlToModelParser {
  private static final String EXT_CONFIG_PREFIX = "ext.config.";

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
          case TRUE:   model.addAnnotation("PublicApi");
                       // fall through!
          case CONFIG: model.getConfigClass().addAnnotation("PublicApi");
        }
      }
    }

    NodeList childNodes = root.getChildNodes();
    Node componentNode = null;
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
            Declaration var = new Declaration(element.getAttribute(Exmlc.EXML_DECLARATION_NAME_ATTRIBUTE),
              element.getAttribute(Exmlc.EXML_DECLARATION_VALUE_ATTRIBUTE),
              element.getAttribute(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE));
            if (!model.getVars().contains(var)) {
              model.addVar(var);
            }
          }
        } else {
          if (componentNode != null) {
            int lineNumber = getLineNumber(componentNode);
            throw new ExmlcException("root node of EXML contained more than one component definition", lineNumber);
          }
          componentNode = node;
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

  private String createFullConfigClassNameFromNode(Node componentNode) {
    String name = componentNode.getLocalName();
    String uri = componentNode.getNamespaceURI();
    String packageName = ExmlUtils.parsePackageFromNamespace(uri);
    if (packageName == null) {
      int lineNumber = getLineNumber(componentNode);
      throw new ExmlcException("namespace '" + uri + "' of element '" + name + "' in EXML file does not denote a config package", lineNumber);
    }
    return packageName + "." + name;
  }

  private void fillModelAttributes(ExmlModel model, JsonObject jsonObject, Node componentNode, ConfigClass configClass) {
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
    if (!ExmlUtils.isCodeExpression(attributeValue)) {
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

  private void fillModelAttributesFromSubElements(ExmlModel model, JsonObject jsonObject, Node componentNode, ConfigClass configClass) {
    NodeList childNodes = componentNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        String elementName = element.getLocalName();
        if (element.hasAttributes()) {
          // it's a complex object with attributes and sub-properties
          parseJavaScriptObjectProperty(jsonObject, element);
        } else {
          // it seems to be an array or an object
          List<Object> childObjects = parseChildObjects(model, element);
          ConfigAttribute configAttribute = getCfgByName(configClass, elementName);
          if (childObjects.size() > 1 || configAttribute != null && "Array".equals(configAttribute.getType())) {
            // TODO: Check for type violation
            // We must write an array.
            JsonArray jsonArray = new JsonArray(childObjects.toArray());
            jsonObject.set(element.getLocalName(), jsonArray);
          } else if (childObjects.size() == 1) {
            // The property is either unspecified, untyped, or object-typed
            // and it contains a single child element. Use that element as the
            // property value.
            jsonObject.set(element.getLocalName(), childObjects.get(0));
          }
          // empty properties are omitted if the type is not fixed to Array
        }
      }
    }
  }

  private ConfigClass getConfigClassByName(String className, Node errorNode) {
    ConfigClass configClass = registry.getConfigClassByName(className);
    if (configClass == null) {
      // maybe it is a target class?
      configClass = registry.getConfigClassOfTargetClass(className);
      }
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

  private List<Element> getChildElements(Element element) {
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

  private List<Object> parseChildObjects(ExmlModel model, Element element) {
    List<Object> childObjects = new ArrayList<Object>();
    for (Element arrayItemNode : getChildElements(element)) {
      Object value;
      if (ExmlUtils.isExmlNamespace(arrayItemNode.getNamespaceURI()) && Exmlc.EXML_OBJECT_NODE_NAME.equals(arrayItemNode.getLocalName())) {
        value = parseExmlObjectNode(arrayItemNode);
      } else {
        String arrayItemClassName = createFullConfigClassNameFromNode(arrayItemNode);
        ConfigClass configClass = getConfigClassByName(arrayItemClassName, arrayItemNode);

        JsonObject arrayItemJsonObject = new JsonObject();
        if (configClass.getType() == null) {
          // Everything not a component, plugin or layout must be created immediately
          // by using net.jangaroo.ext.create() with its configClass and the config:
          arrayItemJsonObject.settingConfigClass(configClass.getFullName());
          model.addImport(configClass.getFullName());
          model.addImport(configClass.getComponentClassName());
          model.addImport(JsonObject.NET_JANGAROO_EXT_CREATE);
        } else {
          if (arrayItemClassName.startsWith(EXT_CONFIG_PREFIX)) {
            // Ext classes are always loaded. We can use the type string directly.
            arrayItemJsonObject.set(configClass.getType().getExtTypeAttribute(), configClass.getTypeValue());
          } else {
            arrayItemJsonObject.settingWrapperClass(configClass.getFullName());
            model.addImport(configClass.getFullName());
            model.addImport(configClass.getComponentClassName());
          }
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
      return "{" + textContent.trim() + "}";
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

  private int getLineNumber(Node node) {
    return Integer.parseInt((String) node.getUserData(PreserveLineNumberHandler.LINE_NUMBER_KEY_NAME));
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
