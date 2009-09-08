/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;
import utils.TestUtils;

import java.io.File;

/**
 *
 */
public class ExtXmlTest extends TestCase {

  public void testMain() throws Exception {
    File outputDir = TestUtils.computeTestDataRoot(getClass());
    File rootDir = TestUtils.getRootDir(getClass());

    String[] args = {"local", outputDir.getPath() + "/testComponentSuite.xsd", rootDir.getPath() + "/testComponentSuite/", outputDir.getPath()};
    ExtXml.main(args);

    File resultXSD = new File(outputDir, "testComponentSuite.xsd");
    assertTrue(resultXSD.exists());

    File asFile = new File(outputDir, "MyLayout.as");
    assertTrue(asFile.exists());

  }

}
