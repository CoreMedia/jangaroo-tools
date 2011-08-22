/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.compiler.PropertiesCompiler;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

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
    PropertiesCompiler.main(new String[]{root.getAbsolutePath(), out.getAbsolutePath()});

    File defaultProp = new File(out,"testPackage/PropertiesTest_properties.as");
    assertTrue(defaultProp.exists());
    assertTrue(defaultProp.length() > 100);

    File deProp = new File(out, "testPackage/PropertiesTest_properties_de.as");

    assertTrue(deProp.exists());
    assertTrue(deProp.length() > 100);

    File it_VA_WINProp = new File(out, "testPackage/PropertiesTest_properties_it_VA_WIN.as");
    assertTrue(it_VA_WINProp.exists());
    assertTrue(it_VA_WINProp.length() > 100);

    File es_ESNProp = new File(out, "testPackage/PropertiesTest_properties_es_ES.as");
    assertTrue(es_ESNProp.exists());
    assertTrue(es_ESNProp.length() > 100);    


    File subPackageProp = new File(out, "testPackage/subPackage/Proberties_properties.as");
    assertTrue(subPackageProp.exists());
    assertTrue(subPackageProp.length() > 100);
  }
}
