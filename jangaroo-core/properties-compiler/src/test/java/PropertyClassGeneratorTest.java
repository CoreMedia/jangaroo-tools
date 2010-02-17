/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Properties;

public class PropertyClassGeneratorTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Test
  public void testSimplePropertySet() throws Exception {

    LocalizationSuite suite = new LocalizationSuite(new File(getClass().getResource("/").toURI()), null);

    PropertyClassGenerator generator =  new PropertyClassGenerator(suite);

    StringWriter writer  = new StringWriter();

    ResourceBundleClass rbc = new ResourceBundleClass("testPackage.PropertiesTest");
    Properties p = new Properties();
    p.put("key", "Die Platte \"{1}\" enthält {0}.");
    p.put("key2", "Die Platte \"{1}\" enthält {0}.");
    PropertiesClass pc = new PropertiesClass(rbc, Locale.ENGLISH,p, null);
    generator.generatePropertiesClass(pc, writer);
    assertEquals(("package testPackage {\n" +
        "\n" +
        "/**\n" +
        " * Properties class for Locale en\n" +
        " */\n" +
        "public class PropertiesTest_properties {\n" +
        "\n" +
        "public static const key:String = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "public static const key2:String = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR), writer.toString());
  }

 
}
