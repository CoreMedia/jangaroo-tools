/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;
import utils.TestUtils;

/**
 *
 */
public class ExtComponentSrcFileScannerTest extends TestCase {

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

  public void testJavaScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite();

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/js/test.js", getClass()));
    assertTrue(suite.getComponentClasses().size() == 2);
    ComponentClass cc = suite.getComponentClassByFullClassName("ext.Panel");
    assertNotNull(cc);
    assertEquals(ComponentType.JavaScript, cc.getType());
    assertEquals("panel", cc.getXtype());
    assertEquals("ext.Container", cc.getSuperClassName());
    assertNotNull(cc.getDescription());
    for (ConfigAttribute attr : cc.getCfgs()) {
      assertNotNull(attr.getName());
      assertNotNull(attr.getJsType());
      if(attr.getName().equals("collapsible")) {
        assertEquals("Boolean", attr.getJsType());
        assertEquals("True to make the panel collapsible and have the expand/collapse toggle button automatically rendered into the header tool\n" +
            "      button area, false to keep the panel statically sized with no button (defaults to false).\n" +
            "   ", attr.getDescription());
      }
    }
    cc = suite.getComponentClassByXtype("container");
    assertNotNull(cc);
  }

  public void testXMLComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite("local", TestUtils.getRootDir(getClass()), null);

    ExtComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/testPackage.xml", getClass()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.testPackage");
    assertNotNull(cc);
    assertEquals(ComponentType.XML, cc.getType());
    assertEquals("testPackage", cc.getXtype());
  }
}
