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
  private boolean suppressCommentedActionScriptCode = false;

  private boolean help, version, verbose, enableAssertions;
  private PublicApiViolationsMode publicApiViolationsMode = PublicApiViolationsMode.WARN;
  private boolean excludeClassByDefault = false;

  private boolean allowDuplicateLocalVariables;

  private File localizedOutputDirectory;

  private File apiOutputDirectory;
  private boolean migrateToTypeScript = false;
  private long typeScriptTargetSourceFormatFeatures = 0L;
  private String extNamespace = "";
  private String extSassNamespace = "";
  private String npmPackageName;
  private boolean useEcmaParameterInitializerSemantics = false;

  private boolean mergeOutput = false;
  private String outputFileName;
  private boolean generateSourceMaps = false;

  private File keepGeneratedActionScriptDirectory;

  private List<NamespaceConfiguration> namespaces = new ArrayList<NamespaceConfiguration>();

  private File catalogOutputDirectory;
  private File reportOutputDirectory;

  private boolean findUnusedDependencies;
  private String dependencyReportOutputFile;

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

  @Override
  public boolean isSuppressCommentedActionScriptCode() {
    return suppressCommentedActionScriptCode;
  }

  @Option(name="-noas", aliases = "--suppress-commented-actionscript-code", usage="Suppress comments for ActionScript syntax like types and modifiers in JavaScript output. Only effective with -g source.")
  public void setSuppressCommentedActionScriptCode(boolean suppressCommentedActionScriptCode) {
    this.suppressCommentedActionScriptCode = suppressCommentedActionScriptCode;
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
  public boolean isMigrateToTypeScript() {
    return migrateToTypeScript;
  }

  @Override
  public long getTypeScriptTargetSourceFormatFeatures() {
    return typeScriptTargetSourceFormatFeatures;
  }

  @Override
  public String getExtNamespace() {
    return extNamespace;
  }

  @Override
  public String getExtSassNamespace() {
    return extSassNamespace;
  }

  @Override
  public String getNpmPackageName() {
    return npmPackageName;
  }

  @Override
  public boolean isUseEcmaParameterInitializerSemantics() {
    return useEcmaParameterInitializerSemantics;
  }

  @Option(name="-ts", aliases = "--migrate-to-typescript", usage ="Migrate ActionScript/MXML code to TypeScript")
  public void setMigrateToTypeScript(boolean migrateToTypeScript) {
    this.migrateToTypeScript = migrateToTypeScript;
  }

  @Option(name="-tsf", aliases="--typescript-target-source-format-features", usage =
          "Sets the target source code format features for migrating ActionScript/MXML code to TypeScript, " +
                  "implicitly activating migrate-to-typescript when the value is not 0 (zero). " +
                  "The value is a combination (addition) of the following feature flags:\n"
                  + "1 - simplified this-usage-before-super-constructor-call syntax\n"
                  + "2 - simplify (as(foo, Foo)).bar to (foo as Foo).bar, i.e. leave out runtime check that would have caused NPE anyway\n"
                  + "4 - generate static blocks (requires TypeScript 4.4, to correctly accept forward-references, 4.7)\n"
  )
  public void setTypeScriptTargetSourceFormatFeatures(long typeScriptTargetSourceFormatFeatures) {
    this.typeScriptTargetSourceFormatFeatures = typeScriptTargetSourceFormatFeatures;
    if (typeScriptTargetSourceFormatFeatures != 0) {
      migrateToTypeScript = true;
    }
  }

  @Option(name="--extNamespace", usage ="The Ext namespace is stripped from the relative path to the source root")
  public void setExtNamespace(String extNamespace) {
    this.extNamespace = extNamespace;
  }

  @Option(name="--extSassNamespace", usage ="The Ext namespace is stripped from the relative path to the source root")
  public void setExtSassNamespace(String extSassNamespace) {
    this.extSassNamespace = extSassNamespace;
  }

  @Option(name="-epi", aliases = "--ecma-parameter-initializers", usage ="Use ECMAScript parameter initializer semantics")
  public void setUseEcmaParameterInitializerSemantics(boolean useEcmaParameterInitializerSemantics) {
    this.useEcmaParameterInitializerSemantics = useEcmaParameterInitializerSemantics;
  }

  public void setNpmPackageName(String npmPackageName) {
    this.npmPackageName = npmPackageName;
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

  public File getLocalizedOutputDirectory() {
    return localizedOutputDirectory;
  }

  @Option(name="-ld", aliases = "--localizedDir", usage = "destination directory for generated localized JavaScript files, compiled from properties files")
  public void setLocalizedOutputDirectory(final File localizedOutputDirectory) {
    this.localizedOutputDirectory = localizedOutputDirectory;
  }

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  @Option(name="-api", aliases = "--apiDir", usage = "destination directory where to generate ActionScript API stubs")
  public void setApiOutputDirectory(final File apiOutputDirectory) {
    this.apiOutputDirectory = apiOutputDirectory;
  }

  @Override
  public File getKeepGeneratedActionScriptDirectory() {
    return keepGeneratedActionScriptDirectory;
  }

  @Option(name="-kgasd", aliases = "--keepGeneratedActionScriptDir", usage = "directory where to keep ActionScript files generated from MXML sources")
  public void setKeepGeneratedActionScriptDirectory(File keepGeneratedActionScriptDirectory) {
    this.keepGeneratedActionScriptDirectory = keepGeneratedActionScriptDirectory;
  }

  public List<NamespaceConfiguration> getNamespaces() {
    return Collections.unmodifiableList(namespaces);
  }

  @Option(name="-namespace", handler = NamespacesHandler.class, usage = "namespace of the component library defined in the given manifest file")
  public void setNamespaces(List<NamespaceConfiguration> namespaces) {
    this.namespaces = new ArrayList<NamespaceConfiguration>(namespaces);
  }

  public File getCatalogOutputDirectory() {
    return catalogOutputDirectory;
  }

  @Option(name="-catalog", aliases = "--catalogDir", usage = "destination directory where to generate catalog.xml")
  public void setCatalogOutputDirectory(File catalogOutputDirectory) {
    this.catalogOutputDirectory = catalogOutputDirectory;
  }

  public File getReportOutputDirectory() {
    return reportOutputDirectory;
  }

  @Option(name="-report", aliases = "--reportDir", usage = "output directory for compilation reports like the cyclic classes report")
  public void setReportOutputDirectory(File reportOutputDirectory) {
    this.reportOutputDirectory = reportOutputDirectory;
  }

  @Override
  public boolean isFindUnusedDependencies() {
    // cannot detect unused dependencies if compile path is empty:
    return findUnusedDependencies && !getCompilePath().isEmpty();
  }

  @Option(name = "--findUnusedDependencies", usage = "Enable checking for unused dependencies, i.e. unused compilePath entries.")
  public void setFindUnusedDependencies(boolean findUnusedDependencies) {
    this.findUnusedDependencies = findUnusedDependencies;
  }

  @Override
  public String getDependencyReportOutputFile() {
    return this.dependencyReportOutputFile;
  }

  @Option(name = "--dependencyReportOutputFile", usage = "the file to use for saving the dependency warnings. In order to execute the dependency checks, this need to be set.")
  public void setDependencyReportOutputFile(String dependencyReportOutputFile) {
    this.dependencyReportOutputFile = dependencyReportOutputFile;
  }
}
