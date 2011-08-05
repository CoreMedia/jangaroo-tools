package net.jangaroo.exml.parser;

import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.ExmlcException;
import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.xml.PreserveLineNumberHandler;
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
import java.util.List;

public final class ExmlToModelParser {
  private static final String EXT_CONFIG_PREFIX = "ext.config.";
  private static final String LAYOUT_SUFFIX = "layout";

  private final ConfigClassRegistry registry;

  public ExmlToModelParser(ConfigClassRegistry registry) {
    this.registry = registry;
  }

  public ExmlModel parse(File file) throws IOException, SAXException {
    ExmlModel model = parse(new BufferedInputStream(new FileInputStream(file)));
    String qName = CompilerUtils.qNameFromFile(registry.getConfig().findSourceDir(file), file);
    model.setClassName(CompilerUtils.className(qName));
    model.setPackageName(CompilerUtils.packageName(qName));
    return model;
  }

  /**
   * Parse the input stream content into a model.
   * Close the input stream after reading.
   * @param inputStream the input stream
   * @return the model
   * @throws IOException if the input stream could not be read
   * @throws SAXException if the XML was not well-formed
   */
  public ExmlModel parse(InputStream inputStream) throws IOException, SAXException {
    ExmlModel model = new ExmlModel();
    Document document = buildDom(inputStream);
    Node root = document.getFirstChild();
    validateRootNode(root);

    NodeList childNodes = root.getChildNodes();
    Node componentNode = null;
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        if (ExmlConstants.EXML_NAMESPACE_URI.equals(node.getNamespaceURI())) {
          if (ExmlConstants.EXML_IMPORT_NODE_NAME.equals(node.getLocalName())) {
            String importedClassName = ((Element) node).getAttribute(ExmlConstants.EXML_IMPORT_CLASS_ATTRIBUTE);
            if (importedClassName == null || importedClassName.equals("")) {
              int lineNumber = getLineNumber(componentNode);
              throw new ExmlcException("<exml:import> element must contain a non-empty class attribute", lineNumber);
            }
            model.addImport(importedClassName);
          } else if (ExmlConstants.EXML_DESCRIPTION_NODE_NAME.equals(node.getLocalName())) {
            model.setDescription(node.getTextContent().trim());
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
      int lineNumber = getLineNumber(root);
      throw new ExmlcException("root node of EXML did not contain a component definition", lineNumber);
    }

    String className = createFullConfigClassNameFromNode(componentNode);
    ConfigClass configClass = getConfigClassByName(className, componentNode);
    String componentClassName = configClass.getComponentClassName();
    model.setSuperClassName(componentClassName);
    model.addImport(componentClassName);

    fillModelAttributes(model, model.getJsonObject(), componentNode, configClass);

    return model;
  }

  private String createFullConfigClassNameFromNode(Node componentNode) {
    String name = componentNode.getLocalName();
    String uri = componentNode.getNamespaceURI();
    String packageName = ExmlConstants.parsePackageFromNamespace(uri);
    if (packageName == null) {
      int lineNumber = getLineNumber(componentNode);
      throw new ExmlcException("namespace '" + uri + "' of element '"+ name +"' in EXML file does not denote a config package", lineNumber);
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
      if (configAttribute == null) {
        setUntypedAttribute(jsonObject, attributeName, attributeValue);
      } else {
        String type = configAttribute.getType();
        if ("Boolean".equals(type)) {
          jsonObject.set(attributeName, Boolean.parseBoolean(attributeValue));
        } else if ("Number".equals(type)) {
          try {
            jsonObject.set(attributeName, Long.parseLong(attributeValue));
          } catch (NumberFormatException e) {
            jsonObject.set(attributeName, Double.parseDouble(attributeValue));
          }
        } else if ("String".equals(type)) {
          jsonObject.set(attributeName, attributeValue);
        } else if ("Array".equals(type)) {
          jsonObject.set(attributeName, new JsonArray(attributeValue));
        } else if ("*".equals(type)) {
          setUntypedAttribute(jsonObject, attributeName, attributeValue);
        } else { // Object or specific type. We don't care (for now).
          //TODO: Warning here?
          jsonObject.set(attributeName, attributeValue);
        }
      }
    }
    NodeList childNodes = componentNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        String elementName = element.getLocalName();
        if(element.hasAttributes()) {
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
        result.add((Element)childNode);
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
      if(ExmlConstants.EXML_NAMESPACE_URI.equals(arrayItemNode.getNamespaceURI()) && ExmlConstants.EXML_OBJECT_NODE_NAME.equals(arrayItemNode.getLocalName())) {
        value = parseExmlObjectNode(arrayItemNode);
      } else {
        String arrayItemClassName = createFullConfigClassNameFromNode(arrayItemNode);
        ConfigClass configClass = getConfigClassByName(arrayItemClassName, arrayItemNode);
        String componentClassName = configClass.getComponentClassName();

        JsonObject arrayItemJsonObject = new JsonObject();
        if (configClass.getType() == ConfigClassType.ACTION) {
          // Actions must be created immediately.
          arrayItemJsonObject.settingWrapperClass(componentClassName);
          model.addImport(componentClassName);
        } else {
          if (arrayItemClassName.startsWith(EXT_CONFIG_PREFIX)) {
            // Ext classes are always loaded. We can use the type string directly.
            String typeString = arrayItemClassName.substring(EXT_CONFIG_PREFIX.length());
            // By convention an optional "layout" suffix of layout types is cut off.
            if (configClass.getType() == ConfigClassType.LAYOUT && typeString.endsWith(LAYOUT_SUFFIX)) {
              typeString = typeString.substring(0, typeString.lastIndexOf(LAYOUT_SUFFIX));
            }

            arrayItemJsonObject.set(configClass.getType().getExtTypeAttribute(), typeString);
          } else {
            arrayItemJsonObject.settingWrapperClass(configClass.getFullName());
            model.addImport(configClass.getFullName());
          }
        }

        fillModelAttributes(model, arrayItemJsonObject, arrayItemNode, configClass);
        value = arrayItemJsonObject;
      }
      if(value != null) {
        childObjects.add(value);
      }
    }
    return childObjects;
  }

  private Object parseExmlObjectNode(Node exmlObjectNode) {
    String textContent = exmlObjectNode.getTextContent();
    if(textContent != null && textContent.length() > 0) {
      return "{" + textContent.trim() + "}";
    } else {
      if(!exmlObjectNode.hasAttributes()) {
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
      setUntypedAttribute(object, attributeName, attributeValue);
    }
  }


  private void setUntypedAttribute(JsonObject jsonObject, String attributeName, String attributeValue) {
    try {
      jsonObject.set(attributeName, Long.parseLong(attributeValue));
      return;
    } catch (NumberFormatException e) {
      // Try again.
    }
    try {
      jsonObject.set(attributeName, Double.parseDouble(attributeValue));
      return;
    } catch (NumberFormatException e) {
      // Try again.
    }
    if ("false".equalsIgnoreCase(attributeValue)) {
      jsonObject.set(attributeName, Boolean.FALSE);
      return;
    }
    if ("true".equalsIgnoreCase(attributeValue)) {
      jsonObject.set(attributeName, Boolean.TRUE);
      return;
    }
    jsonObject.set(attributeName, attributeValue);
  }

  private void validateRootNode(Node root) {
    int lineNumber = getLineNumber(root);
    if (!ExmlConstants.EXML_NAMESPACE_URI.equals(root.getNamespaceURI())) {
      throw new ExmlcException("root node of EXML file must belong to namespace '" + ExmlConstants.EXML_NAMESPACE_URI + "', but was '" + root.getNamespaceURI() +"'", lineNumber);
    }
    if (!ExmlConstants.EXML_COMPONENT_NODE_NAME.equals(root.getLocalName())) {
      throw new ExmlcException("root node of EXML file must be a <" + ExmlConstants.EXML_COMPONENT_NODE_NAME + ">, but was <" + root.getLocalName() +">", lineNumber);
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
      doc =  factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }
}
