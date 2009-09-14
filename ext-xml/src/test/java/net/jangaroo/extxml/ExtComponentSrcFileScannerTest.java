/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import utils.TestUtils;

/**
 *
 */
public class ExtComponentSrcFileScannerTest {

  @Test
  public void actionScriptComponentWithXTypeComment() throws Exception {
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
  public void actionScriptComponentOnlyWithXTypeConstant() throws Exception {
    ComponentSuite suite = new ComponentSuite();

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/SimpleComponent2.as", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.SimpleComponent2");
    assertNotNull(cc);
    assertEquals(ComponentType.ActionScript, cc.getType());
    assertEquals("SimpleComponent2", cc.getXtype());
    assertEquals("ext.Panel", cc.getSuperClassName());
    assertTrue(cc.getImports().contains("ext.Panel"));
    assertTrue(cc.getImports().contains("my.other.Class"));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyOne","Boolean/String")));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyTwo","Number")));
  }

  @Test
  public void pluginWithCommentAndConstantPType() throws Exception {
    ComponentSuite suite = new ComponentSuite();

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/SimplePlugin.as", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.SimplePlugin");
    assertNotNull(cc);
    assertEquals(ComponentType.ActionScript, cc.getType());
    assertEquals("SimplePlugin", cc.getXtype());
    assertNull("Has no superclass", cc.getSuperClassName());
    assertTrue(cc.getImports().contains("ext.Plugin"));
    assertTrue(cc.getImports().contains("my.other.Class"));
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

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/testPackage.exml", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.testPackage");
    assertNotNull(cc);
    assertEquals(ComponentType.EXML, cc.getType());
    assertEquals("testPackage", cc.getXtype());
  }
}
