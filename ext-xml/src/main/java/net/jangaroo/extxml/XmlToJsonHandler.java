/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * Generates an internal representation for the component XML
 */
public class XmlToJsonHandler implements ContentHandler {

  private Locator locator;
  private Json result;
  private ComponentSuite componentSuite;
  private String extendedXtype;
  private Set<String> xtypes;
  private List<String> imports;

  Stack<Json> objects = new Stack<Json>();
  Stack<String> attributes = new Stack<String>();

  private boolean insideJson = false;

  private boolean expectObjects = true;

  public XmlToJsonHandler(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  public void startDocument() throws SAXException {

  }

  public void endDocument() throws SAXException {
    assert objects.empty();
    assert attributes.empty();
    assert expectObjects;
  }

  public void startPrefixMapping(String prefix, String uri) throws SAXException {

  }

  public void endPrefixMapping(String prefix) throws SAXException {

  }

  private JsonObject createJsonObject(String uri, String localName, String qName, Attributes atts) {
    JsonObject result = new JsonObject();
    for (int i = 0; i < atts.getLength(); i++) {
      result.properties.put(atts.getLocalName(i), atts.getValue(i));
    }
    return result;
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if ("component".equals(localName)) {
      //start the parsing
    } else {
      JsonObject jsonObject = createJsonObject(uri, localName, qName, atts);
      Json parentJson = objects.empty() ? null : objects.peek();

      if (expectObjects) {
        jsonObject.set("$name", localName);

        if (parentJson != null) {
          assert !attributes.empty();
          String attr = attributes.peek();
          Object obj = parentJson.get(attr);
          if (obj == null) {
            parentJson.set(attr, jsonObject);
          } else if (!(obj instanceof JsonArray)) {
            JsonArray array = new JsonArray();
            array.push(obj);
            array.push(jsonObject);
            parentJson.set(attr, array);
          } else {
            ((JsonArray) obj).push(jsonObject);
          }
        }
        objects.push(jsonObject);
        expectObjects = false;
      } else {
        assert parentJson != null;
        attributes.push(localName);
        expectObjects = true;
        if(!jsonObject.properties.isEmpty()) {
          parentJson.set(localName, jsonObject);
          objects.push(jsonObject);
          expectObjects = false;
        }
      }
    }
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("component".equals(localName)) {
      //done
    } else {
      if (expectObjects) {
        String attr = attributes.pop();
        assert localName.equals(attr);
        expectObjects = false;
      } else {
        Json json = objects.pop();
        expectObjects = true;

        if(json.get("$name") == null) {
          attributes.pop();
          expectObjects = false;
        }
        if (objects.empty()) {
          result = json;
        }

      }
    }

  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    String str = "";
    for (int i = start; i < start + length; i++)
      str += ch[i];
    if(!objects.empty() && objects.peek().get("$name")!= null && objects.peek().get("$name").equals("json"))
      objects.peek().set("plain", str);
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

  }

  public void processingInstruction(String target, String data) throws SAXException {

  }

  public void skippedEntity(String name) throws SAXException {

  }

  public String getJSON() {
    return result.toString();
  }

  interface Json {
    Object get(String property);

    void set(String property, Object value);
  }

  private class JsonArray implements Json {
    ArrayList<Object> items = new ArrayList<Object>();

    public String toString() {
      return items.toString();
    }

    public Object get(String property) {
      return items.get(Integer.parseInt(property));
    }

    public void set(String property, Object value) {
      items.set(Integer.parseInt(property), value);
    }

    public void push(Object value) {
      this.items.add(value);
    }
  }

  private class JsonObject implements Json {
    LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();

    public String toString() {
      return properties.toString();
    }

    public Object get(String property) {
      return properties.get(property);
    }

    public void set(String property, Object value) {
      this.properties.put(property, value);

    }
  }
}
