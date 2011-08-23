package net.jangaroo.exml.exmlconverter;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

/**
 * A test of file read and write operations.
 */
public class FileConverterTest {
  @Test
  public void testConvert() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".xml");
    File targetFile = File.createTempFile("FileConverterTest.target", ".xml");
    try {
      FileUtils.write(sourceFile, "<exml:component><prefix:WorkAreaBase/></exml:component>");
      FileConverter converter = new FileConverter(sourceFile, targetFile, "UTF8", new Properties());
      converter.execute();
      String result = FileUtils.readFileToString(targetFile);
      Assert.assertEquals("<exml:component><prefix:workAreaBase/></exml:component>", result);
    } finally {
      FileUtils.deleteQuietly(sourceFile);
      FileUtils.deleteQuietly(targetFile);
    }
  }
}
