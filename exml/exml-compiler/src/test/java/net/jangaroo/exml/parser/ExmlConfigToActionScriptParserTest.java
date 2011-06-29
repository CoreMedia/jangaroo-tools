package net.jangaroo.exml.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class ExmlConfigToActionScriptParserTest {

  @Rule
  public TemporaryFolder outputFolder= new TemporaryFolder();


  @Test
  public void testParse() throws Exception {
    File result = ExmlConfigToActionScriptParser.parseExmlFile(getFile("/"), outputFolder.getRoot(), getFile("/testPackage/TestComponent.exml"), "testNamespace.config");
    assertNotNull("Exml config file is null",result);
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")),FileUtils.readFileToString(result));
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlConfigToActionScriptParserTest.class.getResource(path).toURI());
  }
}
