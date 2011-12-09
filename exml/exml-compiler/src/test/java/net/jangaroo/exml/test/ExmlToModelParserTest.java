package net.jangaroo.exml.test;

import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToModelParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;

public class ExmlToModelParserTest extends AbstractExmlTest {
  @Test
  public void testParseAllElements() throws Exception {
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/AllElements.exml"));
    Assert.assertEquals(new HashSet<String>(Arrays.asList("exmlparser.config.allElements", "ext.Panel", "ext.MessageBox")),
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

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestNumber.exml"));
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "panel",
                            "id", "foo",
                             "x", 100.0
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

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestTrueFalse.exml"));
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
  public void testParseArrayAttribute() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestArrayAttribute.exml"));
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", "{config.myItems}",
            "tools", new JsonArray("tools")
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testParseActions() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestActions.exml"));
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "baseAction", "{new ext.Action(ext.config.action({disabled: false}))}"
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testParseUntyped() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestUntyped.exml"));
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "panel",
                             "untyped", "text"
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", true
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", false
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", 1.0
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", -1.5
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", 3.0
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", "3L"
                    ),
                    new JsonObject(
                            "xtype", "panel",
                            "untyped", "42x"
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

    ExmlModel model = exmlToModelParser.parse(getFile("/testPackage/TestComponent2.exml"));
    Assert.assertEquals("testPackage.TestComponent", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "propertyThree", "3"
                    ).settingWrapperClass("testNamespace.config.testComponent2")
            ),
            "columns", new JsonObject("xtype", "agridcolumn")
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testBaseClass() throws Exception {
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestBaseClassUnqualified.exml"));
    Assert.assertEquals("BaseClass", model.getSuperClassName());

    model = exmlToModelParser.parse(getFile("/exmlparser/TestBaseClass.exml"));
    Assert.assertEquals("someOtherPackage.base.BaseClass", model.getSuperClassName());
  }

  @Test
  public void testConstants() throws Exception {
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestConstants.exml"));
    Declaration aConstant = model.getConfigClass().getConstants().get(0);
    Assert.assertEquals("A_CONSTANT", aConstant.getName());
    Assert.assertEquals("\"One two three\"", aConstant.getValue());
    Assert.assertEquals("String", aConstant.getType());
    Assert.assertEquals("This is some constant", aConstant.getDescription());

    Declaration bConstant = model.getConfigClass().getConstants().get(1);
    Assert.assertEquals("B_CONSTANT", bConstant.getName());
    Assert.assertEquals("456", bConstant.getValue());
    Assert.assertEquals("uint", bConstant.getType());
    Assert.assertNull(bConstant.getDescription());

    Declaration cConstant = model.getConfigClass().getConstants().get(2);
    Assert.assertEquals("C_CONSTANT", cConstant.getName());
    Assert.assertEquals("new Object()", cConstant.getValue());
    Assert.assertEquals("Object", cConstant.getType());
    Assert.assertNull(cConstant.getDescription());

    Declaration dConstant = model.getConfigClass().getConstants().get(3);
    Assert.assertEquals("D_CONSTANT", dConstant.getName());
    Assert.assertEquals("new button()", dConstant.getValue());
    Assert.assertEquals("ext.config.button", dConstant.getType());
    Assert.assertTrue(model.getImports().contains("ext.config.button"));
    Assert.assertNull(dConstant.getDescription());

    Declaration eConstant = model.getConfigClass().getConstants().get(4);
    Assert.assertEquals("E_CONSTANT", eConstant.getName());
    Assert.assertEquals("new Container()", eConstant.getValue());
    Assert.assertEquals("ext.Component", eConstant.getType());
    Assert.assertTrue(model.getImports().contains("ext.Component"));
    Assert.assertTrue(model.getImports().contains("ext.Container"));
    Assert.assertNull(eConstant.getDescription());
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlToModelParserTest.class.getResource(path).toURI());
  }
}
