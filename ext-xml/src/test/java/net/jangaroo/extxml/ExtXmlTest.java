/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import junit.framework.TestCase;

import java.io.File;
import java.net.URL;

/**
 *
 */
public class ExtXmlTest extends TestCase {

  public void testMain() throws Exception {
    File outputDir = computeTestDataRoot(getClass());
    outputDir.mkdir();

    File rootDir = new File(getClass().getResource("/").toURI());

    String[] args = {"local", outputDir.getPath()+"/testComponentSuite.xsd", rootDir.getPath()+"/testComponentSuite/", outputDir.getPath()};
    ExtXml.main(args);

    File resultXSD = new File(outputDir, "testComponentSuite.xsd");
    assertTrue(resultXSD.exists());

    File asFile = new File(outputDir, "MyLayout.as");
    assertTrue(asFile.exists());

  }

  public static File computeTestDataRoot(Class anyTestClass) {
    final String clsUri = anyTestClass.getName().replace('.','/') + ".class";
    final URL url = anyTestClass.getClassLoader().getResource(clsUri);
    final String clsPath = url.getPath();
    final File root = new File(clsPath.substring(0, clsPath.length() - clsUri.length()));
    final File clsFile = new File(root, clsUri);
    return new File(root.getParentFile(), "test-data");
  }
}
