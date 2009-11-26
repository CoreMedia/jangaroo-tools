/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class XsdScanner2 {

  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  private ComponentSuite componentSuite;
  private XMLStreamReader parser = null;
  private Stack<ComponentClass> ccStack = new Stack<ComponentClass>();

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  private boolean isXmlSchema() {
    return XML_SCHEMA_URL.equals(parser.getNamespaceURI()) && "schema".equals(parser.getLocalName());
  }

  private boolean isComponentTypeDefinition() {
    return XML_SCHEMA_URL.equals(parser.getNamespaceURI())
        && "complexType".equals(parser.getLocalName())
        && (parser.getAttributeCount() == 2);
  }

  private boolean isElementAttribute() {
    return XML_SCHEMA_URL.equals(parser.getNamespaceURI())
        && "element".equals(parser.getLocalName())
        && (parser.getAttributeCount() == 3);
  }

  private boolean isAttribute() {
    return XML_SCHEMA_URL.equals(parser.getNamespaceURI())
        && "attribute".equals(parser.getLocalName())
        && (parser.getAttributeCount() == 2);
  }

  private void createComponentSuite() {
    String targetNS = parser.getAttributeValue(null, "targetNamespace");
    String nsPrefix = parser.getNamespaceContext().getPrefix(targetNS);
    componentSuite = new ComponentSuite(targetNS, nsPrefix, null, null);
  }

  private void createComponentClass() {
    String id = parser.getAttributeValue(null, "id");
    String name = parser.getAttributeValue(null, "name");

    ccStack.push(new ComponentClass(id, name));
  }

  private void createConfigElementAttribute() {
    String name = parser.getAttributeValue(null, "name");
    ConfigAttribute attr = new ConfigAttribute(name, "Array");
    ccStack.lastElement().addCfg(attr);
  }

  private void createConfigAttribute() {
    String name = parser.getAttributeValue(null, "name");
    String type = parser.getAttributeValue(null, "type");
    ConfigAttribute attr = new ConfigAttribute(name, type);
    ccStack.lastElement().addCfg(attr);
  }

  public void scan(InputStream xsd) throws IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {
      parser = factory.createXMLStreamReader(xsd);
      for (int event = parser.next();
           event != XMLStreamConstants.END_DOCUMENT;
           event = parser.next()) {
        switch (event) {
          case XMLStreamConstants.START_ELEMENT:
            if (isXmlSchema()) {
              createComponentSuite();
              break;
            } else if (isComponentTypeDefinition()) {
              createComponentClass();
              break;
            } else if (isElementAttribute()) {
              createConfigElementAttribute();
            } else if (isAttribute()) {
              createConfigAttribute();
            }
            break;
          case XMLStreamConstants.END_ELEMENT:
            if (isXmlSchema()) {
              for (ComponentClass c : ccStack) {
                componentSuite.addComponentClass(c);
              }
            }

            break;
          case XMLStreamConstants.CHARACTERS:

            break;
          case XMLStreamConstants.CDATA:

            break;
        } // end switch
      }
    } catch (XMLStreamException e) {
      Log.getErrorHandler().error("Error while parsing XSD", e);
    } finally {
      try {
        if (parser != null) {
          parser.close();
        }
      } catch (XMLStreamException e) {
        Log.getErrorHandler().error("Error while parsing XSD", e);
      }
    }
  }


}
