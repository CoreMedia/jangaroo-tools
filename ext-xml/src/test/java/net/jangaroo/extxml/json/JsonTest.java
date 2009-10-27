/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class JsonTest {

  @Test
  public void testStringPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("string", "This is a String");
    assertEquals("{string: \"This is a String\"}", start.toString());
  }

  @Test
  public void testJsonStringPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("json", "{somestuff}");
    assertEquals("{json: somestuff}", start.toString());
  }

  @Test
  public void testObjectPropertyInObject() throws Exception {
    JsonObject start = new JsonObject();
    start.set("object", new JsonObject());
    assertEquals("{object:   {}}", start.toString());
  }
}
