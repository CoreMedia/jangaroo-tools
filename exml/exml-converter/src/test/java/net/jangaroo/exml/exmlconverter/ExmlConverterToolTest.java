package net.jangaroo.exml.exmlconverter;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 */
public class ExmlConverterToolTest {

  @Test
  public void testConvertFileWithEncoding() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".exml");
    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<exml:component><prefix:WörkAreaBase/></exml:component>";
      String encoding = "ISO-8859-1";
      FileUtils.write(sourceFile, input, encoding);
      ExmlConverterTool tool = new ExmlConverterTool(encoding, sourceFile.getParentFile(), new Properties());
      assertTrue(tool.convertAll());
      String result = FileUtils.readFileToString(sourceFile, encoding);
      Assert.assertEquals("<exml:component><prefix:wörkAreaBase/></exml:component>", result);
      String backup = FileUtils.readFileToString(backupFile, encoding);
      Assert.assertEquals(input, backup);
    } finally {
      FileUtils.deleteQuietly(sourceFile);
      FileUtils.deleteQuietly(backupFile);
    }
  }

  @Test
  public void testConvertMalformedFile() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".exml");
    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<";
      FileUtils.write(sourceFile, input);
      ExmlConverterTool tool = new ExmlConverterTool("UTF-8", sourceFile.getParentFile(), new Properties());
      assertFalse(tool.convertAll());
    } finally {
      FileUtils.deleteQuietly(sourceFile);
      FileUtils.deleteQuietly(backupFile);
    }
  }

  @Test
  public void testConvertAll() throws Exception {
    File tempDir = File.createTempFile("FileConverterTest.", ".dir");
    Assert.assertTrue(tempDir.delete());
    Assert.assertTrue(tempDir.mkdir());

    File subdir = new File(tempDir, "subdir");
    Assert.assertTrue(subdir.mkdir());

    File sourceFile = new File(subdir, "FileConverterTest.exml");
    Assert.assertTrue(new File(tempDir, "empty").mkdir());

    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<exml:component><prefix:WorkAreaBase/></exml:component>";
      FileUtils.write(sourceFile, input);
      ExmlConverterTool tool = new ExmlConverterTool("UTF-8", tempDir, new Properties());
      assertTrue(tool.convertAll());
      String result = FileUtils.readFileToString(sourceFile);
      Assert.assertEquals("<exml:component><prefix:workAreaBase/></exml:component>", result);
      String backup = FileUtils.readFileToString(backupFile);
      Assert.assertEquals(input, backup);
    } finally {
      FileUtils.deleteQuietly(tempDir);
    }
  }
}
