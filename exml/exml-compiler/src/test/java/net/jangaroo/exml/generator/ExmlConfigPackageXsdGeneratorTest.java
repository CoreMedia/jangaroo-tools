package net.jangaroo.exml.generator;

import net.jangaroo.exml.test.AbstractExmlTest;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;

/**
 *
 */
public class ExmlConfigPackageXsdGeneratorTest extends AbstractExmlTest{
  @Test
  public void testGenerateXsdFile() throws Exception {
    setUp("ext.config");

    String expected = FileUtils.readFileToString(new File(getClass().getResource("/ext.config.xsd").toURI()));

    StringWriter output = new StringWriter();
    getExmlc().getExmlConfigPackageXsdGenerator().generateXsdFile(getConfigClassRegistry(), output);
    System.out.println(output.toString());
    
    Assert.assertEquals(expected, output.toString());
  }
}
