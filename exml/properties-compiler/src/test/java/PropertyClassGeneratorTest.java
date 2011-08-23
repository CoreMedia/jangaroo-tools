/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import net.jangaroo.utils.FileLocations;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PropertyClassGeneratorTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Test
  public void testSimplePropertySet() throws Exception {

    FileLocations locations = new FileLocations();

    List<File> sourcePath = new ArrayList<File>();
    sourcePath.add(new File(getClass().getResource("/").toURI()));
    locations.setSourcePath(sourcePath);

    locations.addSourceFile("testPackage/subPackage/Properties.properties");
    locations.addSourceFile("testPackage/PropertiesTest.properties");
    locations.addSourceFile("testPackage/PropertiesTest_de.properties");
    locations.addSourceFile("testPackage/PropertiesTest_es_ES.properties");
    locations.addSourceFile("testPackage/PropertiesTest_it_VA_WIN.properties");

    PropertyClassGenerator generator =  new PropertyClassGenerator(locations);

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
