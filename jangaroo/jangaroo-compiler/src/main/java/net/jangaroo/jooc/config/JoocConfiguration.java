package net.jangaroo.jooc.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jangaroo compiler configuration
 */
public class JoocConfiguration implements JoocOptions {

  private SemicolonInsertionMode semicolonInsertionMode = SemicolonInsertionMode.WARN;

  private boolean debug;
  private boolean debugLines, debugSource;

  private boolean help, version, verbose, enableAssertions;

  private boolean allowDuplicateLocalVariables;

  private File outputDirectory;
  private File apiOutputDirectory;

  private boolean mergeOutput = false;
  private String outputFileName;

  private List<File> sourcePath = new ArrayList<File>(); // may contain directories which are source roots
  private List<File> classPath = new ArrayList<File>(); // may contain directories and jar files
  private List<File> sourceFiles = new ArrayList<File>();

  public JoocConfiguration() {
  }

  public SemicolonInsertionMode getSemicolonInsertionMode() {
    return semicolonInsertionMode;
  }

  public void setSemicolonInsertionMode(final SemicolonInsertionMode semicolonInsertionMode) {
    this.semicolonInsertionMode = semicolonInsertionMode;
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
    return new File(outputFileName);
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
    return debug && debugSource;
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

  @Override
  public boolean isGenerateApi() {
    return apiOutputDirectory != null;
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

  public boolean isAllowDuplicateLocalVariables() {
    return allowDuplicateLocalVariables;
  }

  public void setAllowDuplicateLocalVariables(boolean allowDuplicateLocalVariables) {
    this.allowDuplicateLocalVariables = allowDuplicateLocalVariables;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  public void setApiOutputDirectory(final File apiOutputDirectory) {
    this.apiOutputDirectory = apiOutputDirectory;
  }

  public List<File> getSourcePath() {
    return Collections.unmodifiableList(sourcePath);
  }

  public void setSourcePath(final List<File> sourcePath) {
    this.sourcePath = sourcePath;
  }

  public List<File> getClassPath() {
    return Collections.unmodifiableList(classPath);
  }

  public void setClassPath(final List<File> classPath) {
    assert classPath != null;
    this.classPath = classPath;
  }

  public List<File> getSourceFiles() {
    return Collections.unmodifiableList(sourceFiles);
  }

  public void setSourceFiles(List<File> sourceFiles) {
    if (sourceFiles == null) {
      throw new IllegalArgumentException("sourceFiles == null");
    }

    this.sourceFiles = new ArrayList<File>(sourceFiles);
  }

  public void addSourceFile(File source) {
    sourceFiles.add(source);
  }

  public void addSourceFile(String sourcepath) {
    addSourceFile(new File(sourcepath));
  }

}
