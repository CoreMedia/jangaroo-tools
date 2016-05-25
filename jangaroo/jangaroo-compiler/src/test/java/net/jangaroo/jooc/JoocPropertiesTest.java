package net.jangaroo.jooc;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JoocPropertiesTest extends AbstractJoocTest {

  @Test
  public void testPropertiesCompilation() throws Exception {
    //todo add default locale parameter
    compile(".properties",
            //todo"testPackage/subPackage/Proberties",
            "testPackage/PropertiesTest",
            "testPackage/PropertiesTest_de",
            "testPackage/PropertiesTest_es_ES",
            "testPackage/PropertiesTest_it_VA_WIN");

    verifyPropertiesOutput("testPackage/PropertiesTest", "en");
  }

  void verifyPropertiesOutput(String relativeClassFileName, String locale) throws URISyntaxException, IOException {
    verifyOutput(relativeClassFileName, propertiesTargetDir(locale), "/expectedProperties", ".js");
  }

  private File propertiesTargetDir(String locale) throws URISyntaxException {
    return new File(outputFolder, locale); //todo introduce locale output folder
}


}
