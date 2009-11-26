/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import net.jangaroo.extxml.file.ExmlComponentSrcFileScanner;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import utils.TestUtils;
import static junit.framework.Assert.assertEquals;

public class ExmlComponentSrcFileScannerTest {
  @Test
  public void testXMLComponent() throws Exception {
    ComponentSuite suite = new ComponentSuite("local", "", TestUtils.getRootDir(getClass()), null);

    ExmlComponentSrcFileScanner.scan(suite, TestUtils.getFile("/testpackage/testPackage.exml", getClass()), ComponentType.EXML);
    ComponentClass cc = suite.getComponentClassByFullClassName("testpackage.testPackage");
    assertNotNull(cc);
    assertEquals(ComponentType.EXML, cc.getType());
    assertEquals("testpackage.testPackage", cc.getXtype());
  }
}
