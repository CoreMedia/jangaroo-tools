package net.jangaroo.jooc;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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

    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "en");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "de");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "es_ES");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "it_VA_WIN");

    verifyApiOutput("testPackage/PropertiesTest_properties", "/expectedApi");
  }

  void verifyPropertiesOutput(String relativeClassFileName, String locale) throws URISyntaxException, IOException {
    if (jooc.getConfig().isMigrateToTypeScript()) {
      verifyClassOutput(relativeClassFileName + ("en".equals(locale) ? "" : "_" + locale), "/expected");
    } else {
      verifyOutput(relativeClassFileName, propertiesTargetDir(locale), "/expectedProperties/" + locale, ".js");
    }
  }

  private File propertiesTargetDir(String locale) throws URISyntaxException {
    return new File(localizedOutputFolder, locale);
  }
}
