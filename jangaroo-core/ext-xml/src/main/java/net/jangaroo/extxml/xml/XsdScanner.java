/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.xml;

import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.log.Log;
import net.jangaroo.extxml.model.ConfigAttribute;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class XsdScanner {

  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  private ComponentSuite componentSuite;
  private XMLStreamReader parser = null;
  private Stack<ComponentClass> ccStack = new Stack<ComponentClass>();
  private boolean isInsideComplexType = false;
  private boolean isInsideCfg = false;
  private static final String SCHEMA = "schema";
  private static final String COMPLEX_TYPE = "complexType";
  private static final String ELEMENT = "element";
  private static final String ATTRIBUTE = "attribute";
  private static final String EXTENSION = "extension";

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  private boolean isLocalName(String localName) {
    return localName.equals(parser.getLocalName());
  }

  private ComponentSuite createComponentSuite() {
    String targetNS = parser.getAttributeValue(null, "targetNamespace");
    String nsPrefix = parser.getNamespaceContext().getPrefix(targetNS);
    return new ComponentSuite(targetNS, nsPrefix, null, null);
  }

  private ComponentClass createComponentClass() {
    String name = parser.getAttributeValue(null, "name");
    Log.getErrorHandler().info(String.format("createComponentClass: '%s'", name));
    ComponentClass componentClass = new ComponentClass(null, name);
    ccStack.push(componentClass);
    return componentClass;
  }

  private void addXtypeToComponentClass() {
    String xtype = parser.getAttributeValue(null, "name");
    String typeName = afterColon(parser.getAttributeValue(null, "type"));
    ComponentClass componentClass = ccStack.lastElement();
    assert typeName.equals(componentClass.getFullClassName());
    componentClass.setXtype(xtype);
    Log.getErrorHandler().info(String.format("Added xtype '%s' to component class '%s'", xtype, componentClass.getFullClassName()));
  }

  private String afterColon(String typeName) {
    return typeName.substring(typeName.indexOf(':') + 1);
  }

  private void addSupertypeToComponentClass() {
    String supertypeName = afterColon(parser.getAttributeValue(null, "base"));
    ComponentClass componentClass = ccStack.lastElement();
    componentClass.setSuperClassName(supertypeName);
    Log.getErrorHandler().info(String.format("Added supertype '%s' to component class '%s'", supertypeName, componentClass.getFullClassName()));
  }

  private void addConfigElementAttribute() {
    String name = parser.getAttributeValue(null, "name");
    ConfigAttribute attr = new ConfigAttribute(name, "Array");
    ccStack.lastElement().addCfg(attr);
    Log.getErrorHandler().info(String.format("Added config attribute '%s' to component class '%s'", attr.getName(), ccStack.lastElement().getFullClassName()));
  }

  private void addConfigAttribute() throws XMLStreamException {
    String name = parser.getAttributeValue(null, "name");
    String type = parser.getAttributeValue(null, "type");
    ConfigAttribute attr = new ConfigAttribute(name, type);
    ccStack.lastElement().addCfg(attr);
    Log.getErrorHandler().info(String.format("Added config attribute '%s' to component class '%s'", attr.getName(), ccStack.lastElement().getFullClassName()));
  }

  public ComponentSuite scan(InputStream xsd) throws IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {
      parser = factory.createXMLStreamReader(xsd);
      while (parser.hasNext()) {
        int event = parser.next();
        if (XML_SCHEMA_URL.equals(parser.getNamespaceURI())) {
          dispatch(event);
        }
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
    return componentSuite;
  }

  private void dispatch(int event) throws XMLStreamException {
    switch (event) {
      case XMLStreamConstants.START_ELEMENT:
        if (isLocalName(SCHEMA)) {
          componentSuite = createComponentSuite();
          break;
        } else if (!isInsideComplexType && isLocalName(COMPLEX_TYPE)) {
          isInsideComplexType = true;
          createComponentClass();
          break;
        } else if (isLocalName(ELEMENT)) {
          if (isInsideComplexType) {
            addConfigElementAttribute();
            isInsideCfg = true;
          } else {
            addXtypeToComponentClass();
          }
        } else if (isInsideComplexType && !isInsideCfg && isLocalName(EXTENSION)) {
          addSupertypeToComponentClass();
        } else if (isLocalName(ATTRIBUTE)) {
          addConfigAttribute();
        }
        break;
      case XMLStreamConstants.END_ELEMENT:
        if (isLocalName(SCHEMA)) {
          assert ccStack.isEmpty();
        } else if (isLocalName(COMPLEX_TYPE) && !isInsideCfg) {
          isInsideComplexType = false;
        } else if (isLocalName(ELEMENT)) {
          if (isInsideCfg) {
            isInsideCfg = false;
          } else {
            ComponentClass componentClass = ccStack.pop();
            componentSuite.addComponentClass(componentClass);
            Log.getErrorHandler().info(String.format("Added component class '%s, %s' to component suite '%s'", componentClass.getXtype(), componentClass.getFullClassName(), componentSuite.getNamespace()));
          }
        }

        break;
      case XMLStreamConstants.CHARACTERS:

        break;
      case XMLStreamConstants.CDATA:

        break;
    } // end switch
  }

}
