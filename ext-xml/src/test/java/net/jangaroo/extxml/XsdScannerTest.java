/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import utils.TestUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class XsdScannerTest {

  @Test
  public void testSimpleXSD() throws Exception{
    XsdScanner scanner = new XsdScanner(new ComponentSuiteRegistry());
    File xsd = TestUtils.getFile("/schemas/ext3.xsd", getClass());
    ComponentSuite suite = scanner.scan(new FileInputStream(xsd));
    assertNotNull(suite);
    ComponentClass cclass = suite.getComponentClassByXtype("container");
    assertNotNull(cclass);
  }
}
