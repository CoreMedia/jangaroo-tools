package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
   * Set "enableGuessingMembers" to "true" in order to generate "this." before all top-level identifiers
   * that cannot be resolved in the current compilation unit.
   * If "guessClasses" is also "true", "this." is only added for top-level identifiers not starting
   * with an upper case letter; these are considered classes or types that are already in scope.
   *
   * @parameter default-value="true"
   */
  private boolean enableGuessingMembers;
  /**
   * For undeclared top-level identifiers starting with an upper case letter, only issue a warning
   * that they are assumed to be already in scope, i.e. classes or interfaces in the same
   * package, in the top-level package, or imported through a * import.
   * In combination with "enableGuessingMembers", undeclared identifiers starting with an upper case letter
   * are not assumed to be inherited members if this flag is "true".
   *
   * @parameter default-value="true"
   */
  private boolean enableGuessingClasses;
  /**
   * If "enableGuessingTypeCasts" is "true", function calls with undeclared identifiers starting with an upper case
   * letter are assumed to be type casts. In the generated code, such type casts only appear as comments.
   *
   * @parameter default-value="false"
   */
  private boolean enableGuessingTypeCasts;
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


  public abstract String getOutputFileName();

  protected abstract List<File> getCompileSourceRoots();

  protected abstract File getOutputDirectory();

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
    configuration.setEnableGuessingMembers(enableGuessingMembers);
    configuration.setEnableGuessingClasses(enableGuessingClasses);
    configuration.setEnableGuessingTypeCasts(enableGuessingTypeCasts);
    configuration.setVerbose(verbose);

    configuration.setOutputDirectory(getOutputDirectory());
    configuration.setOutputFileName(getOutputFileName());

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

    if (getLog().isDebugEnabled()) {
      log.debug("Source directories: " + getCompileSourceRoots().toString().replace(',', '\n'));
      log.debug("Output file: " + configuration.getOutputFile());
      log.debug("Output directory: " + configuration.getOutputDirectory());
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

    // create output directory if it does not exist
    if (!getOutputDirectory().exists())
      if (!getOutputDirectory().mkdirs())
        throw new MojoExecutionException("Failed to create output directory " + getOutputDirectory().getAbsolutePath());

    configuration.setMergeOutput(true);
    int result = compile(configuration);
    boolean compilationError = (result != Jooc.RESULT_CODE_OK);

    configuration.setMergeOutput(false);
    result = compile(configuration);
    compilationError &= (result != Jooc.RESULT_CODE_OK);

    List messages = Collections.emptyList();

    if (compilationError && failOnError) {
      getLog().info("-------------------------------------------------------------");
      getLog().error("COMPILATION ERROR : ");
      getLog().info("-------------------------------------------------------------");
      if (messages != null) {
        for (Iterator i = messages.iterator(); i.hasNext();) {
          CompilerError message = (CompilerError) i.next();

          getLog().error(message.toString());

        }
        getLog().info(messages.size() + ((messages.size() > 1) ? " errors " : "error"));
        getLog().info("-------------------------------------------------------------");
      }
      throw new MojoFailureException("Compilation failed");
    } else {
      for (Iterator i = messages.iterator(); i.hasNext();) {
        CompilerError message = (CompilerError) i.next();

        getLog().warn(message.toString());
      }
    }
  }

  private int compile(JoocConfiguration config) {
    final List<File> sources = config.getSourceFiles();
    File output = config.isMergeOutput() ? config.getOutputFile() : getOutputDirectory();

    log.info("Compiling " + sources.size() +
            " joo source file"
            + (sources.size() == 1 ? "" : "s")
            + " to " + output);

    Jooc jooc = new Jooc();
    return jooc.run(config);
  }

  private Set<File> computeStaleSources(SourceInclusionScanner scanner) throws MojoExecutionException {


    File outputDirectory = getOutputDirectory();


    scanner.addSourceMapping(new SuffixMapping(Jooc.INPUT_FILE_SUFFIX, Jooc.OUTPUT_FILE_SUFFIX));
    getLog().debug("Searching for");
    Set<File> staleSources = new HashSet<File>();

    for (File rootFile : getCompileSourceRoots()) {
      if (!rootFile.isDirectory()) {
        continue;
      }

      try {
        getLog().debug("scanner.getIncludedSources(" + rootFile + ", " + outputDirectory + ")");
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

  protected SourceInclusionScanner getSourceInclusionScanner(Set includes, Set excludes, int staleMillis) {
    SourceInclusionScanner scanner = null;

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

  protected SourceInclusionScanner getSourceInclusionScanner(Set includes, Set excludes, String inputFileEnding) {
    SourceInclusionScanner scanner = null;

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
}
