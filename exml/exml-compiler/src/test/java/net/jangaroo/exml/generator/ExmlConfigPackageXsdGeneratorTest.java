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
    setUp("ext.config", "/ext-as/", "/");

    String expected = FileUtils.readFileToString(new File(getClass().getResource("/ext-as/ext.config.xsd").toURI()));

    StringWriter output = new StringWriter();
    getConfigClassRegistry().generateXsd(output);
    //System.out.println(output.toString());
    expected = expected.replaceAll("\r\n", "\n");
    String actual = output.toString().replaceAll("\r\n", "\n");
    Assert.assertEquals(expected, actual);
  }
}
