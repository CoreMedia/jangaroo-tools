/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Locale;

public class PropertyClassGeneratorTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Test
  public void testSimplePropertySet() throws Exception {
    FileSet properties = new FileSet();
    properties.setDirectory(getClass().getResource("/").getPath());
    properties.addInclude("**/*.properties");

    LocalizationSuite suite = new LocalizationSuite(properties, null);

    PropertyClassGenerator generator =  new PropertyClassGenerator(suite);

    StringWriter writer  = new StringWriter();

    ResourceBundleClass rbc = new ResourceBundleClass("testPackage.PropertiesTest");
    PropertiesConfiguration p = new PropertiesConfiguration();
    p.setProperty("key", "Die Platte \"{1}\" enthält {0}.");
    p.setProperty("key2", "Die Platte \"{1}\" enthält {0}.");
    PropertiesClass pc = new PropertiesClass(rbc, Locale.ENGLISH,p, null);
    generator.generatePropertiesClass(pc, writer);
    assertEquals(("package testPackage {\n" +
        "\n" +
        "/**\n" +
        " * Properties class for ResourceBundle \"PropertiesTest\" and Locale \"en\".\n" +
        " * @see PropertiesTest_properties#INSTANCE\n" +
        " */\n" +
        "[ResourceBundle('PropertiesTest_en')]\n" +
        "public class PropertiesTest_properties_en extends PropertiesTest_properties {\n" +
        "\n" +
        "\n" +
        "[Resource(key='key',bundle='PropertiesTest_en')]\n" +
        "public const key:String = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "[Resource(key='key2',bundle='PropertiesTest_en')]\n" +
        "public const key2:String = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR), writer.toString());
  }
}
