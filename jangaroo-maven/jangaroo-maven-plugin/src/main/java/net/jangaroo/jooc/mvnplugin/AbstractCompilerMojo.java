package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.AbstractCompileLog;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.api.CompilationResult;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.NamespaceConfiguration;
import net.jangaroo.jooc.config.PublicApiViolationsMode;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Super class for mojos compiling Jangaroo sources.
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
public abstract class AbstractCompilerMojo extends AbstractJangarooMojo {
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String EXML_MAVEN_PLUGIN_ARTIFACT_ID = "exml-maven-plugin";

  /**
   * Indicates whether the build will fail if there are compilation errors.
   */
  @SuppressWarnings("FieldCanBeLocal")
  @Parameter(property = "maven.compiler.failOnError", defaultValue = "true")
  private boolean failOnError;

  /**
   * Set "enableAssertions" to "true" in order to generate runtime checks for assert statements.
   */
  @Parameter(property = "maven.compile.ea", defaultValue = "false")
  private boolean enableAssertions;

  /**
   * Set "allowDuplicateLocalVariables" to "true" in order to allow multiple declarations of local variables
   * within the same scope.
   */
  @Parameter(defaultValue = "false")
  private boolean allowDuplicateLocalVariables;

  /**
   * "publicApiViolations" controls how the compiler reacts on usage of non-public API classes,
   * i.e. classes annotated with <code>[ExcludeClass]</code>.
   * It can take the values "warn" to log a warning whenever such a class is used, "allow" to suppress such warnings,
   * and "error" to stop the build with an error.
   */
  @Parameter(defaultValue = "warn")
  private String publicApiViolations;

  /**
   * If set to "true", the compiler will add an [ExcludeClass] annotation to any
   * API stub whose source class contains neither an [PublicApi] nor an [ExcludeClass]
   * annotation.
   */
  @Parameter(property = "maven.compiler.excludeClassByDefault", defaultValue = "false")
  private boolean excludeClassByDefault;

  /**
   * Let the compiler generate JavaScript source maps that allow debuggers
   * (currently only Google Chrome) to show the original ActionScript source code during
   * debugging.
   * Set to <code>false</code> to disable this feature to decrease build time and artifact size.
   */
  @Parameter(property = "maven.compiler.generateSourceMaps", defaultValue = "true")
  private boolean generateSourceMaps;

  /**
   * If set to "true", the compiler will generate more detailed progress information.
   */
  @Parameter(property = "maven.compiler.verbose", defaultValue = "false")
  private boolean verbose;

  /**
   * Sets the granularity in milliseconds of the last modification
   * date for testing whether a source needs recompilation.
   */
  @Parameter(property = "lastModGranularityMs", defaultValue = "0")
  private int staleMillis;

  /**
   * Keyword list to be appended to the -g  command-line switch. Legal values are one of the following keywords: none, lines, or source.
   * If debuglevel is not specified, by default, nothing will be appended to -g. If debug is not turned on, this attribute will be ignored.
   */
  @Parameter(defaultValue = "source")
  private String debuglevel;

  /**
   * Keyword list to be appended to the -autosemicolon  command-line switch. Legal values are one of the following keywords: error, warn (default), or quirks.
   */
  @Parameter(defaultValue = "warn")
  private String autoSemicolon;

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

  public abstract String getModuleClassesJsFileName();

  public File getModuleClassesJsFile() {
    return new File(getOutputDirectory(), getModuleClassesJsFileName());
  }

  protected abstract List<File> getCompileSourceRoots();

  protected abstract File getOutputDirectory();

  protected File getClassesOutputDirectory() {
    return new File(getOutputDirectory(), "joo/classes");
  }

  protected abstract File getTempClassesOutputDirectory();

  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  protected abstract File getApiOutputDirectory();

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
    boolean compilationError = (result != CompilationResult.RESULT_CODE_OK);

    if (!compilationError) {
      // for now, always set debug mode to "false" for concatenated file:
      configuration.setDebugMode(null);
      configuration.setOutputDirectory(getTempClassesOutputDirectory());
      configuration.setApiOutputDirectory(null);



      result = compile(jooc);
      if (result == CompilationResult.RESULT_CODE_OK) {
        buildOutputFile(getTempClassesOutputDirectory(), getModuleClassesJsFile());
      }

      compilationError = (result != CompilationResult.RESULT_CODE_OK);
    }

    List<CompilerMessage> messages = Collections.emptyList();

    if (compilationError && failOnError) {
      log.info("-------------------------------------------------------------");
      log.error("COMPILATION ERROR : ");
      log.info("-------------------------------------------------------------");
      for (CompilerMessage message : messages) {
        log.error(message.toString());
      }
      log.info(messages.size() + ((messages.size() > 1) ? " errors " : "error"));
      log.info("-------------------------------------------------------------");
      throw new MojoFailureException("Compilation failed");
    } else {
      for (CompilerMessage message : messages) {
        log.warn(message.toString());
      }
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

    HashSet<File> sources = new HashSet<File>();
    log.debug("starting source inclusion scanner");
    sources.addAll(computeStaleSources(staleMillis));
    if (sources.isEmpty()) {
      log.info("Nothing to compile - all classes are up to date");
      return null;
    }
    configuration.setSourceFiles(new ArrayList<File>(sources));
    try {
      configuration.setSourcePath(getCompileSourceRoots());
    } catch (IOException e) {
      throw new MojoFailureException("could not canonicalize source paths: " + getCompileSourceRoots(), e);
    }
    configuration.setClassPath(getActionScriptClassPath());
    configuration.setOutputDirectory(getClassesOutputDirectory());
    configuration.setApiOutputDirectory(getApiOutputDirectory());

    List<NamespaceConfiguration> allNamespaces = new ArrayList<NamespaceConfiguration>();
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

  private void buildOutputFile(File tempOutputDir, File outputFile) throws MojoExecutionException {
    final Log log = getLog();
    if (log.isDebugEnabled()) {
      log.debug("Output file: " + outputFile);
    }

    try {
      // If the directory where the output file is going to land
      // doesn't exist then create it.
      File outputFileDirectory = outputFile.getParentFile();

      if (!outputFileDirectory.exists()) {
        //noinspection ResultOfMethodCallIgnored
        if (outputFileDirectory.mkdirs()) {
          log.debug("created output directory " + outputFileDirectory);
        }
      }

      @SuppressWarnings({"unchecked"})
      // resource bundle classes should always be loaded dynamically:
              List<File> files = FileUtils.getFiles(tempOutputDir, "**/*.js", "**/*_properties_*.js");
      // We should now have all the files we want to concat so let's do it.
      Writer fos = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
      int tempOutputDirPathLength = tempOutputDir.getAbsolutePath().length() + 1;
      for (File file : files) {
        String className = file.getAbsolutePath();
        className = className.substring(tempOutputDirPathLength, className.length() - ".js".length());
        className = className.replace(File.separatorChar, '.');
        fos.write("// class " + className + "\n");
        IOUtil.copy(new FileInputStream(file), fos, "UTF-8");
        fos.write('\n');
      }
      fos.close();
    } catch (IOException e) {
      throw new MojoExecutionException("could not build output file " + outputFile + ": " + e.toString(), e);
    }
  }

  private int compile(Jooc jooc) throws MojoExecutionException {
    File outputDirectory = jooc.getConfig().getOutputDirectory();

    // create output directory if it does not exist
    if (!outputDirectory.exists()) {
      if (!outputDirectory.mkdirs()) {
        throw new MojoExecutionException("Failed to create output directory " + outputDirectory.getAbsolutePath());
      }
    }

    // create api output directory if it does not exist
    File apiOutputDirectory = getApiOutputDirectory();
    if (apiOutputDirectory != null && !apiOutputDirectory.exists()) {
      if (!apiOutputDirectory.mkdirs()) {
        throw new MojoExecutionException("Failed to create api output directory " + apiOutputDirectory.getAbsolutePath());
      }
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
      outputFileSuffix = Jooc.OUTPUT_FILE_SUFFIX;
    }
    List<File> compileSourceRoots = getCompileSourceRoots();


    List<File> staleFiles = new ArrayList<File>();
    staleFiles.addAll(getMavenPluginHelper().computeStaleSources(compileSourceRoots, getIncludes(), getExcludes(), outputDirectory, Jooc.AS_SUFFIX, outputFileSuffix, staleMillis));
    staleFiles.addAll(getMavenPluginHelper().computeStaleSources(compileSourceRoots, getIncludes(), getExcludes(), outputDirectory, Jooc.MXML_SUFFIX, outputFileSuffix, staleMillis));
    return staleFiles;
  }

  protected abstract Set<String> getIncludes();

  protected abstract Set<String> getExcludes();

}
