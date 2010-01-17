package net.jangaroo.extxml.generation;

import net.jangaroo.extxml.json.JsonObject;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import net.jangaroo.extxml.model.ConfigAttribute;
import net.jangaroo.utils.log.Log;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;
import utils.UnitTestLogHandler;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;


public class JooClassGeneratorTest{

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  
  private JooClassGenerator jooClassGenerator = null;
  private UnitTestLogHandler logHandler;

  @Before
  public void initialize() throws Exception {
    logHandler = new UnitTestLogHandler();
    Log.setLogHandler(logHandler);
    jooClassGenerator = new JooClassGenerator(new ComponentSuite("test", "test", null, null));
  }

  @After
  public void checkExpectedErrors() {
    logHandler.checkExpectedErrors();

  }

  @Test
  public void testGenerateJangarooClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass",null);
    jooClazz.setDescription("My Description");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals(("package com.coremedia.test {\n" +
        "\n" +
        "import ext.Ext;\n" +
        "import ext.ComponentMgr;\n" +
        "\n" +
        "/**\n" +
        " * My Description\n" +
        " *\n" +
        " * <b>Do not edit. this is an auto-generated class.</b>\n" +
        " */\n" +
        "public class TestClass extends SuperClass {\n" +
        "\n" +
        "  public static const xtype:String = \"com.coremedia.test.TestClass\";\n" +
        "{\n" +
        "  ext.ComponentMgr.registerType(xtype, TestClass);\n" +
        "}\n" +
        "\n" +
        "  /**\n" +
        "   *\n" +
        "   * @see TestClass \n" +
        "   */\n" +
        "  public function TestClass(config:* = {}) {\n" +
        "    super(Ext.apply(config, {}));\n" +
        "  }\n" +
        "\n" +
        "  public static function main(config:* = {}):void {\n" +
        "    new com.coremedia.test.TestClass(config);\n" +
        "  }\n" +
        "\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR), writer.toString());
    writer.close();
  }

  @Test
  public void testGenerateJangarooClassWithJson() throws Exception {
    JsonObject start = new JsonObject();
    start.set("json", "{somestuff}");
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass",start);
    jooClazz.setDescription("My Description");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals(("package com.coremedia.test {\n" +
        "\n" +
        "import ext.Ext;\n" +
        "import ext.ComponentMgr;\n" +
        "\n" +
        "/**\n" +
        " * My Description\n" +
        " *\n" +
        " * <b>Do not edit. this is an auto-generated class.</b>\n" +
        " */\n" +
        "public class TestClass extends SuperClass {\n" +
        "\n" +
        "  public static const xtype:String = \"com.coremedia.test.TestClass\";\n" +
        "{\n" +
        "  ext.ComponentMgr.registerType(xtype, TestClass);\n" +
        "}\n" +
        "\n" +
        "  /**\n" +
        "   *\n" +
        "   * @see TestClass \n" +
        "   */\n" +
        "  public function TestClass(config:* = {}) {\n" +
        "    super(Ext.apply(config, {json: somestuff}));\n" +
        "  }\n" +
        "\n" +
        "  public static function main(config:* = {}):void {\n" +
        "    new com.coremedia.test.TestClass(config);\n" +
        "  }\n" +
        "\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR),writer.toString());
    writer.close();
  }

  @Test
  public void testGenerateJangarooClassWithAttributes() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass",null);
    jooClazz.addCfg(new ConfigAttribute("property", "Number"));
    jooClazz.addCfg(new ConfigAttribute("property2", "Number/String"));
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals(("package com.coremedia.test {\n" +
        "\n" +
        "import ext.Ext;\n" +
        "import ext.ComponentMgr;\n" +
        "\n" +
        "/**\n" +
        " * \n" +
        " *\n" +
        " * <b>Do not edit. this is an auto-generated class.</b>\n" +
        " */\n" +
        "public class TestClass extends SuperClass {\n" +
        "\n" +
        "  public static const xtype:String = \"com.coremedia.test.TestClass\";\n" +
        "{\n" +
        "  ext.ComponentMgr.registerType(xtype, TestClass);\n" +
        "}\n" +
        "\n" +
        "  /**\n" +
        "   * @cfg {Number} property\n" +
        "   * @cfg {String} property2\n" +
        "   *\n" +
        "   * @see TestClass \n" +
        "   */\n" +
        "  public function TestClass(config:* = {}) {\n" +
        "    super(Ext.apply(config, {}));\n" +
        "  }\n" +
        "\n" +
        "  public static function main(config:* = {}):void {\n" +
        "    new com.coremedia.test.TestClass(config);\n" +
        "  }\n" +
        "\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR),writer.toString());
    writer.close();
  }

  @Test
  public void classWithoutSuperClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass",null,null);
    StringWriter writer = new StringWriter();
    logHandler.expectedErrorMessage = "Super class of component 'com.coremedia.test.TestClass' is undefined!";
    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    assertEquals("",writer.toString());
    writer.close();
  }

  @Test
  public void testGenerateClasses() throws Exception {
    File rootDir = TestUtils.getRootDir(getClass());
    File outDir = TestUtils.computeTestDataRoot(getClass());

    ComponentSuite extSuite = new ComponentSuite("http://extjs.com/ext3", "ext",null, null);
    ComponentClass panel = new ComponentClass("panel", "ext.Panel");
    ComponentClass label = new ComponentClass("label", "ext.form.Label");
    extSuite.addComponentClass(panel);
    extSuite.addComponentClass(label);
    
    ComponentSuite suite = new ComponentSuite("local", "", rootDir, outDir);
    
    ComponentClass cc = new ComponentClass(TestUtils.getFile("/testpackage/testPackage.exml", getClass()));
    cc.setType(ComponentType.EXML);
    cc.setFullClassName("testpackage.testPackage");
    cc.setXtype("testPackage");
    cc.setSuperClassName("ext.Panel");
    suite.addComponentClass(cc);

    JooClassGenerator generator = new JooClassGenerator(suite);
    generator.generateClasses();

    assertEquals("{title:\"Iaminsideapackage!\",items:{xtype:\"label\"}}",cc.getJson().toString(0,0).replaceAll("\\s",""));
    assertEquals("ext.Panel",cc.getSuperClassName());

    File result = new File(outDir, "testpackage/testPackage.as");
    assertTrue(result.exists());

  }

}
