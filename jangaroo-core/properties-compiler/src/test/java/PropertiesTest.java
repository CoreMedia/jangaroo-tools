/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import org.junit.Test;

import java.io.File;
import java.net.URL;

public class PropertiesTest {

   public static File computeTestDataRoot(Class anyTestClass) {
    final String clsUri = anyTestClass.getName().replace('.','/') + ".class";
    final URL url = anyTestClass.getClassLoader().getResource(clsUri);
    final String clsPath = url.getPath();
    final File root = new File(clsPath.substring(0, clsPath.length() - clsUri.length()));
    final File result = new File(root.getParentFile(), "test-data");
    result.mkdir();
    return result;
  }
  
  @Test
  public void testMain() throws Exception {
    File out = computeTestDataRoot(getClass());
    File root = new File(getClass().getResource("/").toURI());

    Properties.main(new String[] {root.getAbsolutePath(), out.getAbsolutePath()});
  }
}
