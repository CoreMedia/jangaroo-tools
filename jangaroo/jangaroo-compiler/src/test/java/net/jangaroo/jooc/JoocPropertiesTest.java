package net.jangaroo.jooc;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JoocPropertiesTest extends AbstractJoocTest {

  @Test
  public void testPropertiesCompilation() throws Exception {
    compile(".properties",
            //todo"testPackage/subPackage/Proberties",
            "testPackage/PropertiesTest",
            "testPackage/PropertiesTest_de",
            "testPackage/PropertiesTest_es_ES",
            "testPackage/PropertiesTest_it_VA_WIN");

    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "en");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "de");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "es_ES");
    verifyPropertiesOutput("testPackage/PropertiesTest_properties", "it_VA_WIN");

    //todo verifyApiOutput("testPackage/PropertiesTest_properties", "/expectedApi");
  }

  void verifyPropertiesOutput(String relativeClassFileName, String locale) throws URISyntaxException, IOException {
    verifyOutput(relativeClassFileName, propertiesTargetDir(locale), "/expectedProperties/" + locale, ".js");
  }

  private File propertiesTargetDir(String locale) throws URISyntaxException {
    return new File(localizedOutputFolder, locale);
  }
}
