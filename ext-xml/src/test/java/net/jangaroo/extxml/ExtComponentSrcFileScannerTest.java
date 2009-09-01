/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;

import java.io.File;

/**
 *
 */
public class ExtComponentSrcFileScannerTest extends TestCase {

  public void testActionScriptComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite(new File(""));

    ExtComponentSrcFileScanner.scan(suite, new File(getClass().getResource("/testpackage/SimpleComponent.as").toURI()));
    ComponentClass cc = suite.getComponentClassByClassName("testpackage.SimpleComponent");
    assertNotNull(cc);
    assertEquals("simplecomponent", cc.getXtype());
    assertEquals("ext.Panel", cc.getSuperClassName());
  }
}
