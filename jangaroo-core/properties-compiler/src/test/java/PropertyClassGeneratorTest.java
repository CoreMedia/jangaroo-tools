/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Properties;

public class PropertyClassGeneratorTest {

  @Test
  public void testSimplePropertySet() throws Exception {

    LocalizationSuite suite = new LocalizationSuite(new File(getClass().getResource("/").toURI()), null);

    PropertyClassGenerator generator =  new PropertyClassGenerator(suite);

    StringWriter writer  = new StringWriter();

    ResourceBundleClass rbc = new ResourceBundleClass("testPackage.PropertiesTest");
    Properties p = new Properties();
    p.put("key", "Die Platte \\\"{1}\\\" enthält {0}.");
    p.put("key2", "Die Platte \\\"{1}\\\" enthält {0}.");
    PropertiesClass pc = new PropertiesClass(rbc, Locale.ENGLISH,p, null);
    generator.generatePropertiesClass(pc, writer);
    System.out.println(writer.toString());
  }

 
}
