package net.jangaroo.exml.xml;

import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.ExmlParseException;
import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ExmlModel;
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
import java.io.IOException;
import java.io.InputStream;

public final class ExmlParser {

  private final ConfigClassRegistry registry;

  ExmlModel model;

  public ExmlParser(ConfigClassRegistry registry) {
    this.registry = registry;
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
    model = new ExmlModel();
    Document document = buildDom(inputStream);
    Node root = document.getFirstChild();
    validateRootNode(root);

    NodeList childNodes = root.getChildNodes();
    Node componentNode = null;
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE && !ExmlConstants.EXML_NAMESPACE_URI.equals(node.getNamespaceURI())) {
        if (componentNode != null) {
          int lineNumber = getLineNumber(componentNode);
          throw new ExmlParseException("root node of EXML contained more than one component definition", lineNumber);
        }
        componentNode = node;
      }
    }
    if (componentNode == null) {
      int lineNumber = getLineNumber(root);
      throw new ExmlParseException("root node of EXML did not contain a component definition", lineNumber);
    }

    String className = createFullClassNameFromNode(componentNode);
    model.setParentClassName(className);
    model.addImport(className);
    fillModelAttributes(model.getJsonObject(), componentNode, className);

    return model;
  }

  private String createFullClassNameFromNode(Node componentNode) {
    String name = componentNode.getLocalName();
    String uri = componentNode.getNamespaceURI();
    String packageName = ExmlConstants.parsePackageFromNamespace(uri);
    if (packageName == null) {
      int lineNumber = getLineNumber(componentNode);
      throw new ExmlParseException("namespace '" + uri + "' of element '"+ name +"' in EXML file does not denote a config package", lineNumber);
    }
    return packageName + "." + name;
  }

  private void fillModelAttributes(JsonObject jsonObject, Node componentNode, String className) {
    ConfigClass configClass = registry.getConfigClassByName(className);
    if (configClass == null) {
      int lineNumber = getLineNumber(componentNode);
      throw new ExmlParseException("unknown type '" + className + "'", lineNumber);
    }
    NamedNodeMap attributes = componentNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String attributeName = attribute.getLocalName();
      String attributeValue = attribute.getValue();
      ConfigAttribute configAttribute = configClass.getCfgByName(attributeName);
      if (configAttribute == null) {
        jsonObject.set(attributeName, attributeValue);
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
          setStarTypedAttribute(jsonObject, attributeName, attributeValue);
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
        ConfigAttribute configAttribute = configClass.getCfgByName(elementName);
        if (configAttribute == null) {
          //Okay, so we have to guess the type
          if(element.hasChildNodes() ) {
            if(element.hasAttributes()) {
              //its a complex property with attributes and sub-properties
              JsonObject property = parseAttributes(jsonObject, element);
              //and ad the sub-properties to it
              NodeList propertyChildNotes = element.getChildNodes();
              for (int j = 0; j < propertyChildNotes.getLength(); j++) {
                Node propertyNode = propertyChildNotes.item(j);
                if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                  parseAttributes(property, propertyNode);
                }
              }
            } else {
              //it seems to be an array
              parseArray(jsonObject, element);
            }
          } else {
            //its a simple property
            parseAttributes(jsonObject, element);
          }

        } else if ("Array".equals(configAttribute.getType())) {
          // Array explicitly specified.
          parseArray(jsonObject, element);

        } else {
          //TODO: What here? also guessing? Or Error?
        }
      }
    }
  }

  private JsonObject parseAttributes(JsonObject jsonObject, Node node) {
    JsonObject simpleProperty = new JsonObject();
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String attributeName = attribute.getLocalName();
      String attributeValue = attribute.getValue();
      setStarTypedAttribute(simpleProperty,attributeName, attributeValue);
    }
    jsonObject.set(node.getLocalName(), simpleProperty);
    return simpleProperty;
  }

  private void parseArray(JsonObject jsonObject, Element element) {
    JsonArray jsonArray = new JsonArray();
    jsonObject.set(element.getLocalName(), jsonArray);
    NodeList arrayChildNodes = element.getChildNodes();

    for (int j = 0; j < arrayChildNodes.getLength(); j++) {
      Node arrayItemNode = arrayChildNodes.item(j);
      if (arrayItemNode.getNodeType() == Node.ELEMENT_NODE) {
        JsonObject arrayJsonObject;
        if(ExmlConstants.EXML_NAMESPACE_URI.equals(arrayItemNode.getNamespaceURI()) && ExmlConstants.EXML_OBJECT_NODE_NAME.equals(arrayItemNode.getLocalName())) {
          Object value = parseExmlObjectNode(arrayItemNode);
          if(value != null) {
            jsonArray.push(value);
          } //otherwise just ignore the empty exml:object element
        } else {
          arrayJsonObject = new JsonObject();
          String arrayItemClassName = createFullClassNameFromNode(arrayItemNode);
          jsonArray.push(arrayJsonObject.settingWrapperClass(arrayItemClassName));
          model.addImport(arrayItemClassName);

          fillModelAttributes(arrayJsonObject, arrayItemNode, arrayItemClassName);
        }
      }
    }
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
      NamedNodeMap attributes = exmlObjectNode.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++) {
        Attr attribute = (Attr) attributes.item(i);
        String attributeName = attribute.getLocalName();
        String attributeValue = attribute.getValue();
        setStarTypedAttribute(object, attributeName, attributeValue);
      }
      return object;
    }
  }


  private void setStarTypedAttribute(JsonObject jsonObject, String attributeName, String attributeValue) {
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
      throw new ExmlParseException("root node of EXML file must belong to namespace '" + ExmlConstants.EXML_NAMESPACE_URI + "', but was '" + root.getNamespaceURI() +"'", lineNumber);
    }
    if (!ExmlConstants.EXML_COMPONENT_NODE_NAME.equals(root.getLocalName())) {
      throw new ExmlParseException("root node of EXML file must be a <" + ExmlConstants.EXML_COMPONENT_NODE_NAME + ">, but was <" + root.getLocalName() +">", lineNumber);
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
