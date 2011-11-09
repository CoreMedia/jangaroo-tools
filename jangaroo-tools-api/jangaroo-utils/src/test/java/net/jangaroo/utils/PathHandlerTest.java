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
public class PathHandlerTest {
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  protected static class PathValueHolder {
    private List<File> sourcePath;

    @Argument(metaVar = "path", handler = PathHandler.class)
    public void setSourceFiles(List<File> sourcePath) {
      this.sourcePath = new ArrayList<File>(sourcePath);
    }
  }

  @Test(expected = CmdLineException.class)
  public void testDirectoryDoesNotExists() throws Exception {
    PathValueHolder holder = new PathValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);
    parser.parseArgument("xyz");
  }

  @Test
  public void testWithOneDirectory() throws Exception {
    PathValueHolder holder = new PathValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);
    parser.parseArgument(outputFolder.getRoot().toString());
    assertEquals("One folder", 1, holder.sourcePath.size());
  }

  @Test
  public void testWithNull() throws Exception {
    PathValueHolder holder = new PathValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);
    parser.parseArgument("");
    assertEquals("Empty", 0, holder.sourcePath.size());
  }

  @Test
  public void testWithMoreDirectories() throws Exception {
    PathValueHolder holder = new PathValueHolder();
    CmdLineParser parser = new CmdLineParser(holder);

    File one = outputFolder.newFile("one");
    File two = outputFolder.newFile("two");
    File three = outputFolder.newFile("three");

    String arg = one.getAbsolutePath() + File.pathSeparator + two.getAbsolutePath() + File.pathSeparator + three.getAbsolutePath();

    parser.parseArgument(arg);

    assertEquals("three files", 3, holder.sourcePath.size());
  }
}
