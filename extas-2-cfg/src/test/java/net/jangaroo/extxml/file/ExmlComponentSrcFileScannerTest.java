/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.file;

import static junit.framework.Assert.assertEquals;

import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import utils.TestUtils;

public class ExmlComponentSrcFileScannerTest {
  @Test
  public void testXMLComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite(new ComponentSuiteRegistry(), "local", "", TestUtils.getRootDir(getClass()), null, null);

    ExmlComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/testPackage.exml", getClass()), ComponentType.EXML);
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.testPackage");
    assertNotNull(cc);
    assertEquals(ComponentType.EXML, cc.getType());
    assertEquals("testpackage.testPackage", cc.getXtype());
  }
}
