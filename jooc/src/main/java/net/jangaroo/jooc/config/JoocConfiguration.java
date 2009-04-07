package net.jangaroo.jooc.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Jangaroo compiler configuration
 */
public class JoocConfiguration implements JoocOptions {

  private boolean debug;
  private boolean debugLines, debugSource;

  private boolean help, version, verbose, enableAssertions;

  private boolean enableGuessingMembers, enableGuessingClasses, enableGuessingTypeCasts;

  private File outputDirectory;

  private boolean mergeOutput = false;
  private String outputFileName;

  private List<File> sourceFiles = new ArrayList<File>();

  public JoocConfiguration() {
  }

  public boolean isMergeOutput() {
    return mergeOutput;
  }

  public void setMergeOutput(boolean mergeOutput) {
    this.mergeOutput = mergeOutput;
  }

  public String getOutputFileName() {
    return outputFileName;
  }

  public void setOutputFileName(String outputFileName) {
    this.outputFileName = outputFileName;
  }

  public File getOutputFile() {
    return (mergeOutput && outputDirectory != null && outputFileName != null ?
      new File(outputDirectory, outputFileName) : null);
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public void setDebugLines(boolean debugLines) {
    this.debugLines = debugLines;
  }

  public boolean isDebugLines() {
    return debug && debugLines;
  }

  public boolean isDebugSource() {
    return  debug && debugSource;
  }

  public void setDebugSource(boolean debugSource) {
    this.debugSource = debugSource;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public boolean isHelp() {
    return help;
  }

  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isVersion() {
    return version;
  }

  public void setVersion(boolean version) {
    this.version = version;
  }

  public boolean isEnableAssertions() {
    return enableAssertions;
  }

  public void setEnableAssertions(boolean enableAssertions) {
    this.enableAssertions = enableAssertions;
  }

  public boolean isEnableGuessingMembers() {
    return enableGuessingMembers;
  }

  public void setEnableGuessingMembers(boolean enableGuessingMembers) {
    this.enableGuessingMembers = enableGuessingMembers;
  }

  public boolean isEnableGuessingClasses() {
    return enableGuessingClasses;
  }

  public void setEnableGuessingClasses(boolean enableGuessingClasses) {
    this.enableGuessingClasses = enableGuessingClasses;
  }

  public boolean isEnableGuessingTypeCasts() {
    return enableGuessingTypeCasts;
  }

  public void setEnableGuessingTypeCasts(boolean enableGuessingTypeCasts) {
    this.enableGuessingTypeCasts = enableGuessingTypeCasts;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public List<File> getSourceFiles() {
    return sourceFiles;
  }

  public void setSourceFiles(List<File> sourceFiles) {
    if (sourceFiles == null)
      throw new IllegalArgumentException("sourceFiles == null");

    this.sourceFiles = new ArrayList<File>(sourceFiles);
  }

  public void addSourceFile(File source) {
    sourceFiles.add(source);
  }

  public void addSourceFile(String sourcepath) {
    addSourceFile(new File(sourcepath));
  }

}
