package utils;/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import java.io.File;
import java.net.URL;
import java.net.URISyntaxException;

/**
 *
 */
public class TestUtils {

  public static File computeTestDataRoot(Class anyTestClass) {
    final String clsUri = anyTestClass.getName().replace('.','/') + ".class";
    final URL url = anyTestClass.getClassLoader().getResource(clsUri);
    final String clsPath = url.getPath();
    final File root = new File(clsPath.substring(0, clsPath.length() - clsUri.length()));
    final File result = new File(root.getParentFile(), "test-data");
    result.mkdir();
    return result;
  }

  public static File getRootDir(Class anyTestClass) throws URISyntaxException {
    return new File(anyTestClass.getResource("/").toURI());
  }

  public static File getFile(String path, Class anyTestClass) throws URISyntaxException {
    return new File(anyTestClass.getResource(path).toURI());
  }
}
