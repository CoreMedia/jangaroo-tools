/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import static junit.framework.Assert.assertEquals;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.junit.Test;

import java.io.File;

public class PropertiesFileScannerTest {

  @Test
  public void testScanning() throws Exception {

    LocalizationSuite suite = new LocalizationSuite(new File(getClass().getResource("/").toURI()), null);

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    scanner.scan();

    assertEquals(2, scanner.getSuite().getResourceBundles().size());
    ResourceBundleClass rbc = scanner.getSuite().getResourceBundles().iterator().next();

    assertEquals("PropertiesTest",rbc.getClassName());
    assertEquals("testPackage.PropertiesTest", rbc.getFullClassName());

    assertEquals(4, rbc.getLocales().size());
  }
}
