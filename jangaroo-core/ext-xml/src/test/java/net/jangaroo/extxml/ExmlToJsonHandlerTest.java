/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import net.jangaroo.extxml.json.Json;
import net.jangaroo.extxml.json.JsonObject;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import utils.TestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;


/**
 *
 */
public class ExmlToJsonHandlerTest {

  private ExmlToJsonHandler jsonHandler;
  private ExmlToComponentMetadataHandler metadataHandler;


  @Before
  public void initTest() throws Exception {
    Log.setErrorHandler(new StandardOutErrorHandler());
  }

  @After
  public void checkExpectedErrors() {
    //errorHandler.checkExpectedErrors();
  }

  @Test
  public void testCDATA() throws Exception {
    parseExml("/testCDATA.exml");
    Json json = jsonHandler.getJson();

    //config elements
    List<ConfigAttribute> cfgs = metadataHandler.getCfgs();
    ConfigAttribute attr = cfgs.get(0);
    assertEquals("This is my <b>descripion</b>",attr.getDescription().trim());

    attr = cfgs.get(1);
    assertEquals("This is my <b>descripion</b>",attr.getDescription().trim());

    Json itemsArray = (Json) json.get("items");

    String jsonElem  = (String) itemsArray.get("0");
    assertEquals("{{xtype: \"editortreepanel\"}}", jsonElem.trim());

    jsonElem  = (String) itemsArray.get("1");
    assertEquals("{{xtype: \"editortreepanel\"}}", jsonElem.trim());

    String tbl = (String) json.get("tbl");
    assertEquals("{{xtype: \"editortreepanel\"}}", tbl.trim());
    

  }

  @Test
  public void testExmlTypeAttribute() throws Exception {
    parseExml("/testExmlTypeAttribute.exml");
    Json json = jsonHandler.getJson();
    Json itemsArray = (Json) json.get("items");

    //assertTrue(itemsArray instanceof JsonArray);

  }

  @Test
  public void testComponent() throws Exception {
    parseExml("/TestComponent.exml");
    Json json = jsonHandler.getJson();

    assertTrue(json instanceof JsonObject);
    assertEquals("panel", jsonHandler.getXtype());
    assertEquals("I am a panel", json.get("title"));
    assertEquals("{config.myLayout}", json.get("layout"));

    Json defaultProp = (Json) json.get("defaults");
    assertNotNull(defaultProp);
    assertNull(defaultProp.get("xtype"));
    assertEquals("border", defaultProp.get("layout"));

    Json layoutConfig = (Json) json.get("layoutConfig");
    assertNotNull(layoutConfig);
    assertNull(layoutConfig.get("xtype"));
    assertEquals("blub", layoutConfig.get("bla"));

    Json anchor = (Json) layoutConfig.get("anchor");
    assertNotNull(anchor);
    assertNull(anchor.get("xtype"));
    assertEquals("test", anchor.get("style"));

    Json border = (Json) layoutConfig.get("border");
    assertNotNull(border);
    assertNull(border.get("xtype"));
    assertEquals("solid", border.get("type"));
    
    Json itemsArray = (Json) json.get("items");
    assertNotNull(itemsArray);

    Json firstElem  = (Json) itemsArray.get("0");
    assertNotNull(firstElem);
    assertEquals("button", firstElem.get("xtype"));

    String jsonElem  = (String) itemsArray.get("1");
    assertNotNull(jsonElem);
    assertEquals("{{xtype: \"editortreepanel\"}}", jsonElem.trim());

    Json tools = (Json) json.get("tools");
    assertNotNull(tools);
    assertEquals("gear", tools.get("id"));
    assertEquals("{function(x){return ''+x;}}", tools.get("handler"));

    //config elements
    List<ConfigAttribute> cfgs = metadataHandler.getCfgs();
    assertFalse(cfgs.isEmpty());
    ConfigAttribute attr = cfgs.get(0);
    assertEquals("myProperty",attr.getName());
    assertEquals("String", attr.getJsType());
    assertNull(attr.getDescription());

    attr = cfgs.get(1);
    assertEquals("myPropertyWithDescription",attr.getName());
    assertEquals("Boolean", attr.getJsType());
    assertNotNull(attr.getDescription());

    //imports
    List<String> imports = jsonHandler.getImports();
    assertTrue(imports.contains("ext.MessageBox"));

    //class description
    assertEquals("This is my <b>TestCompoent</b>", metadataHandler.getComponentDescription());

  }

  @Test
  public void testTrueFalse() throws Exception {
    parseExml("/TestTrueFalse.exml");
    Json json = jsonHandler.getJson();

    Json itemsArray = (Json) json.get("items");
    Boolean b = (Boolean) ((Json)itemsArray.get("0")).get("x");
    assertTrue(b);

    String s = (String) ((Json)itemsArray.get("2")).get("x");
    assertEquals("True", s);

    s = (String) ((Json)itemsArray.get("3")).get("x");
    assertEquals("FALSE", s);
  }

  @Test
  public void testNumber() throws Exception {
    parseExml("/TestNumber.exml");
    Json json = jsonHandler.getJson();

    Json itemsArray = (Json) json.get("items");
    Number n = (Number) ((Json)itemsArray.get("0")).get("x");
    assertEquals(100, n.intValue());

    String s = (String) ((Json)itemsArray.get("1")).get("x");
    assertEquals("200xyz",s);

    n = (Number) ((Json)itemsArray.get("2")).get("x");
    assertEquals(1.5, n);

     n = (Number) ((Json)itemsArray.get("3")).get("x");
    assertEquals(1, n.intValue());

     n = (Number) ((Json)itemsArray.get("4")).get("x");
    assertEquals(-1.5, n);

    s = (String) ((Json)itemsArray.get("5")).get("x");
    assertEquals("3d",s);
  }

  @Test
  public void testEmptyComponent() throws Exception {
    parseExml("/EmptyCompontent.exml");
    Json json = jsonHandler.getJson();
    assertNotNull(json);
    System.out.println(json);
    assertEquals("{anchor:\"100%\",frame:true,collapsible:true,draggable:true,cls:\"x-portlet\"}", json.toString(0,0).replaceAll("\\s",""));
  }

  @Test
  public void testToJsonString() throws Exception {
    parseExml("/TestComponent.exml");
    System.out.println(jsonHandler.getJson());
  }

  private void parseExml(String path) throws SAXException, IOException, URISyntaxException {
    ComponentSuite dummyComponentSuite = new ComponentSuite("test", "test", null, null);
    jsonHandler = new ExmlToJsonHandler(dummyComponentSuite);
    parseExmlWithHandler(path, jsonHandler);
    metadataHandler = new ExmlToComponentMetadataHandler(dummyComponentSuite);
    parseExmlWithHandler(path, metadataHandler);
    Json json = jsonHandler.getJson();
    System.out.println("xtype: " + jsonHandler.getXtype());
    System.out.println(json);
  }

  private void parseExmlWithHandler(String path, ContentHandler handler) throws SAXException, URISyntaxException, IOException {
    XMLReader xr = XMLReaderFactory.createXMLReader();
    xr.setContentHandler(handler);
    InputStream stream = new FileInputStream(TestUtils.getFile(path, getClass()));
    try {
      xr.parse(new InputSource(stream));
    } finally {
      stream.close();
    }
  }
}
