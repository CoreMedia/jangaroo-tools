package net.jangaroo.jooc.config;

import net.jangaroo.utils.FileLocations;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jangaroo compiler configuration
 */
public class JoocConfiguration extends FileLocations implements JoocOptions, ParserOptions {

  private SemicolonInsertionMode semicolonInsertionMode = SemicolonInsertionMode.WARN;

  private DebugMode debugMode;

  private boolean help, version, verbose, enableAssertions;
  private PublicApiViolationsMode publicApiViolationsMode = PublicApiViolationsMode.WARN;
  private boolean excludeClassByDefault = false;

  private boolean allowDuplicateLocalVariables;

  private File apiOutputDirectory;

  private boolean mergeOutput = false;
  private String outputFileName;
  private boolean generateSourceMaps = false;

  private List<NamespaceConfiguration> namespaces = new ArrayList<NamespaceConfiguration>();

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

  @Override
  public PublicApiViolationsMode getPublicApiViolationsMode() {
    return publicApiViolationsMode;
  }

  @Option(name="-pav", aliases = "--publicApiViolations", usage = "Severity of public API violations, i.e. using classes that are annotated with [ExcludeClass]: error, warn, allow")
  public void setPublicApiViolationsMode(PublicApiViolationsMode warnPublicApiViolations) {
    this.publicApiViolationsMode = warnPublicApiViolations;
  }

  public boolean isExcludeClassByDefault() {
    return excludeClassByDefault;
  }

  @Option(name="-ec", aliases = "--excludeClassByDefault", usage = "Whether to add an [ExcludeClass] annotation to a class whenever no [PublicApi] annotation is present; defaults to false")
  public void setExcludeClassByDefault(boolean excludeClassByDefault) {
    this.excludeClassByDefault = excludeClassByDefault;
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

  @Option(name="-sm", aliases = "--generatesourcemaps", usage = "generate JavaScript source maps")
  public void setGenerateSourceMaps(boolean generateSourceMaps) {
    this.generateSourceMaps = generateSourceMaps;
  }

  @Override
  public boolean isGenerateSourceMaps() {
    return generateSourceMaps;
  }

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  @Option(name="-api", aliases = "--apiDir", usage = "destination directory where to generate ActionScript API stubs")
  public void setApiOutputDirectory(final File apiOutputDirectory) {
    this.apiOutputDirectory = apiOutputDirectory;
  }

  public List<NamespaceConfiguration> getNamespaces() {
    return Collections.unmodifiableList(namespaces);
  }

  @Option(name="-namespace", handler = NamespacesHandler.class, usage = "namespace of the component library defined in the given manifest file")
  public void setNamespaces(List<NamespaceConfiguration> namespaces) {
    this.namespaces = new ArrayList<NamespaceConfiguration>(namespaces);
  }

  public void addNamespaces(List<NamespaceConfiguration> namespaces) {
    this.namespaces.addAll(namespaces);
  }
}
