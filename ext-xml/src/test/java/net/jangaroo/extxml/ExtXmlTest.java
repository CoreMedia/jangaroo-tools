/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import utils.TestUtils;

import java.io.File;

/**
 *
 */
public class ExtXmlTest {

  @Test
  public void testMain() throws Exception {
    File outputDir = TestUtils.computeTestDataRoot(getClass());
    File rootDir = TestUtils.getRootDir(getClass());

    String[] args = {"local",
        "ll",
        outputDir.getPath() + "/testComponentSuite.xsd", 
        rootDir.getPath() + "/testComponentSuite/",
        outputDir.getPath()};
    ExtXml.main(args);

    File resultXSD = new File(outputDir, "testComponentSuite.xsd");
    assertTrue(resultXSD.exists());

    File asFile = new File(outputDir, "MyLayout.as");
    assertTrue(asFile.exists());

  }

}
