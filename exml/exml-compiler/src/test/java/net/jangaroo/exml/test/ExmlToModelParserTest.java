package net.jangaroo.exml.test;

import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToModelParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ExmlToModelParserTest extends AbstractExmlTest {
  @Test
  public void testParseAllElements() throws Exception {
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/AllElements.exml"));
    Assert.assertEquals(new HashSet<String>(Arrays.asList("exmlparser.config.allElements", "ext.config.component", "ext.MessageBox", "ext.Panel")),
            model.getImports());
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    final List<Declaration> varDeclarations = model.getVars();
    Assert.assertEquals(3, varDeclarations.size());

    final Declaration myVar = varDeclarations.get(0);
    Assert.assertEquals("myVar", myVar.getName());
    Assert.assertEquals("String", myVar.getType());
    Assert.assertEquals("config.myProperty + '_suffix'", myVar.getValue());

    final Declaration myVar2 = varDeclarations.get(1);
    Assert.assertEquals("myVar2", myVar2.getName());
    Assert.assertEquals("Object", myVar2.getType());
    Assert.assertEquals("{\n" +
            "      prop: config.myProperty\n" +
            "    }", myVar2.getValue().replaceAll(System.getProperty("line.separator"), "\n"));

    final Declaration myVar3 = varDeclarations.get(2);
    Assert.assertEquals("myVar3", myVar3.getName());
    Assert.assertEquals("ext.config.component", myVar3.getType());
    Assert.assertEquals("ext.config.component({\n" +
            "      xtype: \"button\",\n" +
            "      text: \"Foo\"\n" +
            "    })", myVar3.getValue().replaceAll(System.getProperty("line.separator"), "\n"));

    final List<Declaration> constantDeclarations = model.getConfigClass().getConstants();
    Assert.assertEquals(3, constantDeclarations.size());

    final Declaration myConst1 = constantDeclarations.get(0);
    Assert.assertEquals("SOME_CONSTANT", myConst1.getName());
    Assert.assertEquals("1234", myConst1.getValue());
    Assert.assertEquals("uint", myConst1.getType());
    Assert.assertEquals("This is my <b>constant</b>", myConst1.getDescription().trim());

    final Declaration myConst2 = constantDeclarations.get(1);
    Assert.assertEquals("ANOTHER_CONSTANT", myConst2.getName());
    Assert.assertEquals("\"\\n      Lorem ipsum & Co.\\n      Another line.\\n    \"", myConst2.getValue());
    Assert.assertEquals("String", myConst2.getType());
    Assert.assertEquals("This is another <b>constant</b>", myConst2.getDescription().trim());

    final Declaration myConst3 = constantDeclarations.get(2);
    Assert.assertEquals("CODE_CONSTANT", myConst3.getName());
    Assert.assertEquals("1 + 1", myConst3.getValue());
    Assert.assertEquals("int", myConst3.getType());

    JsonObject expectedJsonObject = new JsonObject(
            "layout", JsonObject.code("config.myLayout"),
            "title", "I am a panel",
            "someList", new JsonArray(
              new JsonObject(
                "xtype", "button",
                "text", "click me!"
              )
            ),
            "defaults", new JsonObject("layout","border"),
            "layoutConfig", new JsonObject(
                    "bla", "blub",
                    "anchor", new JsonObject("style", "test"),
                    "border", new JsonObject("type", "solid")
            ),
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "button",
                            "text", "Save",
                            "handler", JsonObject.code("function():void {\n" +
                            "          window.alert('gotcha!');\n" +
                            "        }")
                    )
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
                            "handler", JsonObject.code("function(x){return ''+x;}"),
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
            "items", JsonObject.code("config.myItems"),
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
            "baseAction", JsonObject.code("net.jangaroo.ext.create(ext.config.action,{disabled: false})")
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
  public void testParseTyped() throws Exception{
    setUp("exmlparser.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/TestTyped.exml"));
    Assert.assertEquals("ext.Panel", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "xtype", "component",
                            "margins", "5" // not the number 5!
                    ),
                    new JsonObject(
                            "xtype", "component",
                            "id", "false"  // not the boolean false!
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
  public void testConfigDefaultValues() throws Exception {
    setUp("testNamespace.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/testPackage/TestComponentWithCfgDefaults.exml"));
    List<ConfigAttribute> cfgs = model.getConfigClass().getDirectCfgs();
    Assert.assertEquals(8, cfgs.size());

    JsonObject expectedJsonObject = new JsonObject(
            "propertyWithLiteralDefault", "foobar",
            "propertyWithExpressionDefault", JsonObject.code("'foo' + 'bar'"),
            "propertyWithDefaultElement",
              new JsonObject(
                      "xtype", "button",
                      "text", "click me!"
              ),
            "propertyWithDefaultElementUsingConfig",
              new JsonObject(
                      "xtype", "button",
                      "text", JsonObject.code("config.title + '!'")
              ),
            "arrayPropertyWithDefaultElement",
              new JsonArray(
                      new JsonObject(
                              "xtype", "button",
                              "text", "button1"
                      ),
                      new JsonObject(
                              "xtype", "button",
                              "text", "button2"
                      )
              ),
            "propertyWithInterfaceAndDefault",
              JsonObject.code(" new TestImpl() ")
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getCfgDefaults().toString(2));
  }

  @Test
  public void testContainerDefaults() throws Exception {
    setUp("testNamespace.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/testPackage/TestContainerDefaults.exml"));
    JsonObject expectedJsonObject = new JsonObject(
            "defaults", new JsonObject(
              "text", "it works!"
            ),
            "defaultType", "button",
            "items", new JsonArray(new JsonObject(
              "xtype", "container",
              "defaults", new JsonObject(
                "propertyOne", true
              ),
              "defaultType", "testNamespace.config.testComponent"
            ))
    );
    System.out.println(model.getJsonObject().toString(2));
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }

  @Test
  public void testConfigModes() throws Exception{
    setUp("testNamespace.config");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());

    ExmlModel model = exmlToModelParser.parse(getFile("/testPackage/TestComponentWithConfigModes.exml"));
    Assert.assertEquals("testPackage.TestComponent", model.getSuperClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "items", new JsonArray(
                    new JsonObject(
                            "propertyThree", "3"
                    ).settingWrapperClass("testNamespace.config.testComponent2")
            ),
            "items$at", JsonObject.code("net.jangaroo.ext.Exml.APPEND"),
            "propertyFive", new JsonArray(new JsonObject("xtype", "agridcolumn")),
            "propertyFive$at", JsonObject.code("net.jangaroo.ext.Exml.PREPEND"),
            "layoutConfig", new JsonObject(
                    "mode", "foo"
            )
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
    Assert.assertEquals("This is some constant", aConstant.getDescription().trim());

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
    return new File(ExmlToModelParserTest.class.getResource("/test-module" + path).toURI());
  }
}
