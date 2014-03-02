package net.jangaroo.exml.test;

import net.jangaroo.exml.generator.ExmlComponentClassGenerator;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.model.ExmlSourceFile;
import net.jangaroo.exml.parser.ExmlToModelParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class ExmlComponentClassGeneratorTest extends AbstractExmlTest {
  @Test
  public void testGenerateClass() throws Exception {
    setUp("exmlparser.config");
    String expected = FileUtils.readFileToString(new File(getClass().getResource("/expected/AllElements.as").toURI()));

    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());
    ExmlModel model = exmlToModelParser.parse(getFile("/exmlparser/AllElements.exml"));

    StringWriter output = new StringWriter();
    new ExmlComponentClassGenerator(getExmlc().getConfig()).generateClass(model, output);
    Assert.assertEquals(expected, output.toString());
  }

  @Test
  public void testDoNotGenerateClassWhenActionScriptClassIsAlreadyThere() throws Exception {
    setUp("exmlparser.config");
    ExmlSourceFile exmlSourceFile = new ExmlSourceFile(getConfigClassRegistry(), getFile("/testPackage/TestAction.exml"));
    File outputFile = exmlSourceFile.generateTargetClass();
    Assert.assertNull("A target class must only be generated when there is no ActionScript source class of the same name as the EXML class.", outputFile);
  }
  
  @Test
  public void testGenerateClassWithLowerCaseFileName() throws Exception {
    setUp("exmlparser.config");
    File sourceFile = getFile("/exmlparser/testLowerCase.exml");

    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());
    ExmlModel model = exmlToModelParser.parse(sourceFile);

    Assert.assertEquals("TestLowerCase", model.getClassName());
    Assert.assertEquals("testLowerCase", model.getConfigClass().getName());
    Assert.assertEquals("exmlparser", model.getPackageName());
  }

  @Test
  public void testGenerateClassWithUpperCaseFileName() throws Exception {
    setUp("exmlparser.config");
    File sourceFile = getFile("/exmlparser/AllElements.exml");

    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());
    ExmlModel model = exmlToModelParser.parse(sourceFile);

    Assert.assertEquals("AllElements", model.getClassName());
    Assert.assertEquals("allElements", model.getConfigClass().getName());
    Assert.assertEquals("exmlparser", model.getPackageName());
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlComponentClassGeneratorTest.class.getResource("/test-module" + path).toURI());
  }
}
