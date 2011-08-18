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
    File extXsd = TestUtils.getFile("/schemas/ext3.xsd", getClass());

    String[] args = {
            rootDir.getPath() + "/testComponentSuite/",
            outputDir.getPath(),
            "testComponentSuite.config",
            extXsd.getAbsolutePath(), "ext.config"};
    ExtAsToConfigClassConverter.main(args);

    File configFile = new File(outputDir, "testComponentSuite/config/mypanel.as");
    assertTrue(configFile.exists());
    assertTrue(configFile.length() > 100);

  }

}
