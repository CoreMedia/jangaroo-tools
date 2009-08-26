package net.jangaroo.extxml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * An XsdScanner parses an Ext XML component declaration schema into a {@link ComponentSuite}.
 */
public class XsdScanner {
  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  public XsdScanner(File xsd) {
    this.componentSuite = new ComponentSuite(xsd);
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public ComponentSuite scan() {
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document document = builder.parse(componentSuite.getXsd());
      String targetNamespace = document.getDocumentElement().getAttribute("targetNamespace");
      componentSuite.setNamespace(targetNamespace);
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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    XsdScanner xsdScanner = new XsdScanner(new File(args[0]));
    xsdScanner.scan();
    System.out.println(xsdScanner.getComponentSuite());
  }

  private ComponentSuite componentSuite;
}
