/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;

import java.io.File;
import java.util.Collection;

/**
 *
 */
public class ExtComponentSrcFileScannerTest extends TestCase {

  public void testActionScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite(new File(""));

    ExtComponentSrcFileScanner.scan(suite, new File(getClass().getResource("/testpackage/SimpleComponent.as").toURI()));
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.SimpleComponent");
    assertNotNull(cc);
    assertEquals("simplecomponent", cc.getXtype());
    assertEquals("ext.Panel", cc.getSuperClassName());
    assertTrue(cc.getImports().contains("ext.Panel"));
    assertTrue(cc.getImports().contains("my.other.Class"));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyOne","Boolean/String")));
    assertTrue(cc.getCfgs().contains(new ConfigAttribute("propertyTwo","Number")));
  }

  public void testJavaScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite(new File(""));

    ExtComponentSrcFileScanner.scan(suite, new File(getClass().getResource("/js/test.js").toURI()));
    assertTrue(suite.getComponentClasses().size() == 2);
    ComponentClass cc = suite.getComponentClassByFullClassName("ext.Panel");
    assertNotNull(cc);
    assertEquals("panel", cc.getXtype());
    assertEquals("ext.Container", cc.getSuperClassName());
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
}
