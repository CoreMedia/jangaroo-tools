/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.api.PropertiesCompilerConfiguration;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class PropertyClassGeneratorTest {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Test
  public void testSimplePropertySet() throws Exception {

    PropertiesCompilerConfiguration config = new PropertiesCompilerConfiguration();

    List<File> sourcePath = new ArrayList<File>();
    sourcePath.add(new File(getClass().getResource("/").toURI()));
    config.setSourcePath(sourcePath);

    config.addSourceFile("testPackage/subPackage/Properties.properties");
    config.addSourceFile("testPackage/PropertiesTest.properties");
    config.addSourceFile("testPackage/PropertiesTest_de.properties");
    config.addSourceFile("testPackage/PropertiesTest_es_ES.properties");
    config.addSourceFile("testPackage/PropertiesTest_it_VA_WIN.properties");

    PropertyClassGenerator generator =  new PropertyClassGenerator(config);

    StringWriter writer  = new StringWriter();

    ResourceBundleClass rbc = new ResourceBundleClass("testPackage.PropertiesTest_properties");
    PropertiesConfiguration p = new PropertiesConfiguration();
    p.getLayout().setHeaderComment("# some comment \n# \t [PublicApi]".replaceAll("\n", LINE_SEPARATOR));
    p.setProperty("key", "Die Platte \"{1}\" enthält {0}.");
    p.setProperty("key2", "Resource(key='someKey'\\, bundle='net.jangaroo.icons.SomeBundle')");
    p.setProperty("key3", "Resource(key='someOtherKey'\\, bundle='net.jangaroo.SomeOtherBundle')");
    PropertiesClass pc = new PropertiesClass(rbc, null,p);

    generator.generatePropertiesClass(pc, writer, false);
    assertEquals((
            "/**\n" +
                    " * some comment \n" +
                    " * \t [PublicApi]\n" +
                    "*/\n" +
                    "Ext.define(\"testPackage.PropertiesTest_properties\", {\n" +
                    "  \n" +
                    "  requires: [\n" +
                    "    \"net.jangaroo.icons.SomeBundle_properties\",\n" +
                    "    \"net.jangaroo.SomeOtherBundle_properties\"\n" +
                    "  ],\n" +
                    "  \"key\": \"Die Platte \\\"{1}\\\" enthält {0}.\"\n" +
                    "}, function() {\n" +
                    "  this.prototype[\"key2\"] =  net.jangaroo.icons.SomeBundle_properties.INSTANCE.someKey;\n" +
                    "  this.prototype[\"key3\"] =  net.jangaroo.SomeOtherBundle_properties.INSTANCE.someOtherKey;\n" +
                    "\n" +
                    "  testPackage.PropertiesTest_properties.INSTANCE = new testPackage.PropertiesTest_properties();\n" +
                    "});"
            ).replaceAll("\n", LINE_SEPARATOR), writer.toString());

    writer  = new StringWriter();
    generator.generatePropertiesClass(pc, writer, true);
    assertEquals((
            "package testPackage {\n" +
                    "\n" +
                    "/**\n" +
                    " * some comment \n" +
                    "*/ [PublicApi] /*\n" +
                    " * @see PropertiesTest_properties#INSTANCE\n" +
                    " */\n" +
                    "[Native(\"testPackage.PropertiesTest_properties\", require)]\n" +
                    "public class PropertiesTest_properties {\n" +
                    "\n" +
                    "/**\n" +
                    " * Singleton for the current user Locale's instance of ResourceBundle \"PropertiesTest\".\n" +
                    " * @see PropertiesTest_properties\n" +
                    " */\n" +
                    "public static const INSTANCE:PropertiesTest_properties;\n" +
                    "\n" +
                    "public native function get key():String;\n" +
                    "public native function get key2():String;\n" +
                    "public native function get key3():String;\n" +
                    "\n" +
                    "}\n" +
                    "}"
            ).replaceAll("\n", LINE_SEPARATOR), writer.toString());


    PropertiesClass psc = new PropertiesClass(rbc, Locale.ENGLISH,p);
    writer  = new StringWriter();
    generator.generatePropertiesClass(psc, writer, false);

    assertEquals(("/**\n" +
            " * some comment \n" +
            " * \t [PublicApi]\n" +
            "*/\n" +
            "Ext.define(\"testPackage.PropertiesTest_properties_en\", {\n" +
            "  override: \"testPackage.PropertiesTest_properties\",\n" +
            "  requires: [\n" +
            "    \"net.jangaroo.icons.SomeBundle_properties\",\n" +
            "    \"net.jangaroo.SomeOtherBundle_properties\"\n" +
            "  ],\n" +
            "  \"key\": \"Die Platte \\\"{1}\\\" enthält {0}.\"\n" +
            "}, function() {\n" +
            "  this.prototype[\"key2\"] =  net.jangaroo.icons.SomeBundle_properties.INSTANCE.someKey;\n" +
            "  this.prototype[\"key3\"] =  net.jangaroo.SomeOtherBundle_properties.INSTANCE.someOtherKey;\n" +
            "});").replaceAll("\n", LINE_SEPARATOR), writer.toString());
  }
}
