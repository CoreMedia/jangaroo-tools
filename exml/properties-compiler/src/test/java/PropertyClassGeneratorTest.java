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
    p.setProperty("key3", "Resource(key='the_other_key'\\, bundle='testPackage.otherpackage.OtherProperties_properties')");
    p.setProperty("key4", "Resource(key='the.other.key'\\, bundle='testPackage.otherpackage.OtherProperties_properties')");
    PropertiesClass pc = new PropertiesClass(rbc, null,p, null);

    generator.generatePropertiesClass(pc, writer);
    assertEquals(("package testPackage {\n" +
      "import joo.ResourceBundleAwareClassLoader;\n" +
      "import joo.JavaScriptObject;\n" +
      "import testPackage.otherpackage.OtherProperties_properties;\n" +
      "\n" +
      "/**\n" +
      " * Properties class for ResourceBundle \"PropertiesTest\".\n" +
      " * @see PropertiesTest_properties#INSTANCE\n" +
      " */\n" +
      "public class PropertiesTest_properties extends joo.JavaScriptObject {\n" +
      "\n" +
      "/**\n" +
      " * Singleton for the current user Locale's instance of ResourceBundle \"PropertiesTest\".\n" +
      " * @see PropertiesTest_properties\n" +
      " */\n" +
      "public static const INSTANCE:PropertiesTest_properties = ResourceBundleAwareClassLoader.INSTANCE.createSingleton(PropertiesTest_properties) as PropertiesTest_properties;\n" +
      "\n" +
      "public native function get key():String;\n" +
      "public native function get key2():String;\n" +
      "public native function get key3():String;\n" +
      "public native function get key4():String;\n" +
      "\n" +
      "public function PropertiesTest_properties() {\n" +
      "  this[\"key\"] = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
      "  this[\"key2\"] = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
      "  this[\"key3\"] = testPackage.otherpackage.OtherProperties_properties.INSTANCE.the_other_key;\n" +
      "  this[\"key4\"] = testPackage.otherpackage.OtherProperties_properties.INSTANCE[\"the.other.key\"];\n" +
      "}\n" +
      "}\n" +
      "}").replaceAll("\n", LINE_SEPARATOR), writer.toString());

    PropertiesClass psc = new PropertiesClass(rbc, Locale.ENGLISH,p, null);

    writer  = new StringWriter();
    generator.generatePropertiesClass(psc, writer);
    assertEquals(("package testPackage {\n" +
        "import testPackage.otherpackage.OtherProperties_properties;\n" +
        "\n" +
        "/**\n" +
        " * Properties class for ResourceBundle \"PropertiesTest\" and Locale \"en\".\n" +
        " * @see PropertiesTest_properties#INSTANCE\n" +
        " */\n" +
        "public class PropertiesTest_properties_en extends PropertiesTest_properties {\n" +
        "\n" +
        "\n" +
        "public function PropertiesTest_properties_en() {\n" +
        "  this[\"key\"] = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "  this[\"key2\"] = \"Die Platte \\\"{1}\\\" enthält {0}.\";\n" +
        "  this[\"key3\"] = testPackage.otherpackage.OtherProperties_properties.INSTANCE.the_other_key;\n" +
        "  this[\"key4\"] = testPackage.otherpackage.OtherProperties_properties.INSTANCE[\"the.other.key\"];\n" +
        "}\n" +
        "}\n" +
        "}").replaceAll("\n", LINE_SEPARATOR), writer.toString());
  }
}
