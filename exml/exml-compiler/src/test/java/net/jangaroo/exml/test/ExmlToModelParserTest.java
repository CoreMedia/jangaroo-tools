package net.jangaroo.exml.test;

import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToModelParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

public class ExmlToModelParserTest extends AbstractExmlTest {
  @Test
  public void testParseAllElements() throws Exception {
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    InputStream inputStream = getClass().getResourceAsStream("/exmlparser/AllElements.exml");
    ExmlModel model = exmlToModelParser.parse(inputStream);
    Assert.assertEquals(new HashSet<String>(Arrays.asList("ext.Panel", "ext.MessageBox")),
            model.getImports());
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "layout", "{config.myLayout}",
            "title", "I am a panel",
            "defaults", new JsonObject("layout","border"),
            "layoutConfig", new JsonObject(
                    "bla", "blub",
                    "anchor", new JsonObject("style", "test"),
                    "border", new JsonObject("type", "solid")
            ),
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "button",
                            "text", "Save"
                    ),
                    "{{xtype: \"editortreepanel\"}}"
            ),
            "menu", new JsonArray(
                    new JsonObject(
                            "xtype", "menuitem",
                            "text", "juhu1"
                    ),
                    new JsonObject(
                            "xtype", "menuitem",
                            "text", "juhu2"
                    ),
                    new JsonObject(
                            "xtype", "menuitem",
                            "text", "juhu3"
                    )
            ),
            "tools", new JsonArray(
                    new JsonObject(
                            "handler", "{function(x){return ''+x;}}",
                            "id", "gear"
                    )
            ),
            "plugins", new JsonArray(
                    new JsonObject(
                            "ptype", "aplugin"
                    ),
                    new JsonObject(
                            "ptype", "aplugin"
                    )
            ),
            "layout2", new JsonObject(
                    "type", "a"
            )
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testParseTestNumber() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    InputStream inputStream = getClass().getResourceAsStream("/exmlparser/TestNumber.exml");
    ExmlModel model = exmlToModelParser.parse(inputStream);
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                             "x", 100
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "x", 1.5
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "x", 1.0
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "x", -1.5
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "x", 3.0
                    )
            )
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testParseTestTrueFalse() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    InputStream inputStream = getClass().getResourceAsStream("/exmlparser/TestTrueFalse.exml");
    ExmlModel model = exmlToModelParser.parse(inputStream);
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                             "visible", true
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "visible", false
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "visible", true
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                            "visible", false
                    )
            )
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testInheritProperties() throws Exception{
    setUp("testNamespace.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    InputStream inputStream = getClass().getResourceAsStream("/testPackage/TestComponent2.exml");
    ExmlModel model = exmlToModelParser.parse(inputStream);
    Assert.assertEquals("testPackage.TestComponent", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "propertyThree", "3"
                    ).settingWrapperClass("testNamespace.config.TestComponent2")
            )
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }
}
