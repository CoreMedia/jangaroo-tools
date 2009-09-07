/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import utils.TestUtils;

import java.io.FileInputStream;

/**
 *
 */
public class XmlToJsonHandlerTest extends TestCase {

  public void testParsing() throws Exception {
    XMLReader xr = XMLReaderFactory.createXMLReader();
    XmlToJsonHandler handler = new XmlToJsonHandler(new ComponentSuite());
    xr.setContentHandler(handler);
    xr.parse(new InputSource(new FileInputStream(TestUtils.getFile("/TestComponent.xml", getClass()))));
    System.out.println(handler.getJSON());
  }
}
