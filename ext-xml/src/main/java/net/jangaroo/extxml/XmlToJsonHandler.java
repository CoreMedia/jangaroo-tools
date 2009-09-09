/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Generates an internal representation for the component XML
 */
public class XmlToJsonHandler implements ContentHandler {

  private Locator locator;
  private Json result;
  private ComponentSuite componentSuite;

  private HashMap<String, String> imports = new HashMap<String, String>();
  private ArrayList<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();

  private ErrorHandler errorHandler;

  private static String numberPattern = "-?([0-9]+\\.[0-9]*)|(\\.[0-9]+)|([0-9]+)([eE][+-]?[0-9]+)?";

  private NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);

  Stack<Json> objects = new Stack<Json>();
  Stack<String> attributes = new Stack<String>();

  private boolean expectObjects = true;

  private boolean expectOptionalDescription = false;

  private boolean expectJSON = false;

  public XmlToJsonHandler(ComponentSuite componentSuite, ErrorHandler handler) {
    this.componentSuite = componentSuite;
    this.errorHandler = handler;
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  public void startDocument() throws SAXException {

  }

  public void endDocument() throws SAXException {
    if (!objects.empty()) {
      errorHandler.error("Json object stack not empty at the end of the document.",
          locator.getLineNumber(),
          locator.getColumnNumber());
    }
    if (!attributes.empty()) {
      errorHandler.error("Attribute stack not empty at the end of the document.",
          locator.getLineNumber(),
          locator.getColumnNumber());
    }
    if (!expectObjects) {
      errorHandler.error("The parser is in the wrong state at the end of the document.",
          locator.getLineNumber(),
          locator.getColumnNumber());
    }
  }

  public void startPrefixMapping(String prefix, String uri) throws SAXException {

  }

  public void endPrefixMapping(String prefix) throws SAXException {

  }

  private JsonObject createJsonObject(Attributes atts) {
    JsonObject result = new JsonObject();
    for (int i = 0; i < atts.getLength(); i++) {
      String attsValue = atts.getValue(i);
      Object typedValue;
      if ("true".equals(attsValue) || "false".equals(attsValue)) {
        typedValue = Boolean.parseBoolean(attsValue);
      } else if (attsValue.matches(numberPattern)) {
        try {
          typedValue = nf.parse(attsValue);
        } catch (ParseException e) {
          //well seems to be not a number...
          typedValue = attsValue;
        }
      } else {
        typedValue = attsValue;
      }
      result.properties.put(atts.getLocalName(i), typedValue);
    }
    return result;
  }

  /**
   * adds the element to the current json object, if it is an array knows how to handle it...
   * @param element the element to add.
   */
  private void addElementToJsonObject(Object element) {           
    Json parentJson = objects.empty() ? null : objects.peek();
    if (parentJson != null) {
      assert !attributes.empty();
      String attr = attributes.peek();
      Object obj = parentJson.get(attr);
      if (obj == null) {
        parentJson.set(attr, element);
      } else if (!(obj instanceof JsonArray)) {
        JsonArray array = new JsonArray();
        array.push(obj);
        array.push(element);
        parentJson.set(attr, array);
      } else {
        ((JsonArray) obj).push(element);
      }
    }
  }

  /**
   * Adds the Json to the stack and
   * indicates that no other json object is expected by the parser
   *
   * @param jsonObject the json to add to the stack
   */
  private void addObjectToStack(Json jsonObject) {
    objects.push(jsonObject);
    expectObjects = false;
  }

  /**
   * Adds the Attribute to the stack and
   * indicates that now only json objects are expected by the parser
   *
   * @param attribute the name of the attribute
   */
  private void addAttributeToStack(String attribute) {
    attributes.push(attribute);
    expectObjects = true;
  }

  /**
   * Removes the last json object from stack and indicates that other json objects
   * are expected by the parser
   *
   * @return the last json object from the stack
   */
  private Json removeObjectFromStack() {
    expectObjects = true;
    return objects.pop();
  }

  /**
   * Remove the last attribute from the attribute stack and indicate that attributes are
   * expected by the parser
   */
  private void removeAttributeFromStack() {
    attributes.pop();
    expectObjects = false;
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if ("component".equals(localName)) {
      //start the parsing
    } else if ("cfg".equals(localName)) {
      //handle config elements
      cfgs.add(new ConfigAttribute(atts.getValue("name"), atts.getValue("type")));
    } else if ("description".equals(localName)) {
      expectOptionalDescription = true;
    } else if ("json".equals(localName)) {
      //handle json elements differently: either as a anonymous element with attributes or
      //json as text node of the element.
      if (expectObjects) {
        if (atts.getLength() == 0) {
          expectJSON = true;
        } else {
          addElementToJsonObject(createJsonObject(atts));
        }
      }
    } else {

      JsonObject jsonObject = createJsonObject(atts);
      Json parentJson = objects.empty() ? null : objects.peek();

      if (expectObjects) {
        jsonObject.set("xtype", localName);
        //find component clazz for xtype
        ComponentClass compClazz = componentSuite.getComponentClassByXtype(localName);
        if (compClazz != null) {
          imports.put(localName, compClazz.getFullClassName());
        } else {
          imports.put(localName, null);
          errorHandler.warning(String.format("No component class for xtype '%s' found!", localName), locator.getLineNumber(), locator.getColumnNumber());
        }
        addElementToJsonObject(jsonObject);
        addObjectToStack(jsonObject);
      } else {
        assert parentJson != null;
        addAttributeToStack(localName);
        if (!jsonObject.properties.isEmpty()) {
          parentJson.set(localName, jsonObject);
          addObjectToStack(jsonObject);
        }
      }
    }
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("component".equals(localName)) {
      //done
    } else if ("cfg".equals(localName)) {
    } else if ("description".equals(localName)) {
      expectOptionalDescription = false;
    } else if ("json".equals(localName)) {
      expectJSON = false;
    } else {
      if (expectObjects) {
        String attr = attributes.pop();
        assert localName.equals(attr);
        expectObjects = false;
      } else {
        Json json = removeObjectFromStack();
        if (json.get("xtype") == null) {
          removeAttributeFromStack();
        }
        //if the the last object has been removed,
        //store the result
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
    if (expectOptionalDescription) {
      cfgs.get(cfgs.size() - 1).setDescription(str);
    }
    if (expectJSON) {
      addElementToJsonObject(str);
    }
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

  }

  public void processingInstruction(String target, String data) throws SAXException {

  }

  public void skippedEntity(String name) throws SAXException {

  }

  public Json getJSON() {
    return result;
  }

  public List<String> getImports() {
    return new ArrayList<String>(imports.values());
  }

  public List<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public String getSuperClassName() {
    return componentSuite.getComponentClassByXtype((String) this.result.get("xtype")).getFullClassName();
  }

  public String getJsonAsString() {
    return ((JsonObject) result).toJsonString("", "xtype");
  }

  interface Json {
    Object get(String property);

    void set(String property, Object value);
  }

  class JsonArray implements Json {
    ArrayList<Object> items = new ArrayList<Object>();

    public String toString() {
      return items.toString();
    }

    public String toJsonString(String spaces) {
      StringBuffer bf = new StringBuffer();
      bf.append("[\n");
      bf.append(spaces);
      boolean first = true;
      for (Object item : items) {
        if (!first) {
          bf.append(",\n");
          bf.append(spaces);
        }
        if (item instanceof JsonObject) {
          bf.append(((JsonObject) item).toJsonString("  " + spaces, null));
        } else {
          bf.append(item.toString());
        }
        first = false;
      }
      bf.append("]");
      return bf.toString();
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

  class JsonObject implements Json {
    LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();

    public String toString() {
      return toJsonString("", null);
    }

    public String toJsonString(String spaces, String ignoreProperty) {
      StringBuffer bf = new StringBuffer();
      bf.append(spaces);
      bf.append("{");
      boolean first = true;
      for (Map.Entry<String, Object> prop : this.properties.entrySet()) {
        String key = prop.getKey();
        Object value = prop.getValue();
        if (ignoreProperty == null || !key.equals(ignoreProperty)) {
          if (!first) {
            bf.append(",\n");
            bf.append("  ");
            bf.append(spaces);
          }
          bf.append(key);
          bf.append(": ");
          if (value instanceof Number
              || value instanceof Boolean) {
            bf.append(value.toString());
          } else if (value instanceof JsonObject) {
            bf.append(((JsonObject) value).toJsonString("  " + spaces, null));
          } else if (value instanceof JsonArray) {
            bf.append(((JsonArray) value).toJsonString("  " + spaces));
          } else if (key.equals("xtype")) {
            String className = imports.get(value);
            if (className != null) {
              bf.append(imports.get(value));
              bf.append(".xtype");
            } else {
              bf.append("\"").append(value.toString()).append("\"");
            }
          } else if (((String) value).startsWith("{") && ((String) value).endsWith("}")) {
            bf.append(((String) value).substring(1, ((String) value).lastIndexOf("}")));
          } else {
            bf.append("\"").append(value.toString()).append("\"");
          }
          first = false;
        }
      }
      bf.append("}");
      return bf.toString();
    }


    public Object get(String property) {
      return properties.get(property);
    }

    public void set(String property, Object value) {
      this.properties.put(property, value);

    }
  }
}
