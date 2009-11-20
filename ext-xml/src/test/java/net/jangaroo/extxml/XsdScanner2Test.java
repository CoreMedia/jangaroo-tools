/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import utils.TestUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class XsdScanner2Test {

  @Test
  public void testSimpleXSD() throws Exception{
    XsdScanner2 scanner = new XsdScanner2();
    File xsd = TestUtils.getFile("/schemas/ext3.xsd", getClass());
    scanner.scan(new FileInputStream(xsd));
    ComponentSuite suite = scanner.getComponentSuite(); 
    assertNotNull(suite);
    ComponentClass cclass = suite.getComponentClassByXtype("container");
    assertNotNull(cclass);

    assertTrue(cclass.getCfgs().size() == 3);
  }
}