/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import utils.TestUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class XsdScannerTest {

  @Test
  public void testSimpleXSD() throws Exception{
    XsdScanner scanner = new XsdScanner();
    File xsd = TestUtils.getFile("/schemas/ext3.xsd", getClass());
    ComponentSuite suite = scanner.scan(new FileInputStream(xsd));
    assertNotNull(suite);
    assertEquals("ext",suite.getNs());
    ComponentClass cclass = suite.getComponentClassByXtype("container");
    assertNotNull(cclass);
  }
}
