package com.coremedia.studio.tools.exmlconverter;

import net.jangaroo.exml.exmlconverter.ExmlConverterTool;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * A test of the file handling and the overall wiring of the conversion tool.
 */
public class ExmlConverterToolTest {
  private class ExitError extends Error {}

  private class TestTool extends ExmlConverterTool {
    public TestTool(String[] args) {
      super(args);
    }

    @Override
    void exit(int code) {
      throw new ExitError();
    }
  }

  @Test(expected = ExitError.class)
  public void testUsageNoArgs() throws Exception {
    new TestTool(new String[0]).run();
  }

  @Test(expected = ExitError.class)
  public void testUsageTooManyArgs() throws Exception {
    new TestTool(new String[]{"a", "b", "c"}).run();
  }

  @Test(expected = ExitError.class)
  public void testFileDoesNotExist() throws Exception {
    new TestTool(new String[]{"/no/such/file/exists/on/any/test/system"}).run();
  }

  @Test
  public void testConvertFile() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".exml");
    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<exml:component><prefix:WorkAreaBase/></exml:component>";
      FileUtils.write(sourceFile, input);
      ExmlConverterTool.main(new String[]{sourceFile.getPath()});
      String result = FileUtils.readFileToString(sourceFile);
      Assert.assertEquals("<exml:component><prefix:workAreaBase/></exml:component>", result);
      String backup = FileUtils.readFileToString(backupFile);
      Assert.assertEquals(input, backup);
    } finally {
      FileUtils.deleteQuietly(sourceFile);
      FileUtils.deleteQuietly(backupFile);
    }
  }

  @Test
  public void testConvertFileWithEncoding() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".exml");
    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<exml:component><prefix:WörkAreaBase/></exml:component>";
      String encoding = "ISO-8859-1";
      FileUtils.write(sourceFile, input, encoding);
      ExmlConverterTool.main(new String[]{sourceFile.getPath(), encoding});
      String result = FileUtils.readFileToString(sourceFile, encoding);
      Assert.assertEquals("<exml:component><prefix:wörkAreaBase/></exml:component>", result);
      String backup = FileUtils.readFileToString(backupFile, encoding);
      Assert.assertEquals(input, backup);
    } finally {
      FileUtils.deleteQuietly(sourceFile);
      FileUtils.deleteQuietly(backupFile);
    }
  }

  @Test(expected = ExitError.class)
  public void testConvertMalformedFile() throws Exception {
    File sourceFile = File.createTempFile("FileConverterTest.source", ".exml");
    File backupFile = new File(sourceFile.getPath() + ".bak");
    try {
      String input = "<";
      FileUtils.write(sourceFile, input);
      new TestTool(new String[]{sourceFile.getPath()}).run();
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
      ExmlConverterTool.main(new String[]{tempDir.getPath()});
      String result = FileUtils.readFileToString(sourceFile);
      Assert.assertEquals("<exml:component><prefix:workAreaBase/></exml:component>", result);
      String backup = FileUtils.readFileToString(backupFile);
      Assert.assertEquals(input, backup);
    } finally {
      FileUtils.deleteQuietly(tempDir);
    }
  }
}
