package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.JoocOptions;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Super class for mojos compiling Jangaroo sources.
 */
public abstract class AbstractCompilerMojo extends AbstractMojo {
  private Log log = getLog();

  /**
   * The Maven project object
   *
   * @parameter expression="${project}"
   */
  private MavenProject project;

  /**
   * Indicates whether the build will fail if there are compilation errors.
   *
   * @parameter expression="${maven.compiler.failOnError}" default-value="true"
   */
  private boolean failOnError = true;

  /**
   * Set "debug" to "true" in order to create debuggable output. See "debuglevel" parameter.
   *
   * @parameter expression="${maven.compile.debug}" default-value="true"
   */
  private boolean debug;

  /**
   * Set "enableAssertions" to "true" in order to generate runtime checks for assert statements.
   *
   * @parameter expression="${maven.compile.ea}" default-value="false"
   */
  private boolean enableAssertions;

  /**
   * Set "allowDuplicateLocalVariables" to "true" in order to allow multiple declarations of local variables
   * within the same scope.
   *
   * @parameter default-value="false"
   */
  private boolean allowDuplicateLocalVariables;

  /**
   * If set to "true", the compiler will generate more detailed progress information.
   *
   * @parameter expression="${maven.compiler.verbose}" default-value="false"
   */
  private boolean verbose;

  /**
   * Sets the granularity in milliseconds of the last modification
   * date for testing whether a source needs recompilation.
   *
   * @parameter expression="${lastModGranularityMs}" default-value="0"
   */
  private int staleMillis;

  /**
   * Keyword list to be appended to the -g  command-line switch. Legal values are one of the following keywords: none, lines, or source.
   * If debuglevel is not specified, by default, nothing will be appended to -g. If debug is not turned on, this attribute will be ignored.
   *
   * @parameter default-value="source"
   */
  private String debuglevel;

  /**
   * Keyword list to be appended to the -autosemicolon  command-line switch. Legal values are one of the following keywords: error, warn (default), or quirks.
   *
   * @parameter default-value="warn"
   */
  private String autoSemicolon;

  /**
   * Output directory for all generated ActionScript3 files to compile.
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

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

  /**
   * Runs the compile mojo
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException {


    if (getCompileSourceRoots().isEmpty()) {
      getLog().info("No sources to compile");

      return;
    }


    // ----------------------------------------------------------------------
    // Create the compiler configuration
    // ----------------------------------------------------------------------

    JoocConfiguration configuration = new JoocConfiguration();

    configuration.setDebug(debug);
    configuration.setEnableAssertions(enableAssertions);
    configuration.setAllowDuplicateLocalVariables(allowDuplicateLocalVariables);
    configuration.setVerbose(verbose);

    if (debug && StringUtils.isNotEmpty(debuglevel)) {
      if (debuglevel.equalsIgnoreCase("lines")) {
        configuration.setDebugLines(true);
      } else if (debuglevel.equalsIgnoreCase("source")) {
        configuration.setDebugLines(true);
        configuration.setDebugSource(true);
      } else if (!debuglevel.equalsIgnoreCase("none")) {
        throw new IllegalArgumentException("The specified debug level: '" + debuglevel
          + "' is unsupported. " + "Legal values are 'none', 'lines', and 'source'.");
      }
    }

    if (StringUtils.isNotEmpty(autoSemicolon)) {
      if (autoSemicolon.equalsIgnoreCase("error")) {
        configuration.setSemicolonInsertionMode(JoocOptions.SemicolonInsertionMode.ERROR);
      } else if (autoSemicolon.equalsIgnoreCase("warn")) {
        configuration.setSemicolonInsertionMode(JoocOptions.SemicolonInsertionMode.WARN);
      } else if (autoSemicolon.equalsIgnoreCase("quirks")) {
        configuration.setSemicolonInsertionMode(JoocOptions.SemicolonInsertionMode.QUIRKS);
      } else {
        throw new IllegalArgumentException("The specified semicolon insertion mode: '" + autoSemicolon
          + "' is unsupported. " + "Legal values are 'error', 'warn', and 'quirks'.");
      }
    }

    HashSet<File> sources = new HashSet<File>();
    getLog().debug("starting source inclusion scanner");
    sources.addAll(computeStaleSources(getSourceInclusionScanner(staleMillis)));
    if (sources.isEmpty()) {
      getLog().info("Nothing to compile - all classes are up to date");
      return;
    }
    configuration.setSourceFiles(new ArrayList<File>(sources));
    configuration.setSourcePath(getCompileSourceRoots());
    configuration.setClassPath(getActionScriptClassPath());
    configuration.setOutputDirectory(getClassesOutputDirectory());
    configuration.setApiOutputDirectory(getApiOutputDirectory());

    if (getLog().isDebugEnabled()) {
      log.debug("Source path: " + configuration.getSourcePath().toString().replace(',', '\n'));
      log.debug("Class path: " + configuration.getClassPath().toString().replace(',', '\n'));
      log.debug("Output directory: " + configuration.getOutputDirectory());
      if (configuration.getApiOutputDirectory() != null) {
        log.debug("API output directory: " + configuration.getApiOutputDirectory());
      }
    }

    int result = compile(configuration);
    boolean compilationError = (result != Jooc.RESULT_CODE_OK);

    if (!compilationError) {
      // for now, always set debug mode to "false" for concatenated file:
      configuration.setDebug(false);
      configuration.setDebugLines(false);
      configuration.setDebugSource(false);
      configuration.setOutputDirectory(getTempClassesOutputDirectory());
      configuration.setApiOutputDirectory(null);
      result = compile(configuration);
      if (result == Jooc.RESULT_CODE_OK) {
        buildOutputFile(getTempClassesOutputDirectory(), getModuleClassesJsFile());
      }

      compilationError &= (result != Jooc.RESULT_CODE_OK);
    }

    List<CompilerError> messages = Collections.emptyList();

    if (compilationError && failOnError) {
      getLog().info("-------------------------------------------------------------");
      getLog().error("COMPILATION ERROR : ");
      getLog().info("-------------------------------------------------------------");
      if (messages != null) {
        for (CompilerError message : messages) {
          getLog().error(message.toString());
        }
        getLog().info(messages.size() + ((messages.size() > 1) ? " errors " : "error"));
        getLog().info("-------------------------------------------------------------");
      }
      throw new MojoFailureException("Compilation failed");
    } else {
      for (CompilerError message : messages) {
        getLog().warn(message.toString());
      }
    }
  }

  protected List<File> getActionScriptClassPath() {
    List<File> classPath = new ArrayList<File>();
    classPath.add(new File(project.getBasedir(), "src/main/joo-api"));
    Collection<Artifact> dependencies = getArtifacts();
    for (Artifact dependency : dependencies) {
      if (getLog().isDebugEnabled()) {
        getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      }
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        if (getLog().isDebugEnabled()) {
          getLog().debug("adding to classpath: jangaroo dependency [" + dependency.toString() + "]");
        }
        classPath.add(dependency.getFile());
      }
    }
    return classPath;
  }

  private void buildOutputFile(File tempOutputDir, File outputFile) throws MojoExecutionException {
    if (getLog().isDebugEnabled()) {
      log.debug("Output file: " + outputFile);
    }

    try {
      // If the directory where the output file is going to land
      // doesn't exist then create it.
      File outputFileDirectory = outputFile.getParentFile();

      if (!outputFileDirectory.exists()) {
        //noinspection ResultOfMethodCallIgnored
        outputFileDirectory.mkdirs();
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
      throw new MojoExecutionException(e.toString());
    }
  }

  private int compile(JoocConfiguration config) throws MojoExecutionException {
    File outputDirectory = config.getOutputDirectory();

    // create output directory if it does not exist
    if (!outputDirectory.exists())
      if (!outputDirectory.mkdirs())
        throw new MojoExecutionException("Failed to create output directory " + outputDirectory.getAbsolutePath());

    // create api output directory if it does not exist
    File apiOutputDirectory = getApiOutputDirectory();
    if (apiOutputDirectory != null && !apiOutputDirectory.exists())
      if (!apiOutputDirectory.mkdirs())
        throw new MojoExecutionException("Failed to create api output directory " + apiOutputDirectory.getAbsolutePath());

    final List<File> sources = config.getSourceFiles();

    log.info("Compiling " + sources.size() +
      " joo source file"
      + (sources.size() == 1 ? "" : "s")
      + " to " + outputDirectory);

    Jooc jooc = new Jooc();
    return jooc.run(config);
  }

  private Set<File> computeStaleSources(SourceInclusionScanner scanner) throws MojoExecutionException {


    File outputDirectory = getClassesOutputDirectory();


    scanner.addSourceMapping(new SuffixMapping(Jooc.INPUT_FILE_SUFFIX, Jooc.OUTPUT_FILE_SUFFIX));
    getLog().debug("Searching for");
    Set<File> staleSources = new HashSet<File>();

    for (File rootFile : getCompileSourceRoots()) {
      if (!rootFile.isDirectory()) {
        continue;
      }

      try {
        getLog().debug("scanner.getIncludedSources(" + rootFile + ", " + outputDirectory + ")");
        //noinspection unchecked
        staleSources.addAll(scanner.getIncludedSources(rootFile, outputDirectory));
      }
      catch (InclusionScanException e) {
        throw new MojoExecutionException(
          "Error scanning source root: \'" + rootFile.getAbsolutePath() + "\' " + "for stale files to recompile.", e);
      }
    }

    return staleSources;
  }

  protected abstract SourceInclusionScanner getSourceInclusionScanner(int staleMillis);

  protected SourceInclusionScanner getSourceInclusionScanner(Set<String> includes, Set<String> excludes, int staleMillis) {
    SourceInclusionScanner scanner;

    if (includes.isEmpty() && excludes.isEmpty()) {
      scanner = new StaleSourceScanner(staleMillis);
    } else {
      if (includes.isEmpty()) {
        includes.add("**/*" + Jooc.INPUT_FILE_SUFFIX);
      }
      scanner = new StaleSourceScanner(staleMillis, includes, excludes);
    }

    return scanner;
  }

  protected SourceInclusionScanner getSourceInclusionScanner(Set<String> includes, Set<String> excludes, String inputFileEnding) {
    SourceInclusionScanner scanner;

    if (includes.isEmpty() && excludes.isEmpty()) {
      includes = Collections.singleton("**/*." + inputFileEnding);
      scanner = new SimpleSourceInclusionScanner(includes, Collections.EMPTY_SET);
    } else {
      if (includes.isEmpty()) {
        includes.add("**/*." + inputFileEnding);
      }
      scanner = new SimpleSourceInclusionScanner(includes, excludes);
    }

    return scanner;
  }

  @SuppressWarnings({"unchecked"})
  private Set<Artifact> getArtifacts() {
    return (Set<Artifact>) project.getArtifacts();
  }

  protected boolean isJangarooPackaging() {
    return Types.JANGAROO_TYPE.equals(project.getPackaging());
  }
}
