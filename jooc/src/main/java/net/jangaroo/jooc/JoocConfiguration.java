package net.jangaroo.jooc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Jangaroo compiler configuration
 */
public class JoocConfiguration {

  private boolean debug;

  private String debugLevel;

  private boolean verbose;

  private File outputDirectory;

  private Set<File> sourceFiles;

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public String getDebugLevel() {
    return debugLevel;
  }

  public void setDebugLevel(String debugLevel) {
    this.debugLevel = debugLevel;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public Set<File> getSourceFiles() {
    return sourceFiles;
  }

  public void setSourceFiles(Set<File> sourceFiles) {
    this.sourceFiles = sourceFiles;
  }

  public String[] getCommandLine() {
    List<String> args = new ArrayList<String>(sourceFiles.size() + 10);
    if (debug) {
      args.add("-g");
      if (debugLevel != null) {
        args.add(debugLevel);
      }
    }
    if (verbose) {
      args.add("-v");
    }
    // TODO: reenable assertions
    //if (enableAssertions) args.add("-ea");
    if (outputDirectory != null) {
      args.add("-d");
      args.add(outputDirectory.getAbsolutePath());
    }

    for (File sourceFile : sourceFiles) {
      String filename = sourceFile.getAbsolutePath();
      args.add(filename);
    }
    return args.toArray(new String[args.size()]);
  }
}
