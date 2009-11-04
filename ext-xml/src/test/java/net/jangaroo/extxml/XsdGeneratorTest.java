/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import utils.TestUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 *
 */
public class XsdGeneratorTest {

  private static final String XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";

  @Test
  public void emptySuite() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    XsdGenerator generator = new XsdGenerator(suite, new StandardOutErrorHandler());
    StringWriter writer = new StringWriter();
    generator.generateXsd(writer);

    assertTrue(writer.toString().length() == 0);
  }

  @Test
  public void simpleSuite() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    suite.setNamespace("com.coremedia.examples");
    suite.setNs("cm");

    ComponentClass clazz = new ComponentClass("TestClass", "com.coremedia.examples.TestClass");
    clazz.setDescription("Some Description");
    suite.addComponentClass(clazz);
    Document dom = createDom(suite, "/generateSchemaTests/SimpleSuite.exml");
    assertNotNull(dom);
    Element schemaElement = dom.getDocumentElement();
    assertEquals("com.coremedia.examples", schemaElement.getAttribute("targetNamespace"));

    NodeList types = dom.getElementsByTagNameNS(XML_SCHEMA_URL, "complexType");
    assertNotNull(types);
    Element type = (Element)types.item(0);
    assertEquals("com.coremedia.examples.TestClass", type.getAttribute("name"));

    NodeList components = dom.getElementsByTagNameNS(XML_SCHEMA_URL, "element");
    assertNotNull(components);

    Element elem = (Element)components.item(0);
    assertNotNull(elem);
    assertEquals("TestClass", elem.getAttribute("name"));
    assertEquals("cm:com.coremedia.examples.TestClass", elem.getAttribute("type"));
  }

  @Test
  public void simpleSuiteWithFullQualifiedXtypes() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    suite.setNamespace("com.coremedia.examples");
    suite.setNs("cm");

    ComponentClass clazz = new ComponentClass("com.coremedia.examples.TestClass", "com.coremedia.examples.TestClass");
    suite.addComponentClass(clazz);
    Document dom = createDom(suite ,"/generateSchemaTests/SimpleSuite.exml");
    assertNotNull(dom);
    Element schemaElement = dom.getDocumentElement();
    assertEquals("com.coremedia.examples", schemaElement.getAttribute("targetNamespace"));

    NodeList types = dom.getElementsByTagNameNS(XML_SCHEMA_URL, "complexType");
    assertNotNull(types);
    Element type = (Element)types.item(0);
    assertEquals("com.coremedia.examples.TestClass", type.getAttribute("name"));

    NodeList components = dom.getElementsByTagNameNS(XML_SCHEMA_URL, "element");
    assertNotNull(components);

    Element elem = (Element)components.item(0);
    assertNotNull(elem);
    assertEquals("TestClass", elem.getAttribute("name"));
    assertEquals("cm:com.coremedia.examples.TestClass", elem.getAttribute("type"));
  }

  @Test
  public void classWithAttributes() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    suite.setNamespace("com.coremedia.examples");
    suite.setNs("cm");

    ComponentClass clazz = new ComponentClass("TestClass", "com.coremedia.examples.TestClass");
    clazz.addCfg(new ConfigAttribute("simpleType","Boolean"));
    clazz.addCfg(new ConfigAttribute("simpleFloat","Float"));
    clazz.addCfg(new ConfigAttribute("simpleTypeWithDescription","Number","dlfjasdlfj adlsjf adsj foijd ofj \naksldjfklasj"));
    clazz.addCfg(new ConfigAttribute("simpleObject","Object"));
    clazz.addCfg(new ConfigAttribute("simpleArray","Array","adsfasdfasdf"));
    suite.addComponentClass(clazz);
    createDom(suite, "/generateSchemaTests/ClassWithAttributes.exml");
  }

  @Test(expected = SAXParseException.class)
  public void classWithDuplicateAttributes() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    suite.setNamespace("com.coremedia.examples");
    suite.setNs("cm");

    ComponentClass clazz = new ComponentClass("TestClass", "com.coremedia.examples.TestClass");
    clazz.addCfg(new ConfigAttribute("simpleType","Boolean"));
    clazz.addCfg(new ConfigAttribute("simpleType","Boolean"));
    suite.addComponentClass(clazz);
    createDom(suite, "/generateSchemaTests/SimpleSuite.exml");
  }

  @Test
  public void classWithInheritence() throws Exception {
    ComponentSuite suite = new ComponentSuite();
    suite.setNamespace("com.coremedia.examples");
    suite.setNs("cm");

    ComponentClass clazz = new ComponentClass("TestClass", "com.coremedia.examples.TestClass");
    clazz.addCfg(new ConfigAttribute("simpleFloat","Float"));
    clazz.addCfg(new ConfigAttribute("simpleTypeWithDescription","Number","dlfjasdlfj adlsjf adsj foijd ofj \naksldjfklasj"));
    clazz.addCfg(new ConfigAttribute("simpleObject","Object"));
    clazz.addCfg(new ConfigAttribute("simpleArray","Array","adsfasdfasdf"));
    suite.addComponentClass(clazz);

    ComponentClass subclass = new ComponentClass("SubClass", "com.coremedia.examples.SubClass");
    subclass.setSuperClassName("com.coremedia.examples.TestClass");
    subclass.addCfg(new ConfigAttribute("simpleType","Boolean"));
    subclass.addCfg(new ConfigAttribute("items","Array","adsfasdfasdf"));
    suite.addComponentClass(subclass);    

    createDom(suite, "/generateSchemaTests/ClassWithInheritence.exml");
  }

  private Document createDom(ComponentSuite suite, String path) throws Exception {
    XsdGenerator generator = new XsdGenerator(suite, new StandardOutErrorHandler());
    StringWriter writer = new StringWriter();
    generator.generateXsd(writer);
    System.out.println(writer.toString());
    Reader r = new StringReader(writer.toString());
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    DocumentBuilder builder = builderFactory.newDocumentBuilder();
    Document dom =  builder.parse(new InputSource(r));

    // validate schema
    SchemaFactory factory = SchemaFactory.newInstance(XML_SCHEMA_URL);
    FileInputStream schemastream = new FileInputStream(TestUtils.getFile("/net/jangaroo/extxml/schemas/extxml.xsd", getClass()));
    Schema schema = factory.newSchema(new Source[] {new DOMSource(dom), new StreamSource(schemastream)});

    Validator v = schema.newValidator();
    FileInputStream stream = new FileInputStream(TestUtils.getFile(path, getClass()));
    v.validate(new StreamSource(stream));
    stream.close();
    schemastream.close();

    return dom;

  }
}
