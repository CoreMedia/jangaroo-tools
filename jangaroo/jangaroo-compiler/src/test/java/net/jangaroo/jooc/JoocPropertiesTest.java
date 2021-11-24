package net.jangaroo.jooc;

import net.jangaroo.properties.PropcHelper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

public class JoocPropertiesTest extends AbstractJoocTest {

  @Test
  public void testPropertiesCompilation() throws Exception {
    verifyPropertiesCompilation();
    jooc.getConfig().setMigrateToTypeScript(true);
    try {
      verifyPropertiesCompilation();
    } finally {
      jooc.getConfig().setMigrateToTypeScript(false);
    }
  }

  private void verifyPropertiesCompilation() throws Exception {
    compile(".properties",
            "testPackage/PropertiesTest",
            "testPackage/PropertiesTest_de",
            "testPackage/PropertiesTest_es_ES",
            "testPackage/PropertiesTest_it_VA_WIN");

    verifyPropertiesOutput("test/package2/PropertiesTest_properties", new Locale("en"));
    verifyPropertiesOutput("test/package2/PropertiesTest_properties", new Locale("de"));
    verifyPropertiesOutput("test/package2/PropertiesTest_properties", new Locale("es", "ES"));
    verifyPropertiesOutput("test/package2/PropertiesTest_properties", new Locale("it", "VA", "WIN"));

    verifyApiOutput("testPackage/PropertiesTest_properties", "/expectedApi");
  }

  @Test
  public void testEmptyPropertiesCompilation() throws Exception {
    verifyEmptyPropertiesCompilation();
    jooc.getConfig().setMigrateToTypeScript(true);
    try {
      verifyEmptyPropertiesCompilation();
    } finally {
      jooc.getConfig().setMigrateToTypeScript(false);
    }
  }

  private void verifyEmptyPropertiesCompilation() throws Exception {
    compile(".properties",
            "testPackage/EmptyPropertiesTest");

    verifyPropertiesOutput("testPackage/EmptyPropertiesTest_properties", new Locale("en"));
    verifyApiOutput("testPackage/EmptyPropertiesTest_properties", "/expectedApi");
  }

  void verifyPropertiesOutput(String relativeClassFileName, Locale locale) throws URISyntaxException, IOException {
    if (jooc.getConfig().isMigrateToTypeScript()) {
      verifyClassOutput(PropcHelper.insertNonDefaultLocale(relativeClassFileName, locale), "/expected");
    } else {
      verifyOutput(relativeClassFileName, propertiesTargetDir(locale.toString()), "/expectedProperties/" + locale, ".js");
    }
  }

  private File propertiesTargetDir(String locale) throws URISyntaxException {
    return new File(localizedOutputFolder, locale);
  }
}
