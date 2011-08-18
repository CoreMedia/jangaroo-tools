/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.xml;

import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    ComponentClass cclass = suite.getComponentClassByXtype("component");
    assertNotNull(cclass);

    assertEquals("ext.Component", cclass.getFullClassName());
    assertNotNull(cclass.getDescription());
    assertFalse(cclass.getCfgs().isEmpty());

    ComponentClass container = suite.getComponentClassByFullClassName("ext.Container");
    assertEquals("container", container.getXtype());
    assertEquals("ext.BoxComponent", container.getSuperClassName());
    assertEquals(18, container.getCfgs().size());
    assertNotNull(container.getCfgs().iterator().next().getDescription());

    ComponentClass gridPanel = suite.getComponentClassByXtype("grid");
    ComponentClass panel = gridPanel.getSuperClass();
    assertEquals(container, panel.getSuperClass());
    //should also be the same class.
    assertTrue(container == panel.getSuperClass());
  }
}
