package net.jangaroo.extxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;


public class JooClassGeneratorTest{

  private JooClassGenerator jooClassGenerator = null;

  @Before
  public void initialize() throws Exception {
    jooClassGenerator = new JooClassGenerator(new ComponentSuite(), new StandardOutErrorHandler());
  }

  @Test
  public void testGenerateJangarooClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass","json");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals("package com.coremedia.test {import ext.ComponentMgr;/** * @xtype com.coremedia.test.TestClass */public class TestClass extends SuperClass {  public static const xtype:String = \"com.coremedia.test.TestClass\";{  ext.ComponentMgr.registerType(xtype, TestClass);}  public function TestClass(config:* = {}) {    super(Ext.apply(config, json));  }}}",writer.toString().replaceAll("\r|\n",""));
    writer.close();
  }

  @Test
  public void classWithoutSuperClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass",null,"json");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    assertEquals("",writer.toString());
    writer.close();
  }

  @Test
  public void testGenerateClasses() throws Exception {
    File rootDir = TestUtils.getRootDir(getClass());
    File outDir = TestUtils.computeTestDataRoot(getClass());
    
    ComponentSuite suite = new ComponentSuite("local", "", rootDir, outDir);
    ComponentClass panel = new ComponentClass("panel", "ext.Panel");
    ComponentClass label = new ComponentClass("label", "ext.Label");
    suite.addComponentClass(panel);
    suite.addComponentClass(label);
    
    ComponentClass cc = new ComponentClass(TestUtils.getFile("/testpackage/testPackage.exml", getClass()));
    cc.setType(ComponentType.EXML);
    cc.setFullClassName("testpackage.testPackage");
    cc.setXtype("testPackage");
    suite.addComponentClass(cc);

    JooClassGenerator generator = new JooClassGenerator(suite, new StandardOutErrorHandler());
    generator.generateClasses();

    assertEquals("{title:\"Iaminsideapackage!\",items:{xtype:ext.Label.xtype}}",cc.getJson().replaceAll("\\s",""));
    assertEquals("ext.Panel",cc.getSuperClassName());

    File result = new File(outDir, "testpackage/testPackage.as");
    assertTrue(result.exists());

  }

}
