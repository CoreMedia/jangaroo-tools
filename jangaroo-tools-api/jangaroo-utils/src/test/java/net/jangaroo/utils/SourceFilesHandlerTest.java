package net.jangaroo.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class SourceFilesHandlerTest {
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  protected static class SourceFilesValueHolder {
    private List<File> sourcePath;

    @Argument(metaVar = "path", handler = SourceFilesHandler.class, required = true)
    public void setSourceFiles(List<File> sourcePath) {
      this.sourcePath = new ArrayList<File>(sourcePath);
    }
  }

  @Test(expected = CmdLineException.class)
  public void testDirectoryDoesNotExists() throws Exception {
    SourceFilesValueHolder holder = new SourceFilesValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);
    parser.parseArgument("xyz");
  }

  @Test
  public void testWithOneDirectory() throws Exception {
    SourceFilesValueHolder holder = new SourceFilesValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);
    parser.parseArgument(outputFolder.getRoot().toString());
    assertEquals("One folder", 1, holder.sourcePath.size());
  }

  @Test
  public void testWithMoreDirectories() throws Exception {
    SourceFilesValueHolder holder = new SourceFilesValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);

    File one = outputFolder.newFile("one");
    File two = outputFolder.newFile("two");
    File three = outputFolder.newFile("three");

    parser.parseArgument(one.getAbsolutePath(), two.getAbsolutePath(), three.getAbsolutePath());

    assertEquals("three files", 3, holder.sourcePath.size());
  }
}
