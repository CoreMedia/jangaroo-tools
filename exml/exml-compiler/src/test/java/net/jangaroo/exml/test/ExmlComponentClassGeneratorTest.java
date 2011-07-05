package net.jangaroo.exml.test;

import net.jangaroo.exml.generator.ExmlComponentClassGenerator;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToModelParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class ExmlComponentClassGeneratorTest extends AbstractExmlTest {
  @Test
  public void testGenerateClass() throws Exception {
    setUp("exmlparser.config");
    String expected = FileUtils.readFileToString(new File(getClass().getResource("/exmlparser/AllElements.as").toURI()));
    InputStream inputStream = getClass().getResourceAsStream("/exmlparser/AllElements.exml");
    ExmlToModelParser exmlToModelParser = new ExmlToModelParser(getConfigClassRegistry());
    ExmlModel model = exmlToModelParser.parse(inputStream);
    model.setClassName("AllElements");
    model.setPackageName("exmlparser");

    StringWriter output = new StringWriter();
    getExmlc().getExmlComponentClassGenerator().generateClass(model, output);
    Assert.assertEquals(expected, output.toString());
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlComponentClassGeneratorTest.class.getResource(path).toURI());
  }
}
