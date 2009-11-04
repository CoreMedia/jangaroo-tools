package net.jangaroo.extxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * An XsdScanner parses an Ext XML component declaration schema into a {@link ComponentSuite}.
 */
public final class XsdScanner {
  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  private DocumentBuilder builder = null;

  public XsdScanner() {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    try {
      builder = builderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      ComponentSuiteRegistry.getInstance().getErrorHandler().error("Error while preparing parser for xsd", e);
    }
  }

  public ComponentSuite scan(InputStream xsd) throws IOException {
    try {
      Document document = null;
      try {
        if (builder != null) {
          document = builder.parse(xsd);
        }
      } catch (SAXException e) {
        ComponentSuiteRegistry.getInstance().getErrorHandler().error("Error while parsing XSD", e);
      }
      if (document != null) {
        Element schemaElement = document.getDocumentElement();

        ComponentSuite componentSuite = new ComponentSuite(
          schemaElement.getAttribute("targetNamespace"),
          schemaElement.getAttribute("id"), null, null);
        NodeList components = document.getElementsByTagNameNS(XML_SCHEMA_URL, "element");
        for (int i = 0; i < components.getLength(); ++i) {
          Element component = (Element) components.item(i);
          String name = component.getAttribute("name");
          String type = component.getAttribute("type");
          int colonPos = type.indexOf(':');
          if (colonPos != -1) {
            type = type.substring(colonPos + 1);
          }
          componentSuite.addComponentClass(new ComponentClass(name, type));
        }
        return componentSuite;
      }
      return null;
    } finally {
      xsd.close();
    }
  }

}
