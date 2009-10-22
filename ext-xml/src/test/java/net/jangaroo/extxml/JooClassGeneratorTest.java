package net.jangaroo.extxml;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;
import utils.UnitTestErrorHandler;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;


public class JooClassGeneratorTest{

  private JooClassGenerator jooClassGenerator = null;
  private UnitTestErrorHandler errorHandler;

  @Before
  public void initialize() throws Exception {
    errorHandler = new UnitTestErrorHandler();
    jooClassGenerator = new JooClassGenerator(new ComponentSuite(new ComponentSuiteRegistry(), "test", "test", null, null), errorHandler);
  }

  @After
  public void checkExpectedErrors() {
    errorHandler.checkExpectedErrors();

  }

  @Test
  public void testGenerateJangarooClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass","json");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals("package com.coremedia.test {import Ext;import ext.ComponentMgr;/** * Do not edit. this is an auto-generated class. * @xtype com.coremedia.test.TestClass */public class TestClass extends SuperClass {  public static const xtype:String = \"com.coremedia.test.TestClass\";{  ext.ComponentMgr.registerType(xtype, TestClass);}  /**   */  public function TestClass(config:* = {}) {    super(Ext.apply(config, json));  }  public static function main(config:* = {}):void {    new com.coremedia.test.TestClass(config);  }}}",writer.toString().replaceAll("\r|\n",""));
    writer.close();
  }

  @Test
  public void testGenerateJangarooClassWithAttributes() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass","json");
    jooClazz.addCfg(new ConfigAttribute("property", "Number"));
    jooClazz.addCfg(new ConfigAttribute("property2", "Number/String"));
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals("package com.coremedia.test {import Ext;import ext.ComponentMgr;/** * Do not edit. this is an auto-generated class. * @xtype com.coremedia.test.TestClass */public class TestClass extends SuperClass {  public static const xtype:String = \"com.coremedia.test.TestClass\";{  ext.ComponentMgr.registerType(xtype, TestClass);}  /**   * @cfg {Number} property   * @cfg {String} property2   */  public function TestClass(config:* = {}) {    super(Ext.apply(config, json));  }  public static function main(config:* = {}):void {    new com.coremedia.test.TestClass(config);  }}}",writer.toString().replaceAll("\r|\n",""));
    writer.close();
  }

  @Test
  public void classWithoutSuperClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass",null,"json");
    StringWriter writer = new StringWriter();
    errorHandler.expectedErrorMessage = "Super class of component 'com.coremedia.test.TestClass' is undefined!";
    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    assertEquals("",writer.toString());
    writer.close();
  }

  @Test
  public void testGenerateClasses() throws Exception {
    File rootDir = TestUtils.getRootDir(getClass());
    File outDir = TestUtils.computeTestDataRoot(getClass());
    ComponentSuiteRegistry reg = new ComponentSuiteRegistry();

    ComponentSuite extSuite = new ComponentSuite(reg, "http://extjs.com/ext3", "ext",null, null);
    ComponentClass panel = new ComponentClass("panel", "ext.Panel");
    ComponentClass label = new ComponentClass("label", "ext.form.Label");
    extSuite.addComponentClass(panel);
    extSuite.addComponentClass(label);
    
    ComponentSuite suite = new ComponentSuite(reg, "local", "", rootDir, outDir);
    
    ComponentClass cc = new ComponentClass(TestUtils.getFile("/testpackage/testPackage.exml", getClass()));
    cc.setType(ComponentType.EXML);
    cc.setFullClassName("testpackage.testPackage");
    cc.setXtype("testPackage");
    suite.addComponentClass(cc);

    JooClassGenerator generator = new JooClassGenerator(suite, errorHandler);
    generator.generateClasses();

    assertEquals("{title:\"Iaminsideapackage!\",items:{xtype:ext.form.Label.xtype}}",cc.getJson().replaceAll("\\s",""));
    assertEquals("ext.Panel",cc.getSuperClassName());

    File result = new File(outDir, "testpackage/testPackage.as");
    assertTrue(result.exists());

  }

}
