/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.xml;

import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.utils.log.Log;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
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
  private ConfigAttribute currentAttr = null;
  private boolean isInsideComplexType = false;
  private boolean isInsideCfg = false;
  private static final String SCHEMA = "schema";
  private static final String COMPLEX_TYPE = "complexType";
  private static final String ELEMENT = "element";
  private static final String ATTRIBUTE = "attribute";
  private static final String EXTENSION = "extension";
  private static final String DOCUMENTATION = "documentation";

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  private boolean isLocalName(String localName) {
    return localName.equals(parser.getLocalName());
  }

  private ComponentSuite createComponentSuite() {
    String targetNS = parser.getAttributeValue(null, "targetNamespace");
    String nsPrefix = parser.getNamespaceContext().getPrefix(targetNS);
    return new ComponentSuite(new ComponentSuiteRegistry(), targetNS, nsPrefix, null, null, null);
  }

  private ComponentClass createComponentClass() {
    String name = parser.getAttributeValue(null, "name");
    ComponentClass componentClass = new ComponentClass(null, name);
    ccStack.push(componentClass);
    return componentClass;
  }

  private void addXtypeToComponentClass() throws XMLStreamException {
    String xtype = parser.getAttributeValue(null, "id");
    if (xtype == null) {
      xtype = parser.getAttributeValue(null, "name");
    }

    String typeName = afterColon(parser.getAttributeValue(null, "type"));
    if (typeName.startsWith("ext.")) {
      xtype = xtype.toLowerCase();
    }

    ComponentClass componentClass = ccStack.lastElement();
    assert typeName.equals(componentClass.getFullClassName());
    componentClass.setXtype(xtype);
  }

  private String afterColon(String typeName) {
    return typeName.substring(typeName.indexOf(':') + 1);
  }

  private void addSupertypeToComponentClass() {
    String supertypeName = afterColon(parser.getAttributeValue(null, "base"));
    ComponentClass componentClass = ccStack.lastElement();
    componentClass.setSuperClassName(supertypeName);
  }

  private void addConfigElementAttribute() throws XMLStreamException {
    String name = parser.getAttributeValue(null, "name");
    ConfigAttribute attr = new ConfigAttribute(name, "Array");
    ccStack.lastElement().addCfg(attr);
    currentAttr = attr;
  }

  private void addConfigAttribute() throws XMLStreamException {
    String name = parser.getAttributeValue(null, "name");
    String type = parser.getAttributeValue(null, "type");
    ConfigAttribute attr = new ConfigAttribute(name, type);
    ccStack.lastElement().addCfg(attr);
    currentAttr = attr;
  }

  private void parseDocumentation() throws XMLStreamException {
    StringBuffer txt = new StringBuffer();
    while (parser.hasNext()) {
      parser.next();

      if (parser.hasText()) {
        txt.append(parser.getText());
      } else if (parser.isStartElement()) {
        txt.append("<").append(parser.getLocalName()).append(">");
      } else if (parser.isEndElement()) {
        if(isLocalName(DOCUMENTATION)) {
          break;
        } else {
          txt.append("</").append(parser.getLocalName()).append(">");
        }
      }
    }
    if (currentAttr != null) {
      currentAttr.setDescription(txt.toString());
    } else {
      ccStack.lastElement().setDescription(txt.toString());
    }
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
      Log.e("Error while parsing XSD", e);
    } finally {
      try {
        if (parser != null) {
          parser.close();
        }
      } catch (XMLStreamException e) {
        Log.e("Error while parsing XSD", e);
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
        } else if (isLocalName(DOCUMENTATION)) {
          parseDocumentation();
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
            currentAttr = null;
          } else {
            ComponentClass componentClass = ccStack.pop();
            componentSuite.addComponentClass(componentClass);
          }
        } else if(isLocalName(ATTRIBUTE)) {
          currentAttr = null;
        }

        break;
      case XMLStreamConstants.CHARACTERS:

        break;
      case XMLStreamConstants.CDATA:

        break;
    } // end switch
  }

}
