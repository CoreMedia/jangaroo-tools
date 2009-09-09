/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import utils.TestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 */
public class XmlToJsonHandlerTest extends TestCase {

  public void testComponent() throws Exception {
    XmlToJsonHandler handler  = parseJson("/TestComponent.xml");
    XmlToJsonHandler.Json json = handler.getJSON();

    assertTrue(json instanceof XmlToJsonHandler.JsonObject);
    assertEquals("panel", json.get("xtype"));
    assertEquals("I am a panel", json.get("title"));
    assertEquals("{config.myLayout}", json.get("layout"));

    XmlToJsonHandler.Json defaultProp = (XmlToJsonHandler.Json) json.get("default");
    assertNotNull(defaultProp);
    assertNull(defaultProp.get("xtype"));
    assertEquals("border", defaultProp.get("layout"));

    XmlToJsonHandler.Json layoutConfig = (XmlToJsonHandler.Json) json.get("layoutConfig");
    assertNotNull(layoutConfig);
    assertNull(layoutConfig.get("xtype"));
    assertEquals("blub", layoutConfig.get("bla"));

    XmlToJsonHandler.Json anchor = (XmlToJsonHandler.Json) layoutConfig.get("anchor");
    assertNotNull(anchor);
    assertNull(anchor.get("xtype"));
    assertEquals("test", anchor.get("style"));

    XmlToJsonHandler.Json border = (XmlToJsonHandler.Json) layoutConfig.get("border");
    assertNotNull(border);
    assertNull(border.get("xtype"));
    assertEquals("solid", border.get("type"));
    
    XmlToJsonHandler.Json itemsArray = (XmlToJsonHandler.Json) json.get("items");
    assertNotNull(itemsArray);

    XmlToJsonHandler.Json firstElem  = (XmlToJsonHandler.Json) itemsArray.get("0");
    assertNotNull(firstElem);
    assertEquals("button", firstElem.get("xtype"));

    String jsonElem  = (String) itemsArray.get("1");
    assertNotNull(jsonElem);
    assertEquals("{xtype: \"editortreepanel\"}", jsonElem);

    XmlToJsonHandler.Json tools = (XmlToJsonHandler.Json) json.get("tools");
    assertNotNull(tools);
    assertEquals("gear", tools.get("id"));
    assertEquals("{function(x){return ''+x;}}", tools.get("handler"));

    //config elements

    List<ConfigAttribute> cfgs = handler.getCfgs();
    assertFalse(cfgs.isEmpty());
    ConfigAttribute attr = cfgs.get(0);
    assertEquals("myProperty",attr.getName());
    assertEquals("String", attr.getJsType());
    assertNull(attr.getDescription());

    attr = cfgs.get(1);
    assertEquals("myPropertyWithDescription",attr.getName());
    assertEquals("Boolean", attr.getJsType());
    assertNotNull(attr.getDescription());
  }

  public void testTrueFalse() throws Exception {
    XmlToJsonHandler handler = parseJson("/TestTrueFalse.xml");
    XmlToJsonHandler.Json json = handler.getJSON();

    XmlToJsonHandler.Json itemsArray = (XmlToJsonHandler.Json) json.get("items");
    Boolean b = (Boolean) ((XmlToJsonHandler.Json)itemsArray.get("0")).get("x");
    assertTrue(b);

    String s = (String) ((XmlToJsonHandler.Json)itemsArray.get("2")).get("x");
    assertEquals("True", s);

    s = (String) ((XmlToJsonHandler.Json)itemsArray.get("3")).get("x");
    assertEquals("FALSE", s);
  }

  public void testNumber() throws Exception {
    XmlToJsonHandler handler = parseJson("/TestNumber.xml");
    XmlToJsonHandler.Json json = handler.getJSON();

    XmlToJsonHandler.Json itemsArray = (XmlToJsonHandler.Json) json.get("items");
    Number n = (Number) ((XmlToJsonHandler.Json)itemsArray.get("0")).get("x");
    assertEquals(100, n.intValue());

    String s = (String) ((XmlToJsonHandler.Json)itemsArray.get("1")).get("x");
    assertEquals("200xyz",s);

    n = (Number) ((XmlToJsonHandler.Json)itemsArray.get("2")).get("x");
    assertEquals(1.5, n);

     n = (Number) ((XmlToJsonHandler.Json)itemsArray.get("3")).get("x");
    assertEquals(1, n.intValue());

     n = (Number) ((XmlToJsonHandler.Json)itemsArray.get("4")).get("x");
    assertEquals(-1.5, n);

    s = (String) ((XmlToJsonHandler.Json)itemsArray.get("5")).get("x");
    assertEquals("3d",s);
  }

  public void testEmptyComponent() throws Exception {
    XmlToJsonHandler handler = parseJson("/EmptyCompontent.xml");
    String json = handler.getJsonAsString();
    assertNotNull(json);
    System.out.println(json);
    assertEquals("{anchor:\"100%\",frame:true,collapsible:true,draggable:true,cls:\"x-portlet\"}", json.replaceAll("\\s",""));
  }

  public void testToJsonString() throws Exception {
    XmlToJsonHandler handler = parseJson("/TestComponent.xml");
    System.out.println(handler.getJsonAsString());
  }

  private XmlToJsonHandler parseJson(String path) throws SAXException, IOException, URISyntaxException {
    XMLReader xr = XMLReaderFactory.createXMLReader();
    XmlToJsonHandler handler = new XmlToJsonHandler(new ComponentSuite(), new StandardOutErrorHandler());
    xr.setContentHandler(handler);
    xr.parse(new InputSource(new FileInputStream(TestUtils.getFile(path, getClass()))));
    XmlToJsonHandler.Json json = handler.getJSON();
    System.out.println(json);
    return handler;
  }
}
