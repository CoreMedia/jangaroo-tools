/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.json;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 */
public class JsonTest {

  @Test
  public void testStringPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("string", "This is a String");
    assertEquals("{string: \"This is a String\"}", start.toString(2));
  }

  @Test
  public void testJsonStringPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("json", "{somestuff}");
    assertEquals("{json: somestuff}", start.toString(2));
  }

  @Test
  public void testObjectPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("object", new JsonObject());
    assertEquals("{object: {}}", start.toString(2));
  }

  @Test
  public void prettyPrint() throws Exception {
    JsonObject start = new JsonObject();
    start.set("object", new JsonObject());

    JsonArray array = new JsonArray();
    array.push("one");
    array.push("two");
    JsonObject sub = new JsonObject();
    sub.set("yes", false);
    sub.set("number", 1.6);
    sub.set("null", null);
    array.push(sub);
    start.set("items", array);
    start.set("function", "{function {return;}}");
    start.set("desc","\nsdjaflsdjf \t jasdljf");


    System.out.println(start.toString(2));
    assertEquals("{\n" +
        "  object: {},\n" +
        "  items: [\n" +
        "    \"one\",\n" +
        "    \"two\",\n" +
        "    {\n" +
        "      yes: false,\n" +
        "      number: 1.6,\n" +
        "      null: null\n" +
        "    }\n" +
        "  ],\n" +
        "  function: function {return;},\n" +
        "  desc: \"\\nsdjaflsdjf \\t jasdljf\"\n" +
        "}", start.toString(2));
  }

}
