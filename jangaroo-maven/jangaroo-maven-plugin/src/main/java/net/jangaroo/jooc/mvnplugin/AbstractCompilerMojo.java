package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.Lists;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.AbstractCompileLog;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.api.CompilationResult;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.NamespaceConfiguration;
import net.jangaroo.jooc.config.PublicApiViolationsMode;
import net.jangaroo.jooc.config.SearchAndReplace;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Super class for mojos compiling Jangaroo sources.
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
public abstract class AbstractCompilerMojo extends AbstractJangarooMojo {
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String EXML_MAVEN_PLUGIN_ARTIFACT_ID = "exml-maven-plugin";

  /**
   * Indicates whether the build will fail if there are compilation errors.
   * Defaults to "true".
   */
  @SuppressWarnings("FieldCanBeLocal")
  @Parameter(property = "maven.compiler.failOnError")
  private boolean failOnError = true;

  /**
   * Set "enableAssertions" to "true" in order to generate runtime checks for assert statements.
   */
  @Parameter(property = "maven.compile.ea")
  private boolean enableAssertions;

  /**
   * Set "allowDuplicateLocalVariables" to "true" in order to allow multiple declarations of local variables
   * within the same scope.
   */
  @Parameter(property = "maven.compiler.allowDuplicateLocalVariables")
  private boolean allowDuplicateLocalVariables;

  /**
   * "publicApiViolations" controls how the compiler reacts on usage of non-public API classes,
   * i.e. classes annotated with <code>[ExcludeClass]</code>.
   * It can take the values "warn" to log a warning whenever such a class is used, "allow" to suppress such warnings,
   * and "error" to stop the build with an error.
   */
  @Parameter(property = "maven.compiler.publicApiViolations")
  private String publicApiViolations = PublicApiViolationsMode.WARN.toString().toLowerCase();

  /**
   * If set to "true", the compiler will add an [ExcludeClass] annotation to any
   * API stub whose source class contains neither an [PublicApi] nor an [ExcludeClass]
   * annotation.
   */
  @Parameter(property = "maven.compiler.excludeClassByDefault")
  private boolean excludeClassByDefault;

  /**
   * Let the compiler generate JavaScript source maps that allow debuggers
   * (currently only Google Chrome) to show the original ActionScript source code during
   * debugging.
   * Set to <code>false</code> to disable this feature to decrease build time and artifact size.
   */
  @Parameter(property = "maven.compiler.generateSourceMaps")
  private boolean generateSourceMaps;

  /**
   * If set to "true", the compiler will generate more detailed progress information.
   */
  @Parameter(property = "maven.compiler.verbose")
  private boolean verbose;

  /**
   * Sets the granularity in milliseconds of the last modification
   * date for testing whether a source needs recompilation.
   */
  @Parameter(property = "lastModGranularityMs")
  private int staleMillis;

  /**
   * Keyword list to be appended to the -g  command-line switch. Legal values are one of the following keywords: none, lines, or source.
   * If debuglevel is not specified, by default, nothing will be appended to -g. If debug is not turned on, this attribute will be ignored.
   */
  @Parameter(property = "maven.compiler.debuglevel")
  private String debuglevel = DebugMode.SOURCE.toString().toLowerCase();

  /**
   * Keyword list to be appended to the -autosemicolon  command-line switch. Legal values are one of the following keywords: error, warn (default), or quirks.
   */
  @Parameter(property = "maven.compiler.autoSemicolon")
  private String autoSemicolon = SemicolonInsertionMode.WARN.toString().toLowerCase();

  /**
   * Output directory for all generated ActionScript3 files to compile.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/joo")
  private File generatedSourcesDirectory;

  /**
   * Directory where to save ActionScript3 files generated from MXML.
   * These are not needed for compilation (MXML files are compiled directly to JavaScript),
   * but can be kept for your information.
   */
  @Parameter
  private File keepGeneratedActionScriptDirectory;

  /**
   * Output directory into which to generate an SWC-compatible catalog.xml generated
   * from all namespaces and manifests.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File catalogOutputDirectory;

  /**
   * Experimental: If set to "true", compiler generates TypeScript output instead of JavaScript.
   */
  @Parameter(property = "maven.compiler.migrateToTypeScript")
  private boolean migrateToTypeScript = false;

  /**
   * Experimental: The Ext namespace is stripped from the relative path to the source root.
   */
  @Parameter(property = "extNamespace")
  private String extNamespace = "";

  /**
   * Experimental: The Ext namespace is stripped from the relative path to the sass sources roots (var/src)
   */
  @Parameter(property = "extSassNamespace")
  private String extSassNamespace = null;

  @Parameter (defaultValue = "${project.basedir}/src/main/sencha")
  private File senchaSrcDir;

  /**
   * Experimental: If set to "true", compiler generates parameter initializer code that implements
   * ECMAScript semantics, which is subtly different from ActionScript 3 semantics.
   * In ECMAScript, initializer values are assigned to all 'undefined' arguments.
   * In AS3, initializer values are assigned only if you call a method with less arguments.
   * An example would be
   *     function foo(bar: string = "default"): string {
   *       return bar;
   *     }
   *     foo(); // "default" for both AS3 and ECMAScript semantics
   *     foo(undefined); // 'undefined' in AS3, "default" in ECMAScript semantics
   */
  @Parameter(property = "maven.compiler.useEcmaParameterInitializerSemantics")
  private boolean useEcmaParameterInitializerSemantics = false;

  /**
   * Experimental: If set to "true", compiler generates no ActionScript syntax in comments, resulting
   * in "purer" JavaScript code.
   */
  @Parameter(property = "maven.compiler.suppressCommentedActionScriptCode")
  private boolean suppressCommentedActionScriptCode = false;

  /**
   * Experimental:
   * Only effective if {@link #migrateToTypeScript} is enabled. The configuration can be used to replace the generated
   * npm package name of a converted Maven module by a different one.
   * It defines a list of replacers consisting of a search and a replace field. The search is interpreted as
   * a regular pattern matched against the generated npm package name while the replacement is a string which can
   * contain tokens (e.g. $1) matching pattern groups. Order is important, the first matching replacer wins.
   */
  @Parameter
  private List<SearchAndReplaceConfiguration> npmPackageNameReplacers = new ArrayList<>();

  protected abstract List<File> getCompileSourceRoots();

  protected abstract File getOutputDirectory();

  protected File getClassesOutputDirectory() {
    return new File(getOutputDirectory(), Type.JANGAROO_APP_PACKAGING.equals(getProject().getPackaging()) ? "app" : "src");
  }

  /**
   * TODO: make this configurable via POM!
   * Output directory into which compiled property file classes are generated.
   * By default, for packaging type <code>jangaroo-app</code>, the directory
   * <code>${project.build.directory}/app/locale</code>
   * is used, for packaging type <code>swc</code>, it is
   * <code>${project.build.directory}/packages/${package.name}/locale</code>.
   */
  private File getLocalizedOutputDirectory() {
    return new File(getOutputDirectory(), SenchaUtils.SENCHA_LOCALE_PATH);
  }

  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  @Nullable
  protected abstract File getApiOutputDirectory();

  public boolean isMigrateToTypeScript() { return migrateToTypeScript; }

  public boolean isSuppressCommentedActionScriptCode() { return suppressCommentedActionScriptCode; }

  protected File getCatalogOutputDirectory() {
    return catalogOutputDirectory;
  }

  /**
   * Runs the compile mojo
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException {

    final Log log = getLog();

    if (getCompileSourceRoots().isEmpty()) {
      log.info("No sources to compile");
      return;
    }

    // ----------------------------------------------------------------------
    // Create the compiler configuration
    // ----------------------------------------------------------------------

    JoocConfiguration configuration = createJoocConfiguration(log);
    if (configuration == null) {
      return;
    }

    Jooc jooc = new Jooc(configuration, new AbstractCompileLog() {
      @Override
      protected void doLogError(String msg) {
        log.error(msg);
      }

      @Override
      public void warning(String msg) {
        log.warn(msg);
      }
    });

    int result = compile(jooc);

    if ((result != CompilationResult.RESULT_CODE_OK) && failOnError) {
      log.info("-------------------------------------------------------------");
      if (result == CompilationResult.RESULT_CODE_COMPILATION_FAILED) {
        log.error("There were compile errors (see log above).");
      } else {
        log.error("Internal Jangaroo compiler error: " + result + "\nSee log for error details.");
      }
      log.info("-------------------------------------------------------------");
      throw new MojoFailureException("Compilation failed");
    }
  }

  protected JoocConfiguration createJoocConfiguration(Log log) throws MojoExecutionException, MojoFailureException {
    JoocConfiguration configuration = new JoocConfiguration();

    configuration.setEnableAssertions(enableAssertions);
    configuration.setAllowDuplicateLocalVariables(allowDuplicateLocalVariables);
    configuration.setVerbose(verbose);
    configuration.setExcludeClassByDefault(excludeClassByDefault);
    configuration.setGenerateSourceMaps(generateSourceMaps);
    configuration.setKeepGeneratedActionScriptDirectory(keepGeneratedActionScriptDirectory);
    configuration.setMigrateToTypeScript(migrateToTypeScript);
    configuration.setExtNamespace(extNamespace);
    configuration.setExtSassNamespace(extSassNamespace != null ? extSassNamespace : extNamespace);
    configuration.setUseEcmaParameterInitializerSemantics(useEcmaParameterInitializerSemantics);
    configuration.setSuppressCommentedActionScriptCode(suppressCommentedActionScriptCode);
    configuration.setNpmPackageNameReplacers(
            npmPackageNameReplacers.stream()
                    .map(config -> new SearchAndReplace(Pattern.compile(config.getSearch()), config.getReplace()))
            .collect(Collectors.toList())
    );

    if (StringUtils.isNotEmpty(debuglevel)) {
      try {
        configuration.setDebugMode(DebugMode.valueOf(debuglevel.toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("The specified debug level: '" + debuglevel
                + "' is unsupported. " + "Legal values are 'none', 'lines', and 'source'.");
      }
    }

    if (StringUtils.isNotEmpty(autoSemicolon)) {
      try {
        configuration.setSemicolonInsertionMode(SemicolonInsertionMode.valueOf(autoSemicolon.toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("The specified semicolon insertion mode: '" + autoSemicolon
                + "' is unsupported. " + "Legal values are 'error', 'warn', and 'quirks'.");
      }
    }

    if (StringUtils.isNotEmpty(publicApiViolations)) {
      try {
        configuration.setPublicApiViolationsMode(PublicApiViolationsMode.valueOf(publicApiViolations.toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("The specified public API violations mode: '" + publicApiViolations
                + "' is unsupported. " + "Legal values are 'error', 'warn', and 'allow'.");
      }
    }

    HashSet<File> sources = new HashSet<>();
    log.debug("starting source inclusion scanner");
    sources.addAll(computeStaleSources(staleMillis));


    // SASS files
    Map<String, File> sassSourcePathByType = new HashMap<>();
    Map<String, File> sassOutputDirectoryByType = new HashMap<>();
    Map<String, List<File>> sassSourceFilesByType = new HashMap<>();
    for (String sassType : Lists.newArrayList("var", "src")) {
      File sassSourceFolder = senchaSrcDir.toPath().resolve("sass").resolve(sassType).toFile();
      sassSourcePathByType.put(sassType, sassSourceFolder);
      sassSourceFilesByType.put(sassType, listFilesRecursive(sassSourceFolder));
      sassOutputDirectoryByType.put(sassType, getOutputDirectory().toPath().resolve("sass").resolve(sassType).toFile());
    }

    if (sources.isEmpty() && sassSourceFilesByType.values().stream().mapToInt(List::size).sum() == 0) {
      log.info("Nothing to compile - all classes are up to date");
      return null;
    }
    configuration.setSourceFiles(new ArrayList<>(sources));
    try {
      configuration.setSourcePath(getCompileSourceRoots());
    } catch (IOException e) {
      throw new MojoFailureException("could not canonicalize source paths: " + getCompileSourceRoots(), e);
    }
    configuration.setClassPath(getActionScriptClassPath());
    configuration.setOutputDirectory(getClassesOutputDirectory());
    configuration.setLocalizedOutputDirectory(getLocalizedOutputDirectory());
    configuration.setApiOutputDirectory(getApiOutputDirectory());

    configuration.setSassSourceFilesByType(sassSourceFilesByType);
    try {
      configuration.setSassSourcePathByType(sassSourcePathByType);
    } catch (IOException e) {
      throw new MojoFailureException("could not canonicalize sass source paths: " + sassSourcePathByType, e);
    }
    try {
      configuration.setSassOutputDirectoryByType(sassOutputDirectoryByType);
    } catch (IOException e) {
      throw new MojoFailureException("could not canonicalize sass output directories: " + sassOutputDirectoryByType, e);
    }

    List<NamespaceConfiguration> allNamespaces = new ArrayList<>();
    if (getNamespaces() != null) {
      allNamespaces.addAll(Arrays.asList(getNamespaces()));
    }
    String configClassPackage = findConfigClassPackageInExmlPluginConfiguration();
    if (configClassPackage != null) {
      String namespace = Exmlc.EXML_CONFIG_URI_PREFIX + configClassPackage;
      getLog().info(String.format("Adding namespace %s derived from %s configuration.",
              namespace, EXML_MAVEN_PLUGIN_ARTIFACT_ID));
      allNamespaces.add(new NamespaceConfiguration(namespace, null));
    }
    configuration.setNamespaces(allNamespaces);

    if (log.isDebugEnabled()) {
      log.debug("Source path: " + configuration.getSourcePath().toString().replace(',', '\n'));
      log.debug("Class path: " + configuration.getClassPath().toString().replace(',', '\n'));
      log.debug("Output directory: " + configuration.getOutputDirectory());
      if (configuration.getApiOutputDirectory() != null) {
        log.debug("API output directory: " + configuration.getApiOutputDirectory());
      }
    }
    return configuration;
  }

  private String findConfigClassPackageInExmlPluginConfiguration() {
    String configClassPackage = null;
    @SuppressWarnings("unchecked")
    List<Plugin> plugins = getProject().getBuildPlugins();
    for (Plugin plugin : plugins) {
      if (JANGAROO_GROUP_ID.equals(plugin.getGroupId()) && EXML_MAVEN_PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId())) {
        Object exmlPluginConfiguration = plugin.getConfiguration();
        if (exmlPluginConfiguration instanceof Xpp3Dom) {
          Xpp3Dom configClassPackageElement = ((Xpp3Dom) exmlPluginConfiguration).getChild("configClassPackage");
          if (configClassPackageElement != null) {
            configClassPackage = configClassPackageElement.getValue();
            if (configClassPackage != null) {
              configClassPackage = configClassPackage.trim();
              if (!configClassPackage.isEmpty()) {
                break;
              }
            }
          }
        }
      }
    }
    return configClassPackage;
  }

  protected abstract List<File> getActionScriptClassPath();

  private int compile(Jooc jooc) throws MojoExecutionException {
    File outputDirectory = jooc.getConfig().getOutputDirectory();
    FileHelper.ensureDirectory(outputDirectory);

    // create api output directory if it does not exist
    File apiOutputDirectory = getApiOutputDirectory();
    if (apiOutputDirectory != null) {
      FileHelper.ensureDirectory(apiOutputDirectory);
    }

    final List<File> sources = jooc.getConfig().getSourceFiles();

    final Log log = getLog();
    log.info("Compiling " + sources.size() +
            " joo source file"
            + (sources.size() == 1 ? "" : "s")
            + " to " + outputDirectory);

    return jooc.run().getResultCode();
  }

  private List<File> computeStaleSources(int staleMillis) throws MojoExecutionException {
    File outputDirectory = getApiOutputDirectory();
    String outputFileSuffix = Jooc.AS_SUFFIX;
    if (outputDirectory == null) {
      outputDirectory = getClassesOutputDirectory();
      outputFileSuffix = Jooc.getOutputSuffix(isMigrateToTypeScript());
    }
    List<File> compileSourceRoots = getCompileSourceRoots();
    List<File> staleFiles = new ArrayList<>();
    staleFiles.addAll(getMavenPluginHelper().computeStaleSources(compileSourceRoots, getIncludes(), getExcludes(), outputDirectory, Jooc.AS_SUFFIX, outputFileSuffix, staleMillis));
    staleFiles.addAll(getMavenPluginHelper().computeStaleSources(compileSourceRoots, getIncludes(), getExcludes(), outputDirectory, Jooc.MXML_SUFFIX, outputFileSuffix, staleMillis));
    staleFiles.addAll(getMavenPluginHelper().computeStalePropertiesSources(compileSourceRoots, getIncludes(), getExcludes(), getLocalizedOutputDirectory(), staleMillis));
    return staleFiles;
  }

  private static List<File> listFilesRecursive(File folder) throws MojoExecutionException {
    List<File> recursiveFolderContents = new ArrayList<>();
    if (!folder.exists()) {
      return recursiveFolderContents;
    }
    if (!folder.isDirectory()) {
      throw new MojoExecutionException("Not a directory: " + folder);
    }
    File[] folderContents = folder.listFiles();
    if (folderContents == null) {
      throw new MojoExecutionException("Could not read directory: " + folder);
    }
    for (File file : folderContents) {
      if (file.isDirectory()) {
        recursiveFolderContents.addAll(listFilesRecursive(file));
      } else {
        recursiveFolderContents.add(file);
      }
    }
    return recursiveFolderContents;
  }

  protected abstract Set<String> getIncludes();

  protected abstract Set<String> getExcludes();

}
