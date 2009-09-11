/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import utils.TestUtils;

/**
 *
 */
public class ExtComponentSrcFileScannerTest {

  @Test
  public void testActionScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite();

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/SimpleComponent.as", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.SimpleComponent");
    assertNotNull(cc);
    assertEquals(ComponentType.ActionScript, cc.getType());
    assertEquals("simplecomponent", cc.getXtype());
    assertEquals("ext.Panel", cc.getSuperClassName());
    assertTrue(cc.getImports().contains("ext.Panel"));
    assertTrue(cc.getImports().contains("my.other.Class"));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyOne","Boolean/String")));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyTwo","Number")));
  }

  @Test
  public void testJavaScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite();

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/js/test.js", getClass()));
    assertTrue(suite.getComponentClasses().size() == 2);
    ComponentClass cc = suite.getComponentClassByFullClassName("example.Component2");
    assertNotNull(cc);
    assertEquals(ComponentType.JavaScript, cc.getType());
    assertEquals("component2", cc.getXtype());
    assertEquals("example.Component1", cc.getSuperClassName());
    assertNotNull(cc.getDescription());
    for (ConfigAttribute attr : cc.getCfgs()) {
      assertNotNull(attr.getName());
      assertNotNull(attr.getJsType());
      if(attr.getName().equals("attributeTwo")) {
        assertEquals("Boolean", attr.getJsType());
        assertEquals("this is another description", attr.getDescription());
      }
    }
    cc = suite.getComponentClassByXtype("component2");
    assertNotNull(cc);
  }

  @Test
  public void testXMLComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite("local", "", TestUtils.getRootDir(getClass()), null);

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/testPackage.xml", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.testPackage");
    assertNotNull(cc);
    assertEquals(ComponentType.XML, cc.getType());
    assertEquals("testPackage", cc.getXtype());
  }
}
