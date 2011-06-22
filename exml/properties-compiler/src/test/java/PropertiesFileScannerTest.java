/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import static junit.framework.Assert.assertEquals;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;

import java.io.File;

public class PropertiesFileScannerTest {

  @Test
  public void testScanning() throws Exception {

    FileSet properties = new FileSet();
    properties.setDirectory(getClass().getResource("/").getPath());
    properties.addInclude("**/*.properties");
    
    LocalizationSuite suite = new LocalizationSuite(properties, null);

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    scanner.scan();

    assertEquals(2, scanner.getSuite().getResourceBundles().size());
    ResourceBundleClass rbc = scanner.getSuite().getResourceBundles().iterator().next();

    assertEquals("PropertiesTest",rbc.getClassName());
    assertEquals("testPackage.PropertiesTest", rbc.getFullClassName());

    assertEquals(4, rbc.getLocales().size());
  }
}
