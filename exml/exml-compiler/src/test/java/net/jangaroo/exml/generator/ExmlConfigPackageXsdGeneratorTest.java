package net.jangaroo.exml.generator;

import net.jangaroo.exml.test.AbstractExmlTest;
import org.junit.Test;

import java.io.StringWriter;

/**
 *
 */
public class ExmlConfigPackageXsdGeneratorTest extends AbstractExmlTest{
  @Test
  public void testGenerateXsdFile() throws Exception {
    setUp("ext.config");

    StringWriter output = new StringWriter();
    getExmlc().getExmlConfigPackageXsdGenerator().generateXsdFile(getConfigClassRegistry(), output);
    System.out.println(output.toString());
  }
}
