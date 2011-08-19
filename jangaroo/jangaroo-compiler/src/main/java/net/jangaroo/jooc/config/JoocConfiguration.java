package net.jangaroo.jooc.config;

import org.kohsuke.args4j.Option;

import java.io.File;

/**
 * Jangaroo compiler configuration
 */
public class JoocConfiguration extends FileLocations implements JoocOptions, ParserOptions {

  private SemicolonInsertionMode semicolonInsertionMode = SemicolonInsertionMode.WARN;

  private DebugMode debugMode;

  private boolean help, version, verbose, enableAssertions;

  private boolean allowDuplicateLocalVariables;

  private File apiOutputDirectory;

  private boolean mergeOutput = false;
  private String outputFileName;

  public SemicolonInsertionMode getSemicolonInsertionMode() {
    return semicolonInsertionMode;
  }

  @Option(name = "-autosemicolon", usage = "automatic semicolon insertion mode, possible modes: error, warn (default), quirk (no warnings)")
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

  public DebugMode getDebugMode() {
    return debugMode;
  }

  @Option(name="-g", usage ="generate debuggable output (possible modes: source, lines, none)")
  public void setDebugMode(DebugMode debugMode) {
    this.debugMode = debugMode;
  }

  public boolean isVerbose() {
    return verbose;
  }

  @Option(name="-v", aliases = "--verbose", usage="be extra verbose")
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

  @Option(name="-h", aliases = "--help", usage = "print this message")
  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isVersion() {
    return version;
  }

  @Option(name="-version", usage = "print version information and exit")
  public void setVersion(boolean version) {
    this.version = version;
  }

  public boolean isEnableAssertions() {
    return enableAssertions;
  }
  
  @Option(name="-ea", aliases = "--enableassertions", usage = "enable assertions")
  public void setEnableAssertions(boolean enableAssertions) {
    this.enableAssertions = enableAssertions;
  }

  public boolean isAllowDuplicateLocalVariables() {
    return allowDuplicateLocalVariables;
  }

  @Option(name="-ad", aliases = "--allowduplicatelocalvariables", usage = "allow multiple declarations of local variables")
  public void setAllowDuplicateLocalVariables(boolean allowDuplicateLocalVariables) {
    this.allowDuplicateLocalVariables = allowDuplicateLocalVariables;
  }

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  @Option(name="-api", aliases = "--apiDir", usage = "destination directory where to generate ActionScript API stubs")
  public void setApiOutputDirectory(final File apiOutputDirectory) {
    this.apiOutputDirectory = apiOutputDirectory;
  }

}
