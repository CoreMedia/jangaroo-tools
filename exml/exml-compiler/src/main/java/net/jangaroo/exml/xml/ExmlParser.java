package net.jangaroo.exml.xml;

import net.jangaroo.exml.ExmlConstants;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExmlParser {
  private final ConfigClassRegistry registry;
  private Set<String> imports = new LinkedHashSet<String>();

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
          throw new RuntimeException("root node of EXML contained more than one component definition");
        }
        componentNode = node;
      }
    }
    if (componentNode == null) {
      throw new RuntimeException("root node of EXML did not contain a component definition");
    }

    String name = componentNode.getLocalName();
    String uri = componentNode.getNamespaceURI();
    String packageName = ExmlConstants.parsePackageFromNamespace(uri);
    if (packageName == null) {
      throw new RuntimeException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package");
    }
    model.setParentClassName(packageName, name);
    fillModelAttributes(model.getJsonObject(), componentNode, packageName, name);

    return model;
  }

  private void fillModelAttributes(JsonObject jsonObject, Node componentNode, String packageName, String name) {
    String fullClassName = packageName + "." + name;
    ConfigClass configClass = registry.getConfigClassByName(fullClassName);
    if (configClass == null) {
      throw new RuntimeException("unknown type '" + fullClassName + "'");
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

        } else if ("Array".equals(configAttribute.getType())) {
          // Array explicitly specified.

        } else {

        }
      }
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
    if (!ExmlConstants.EXML_NAMESPACE_URI.equals(root.getNamespaceURI())) {
      throw new RuntimeException("root node of EXML file must belong to namespace '" + ExmlConstants.EXML_NAMESPACE_URI + "', but was '" + root.getNamespaceURI() +"'");
    }
    if (!ExmlConstants.EXML_COMPONENT_NODE_NAME.equals(root.getLocalName())) {
      throw new RuntimeException("root node of EXML file must be a <" + ExmlConstants.EXML_COMPONENT_NODE_NAME + ">, but was <" + root.getLocalName() +">");
    }
  }

  private Document buildDom(InputStream inputStream) throws SAXException, IOException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setIgnoringComments(true);
      factory.setIgnoringElementContentWhitespace(true);
      return factory.newDocumentBuilder().parse(inputStream);
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
  }
}
