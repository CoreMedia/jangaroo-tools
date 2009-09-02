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
public class XsdScanner {
  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  private XsdScanner() {
  }

  public static ComponentSuite scan(InputStream xsd) throws IOException, SAXException, ParserConfigurationException {
      ComponentSuite componentSuite = new ComponentSuite();
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document document = builder.parse(xsd);
      Element schemaElement = document.getDocumentElement();
      componentSuite.setNamespace(schemaElement.getAttribute("targetNamespace"));
      componentSuite.setNs(schemaElement.getAttribute("id"));
      NodeList components = document.getElementsByTagNameNS(XML_SCHEMA_URL, "element");
      for (int i=0; i<components.getLength(); ++i) {
        Element component = (Element)components.item(i);
        String name = component.getAttribute("name");
        String type = component.getAttribute("type");
        int colonPos = type.indexOf(':');
        if (colonPos!=-1) {
          type = type.substring(colonPos+1);
        }
        componentSuite.addComponentClass(new ComponentClass(name, type));
      }
      return componentSuite;
  }

  public static ComponentSuite getExt3ComponentSuite() throws IOException, SAXException, ParserConfigurationException {
    return scan(XsdScanner.class.getResourceAsStream("/net/jangaroo/extxml/schemas/ext3.xsd"));
  }
}
