package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
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
import java.util.*;

/**
 * Super class for mojos compiling Jangaroo sources.
 */
public abstract class AbstractCompilerMojo extends AbstractMojo {
  private Log log = getLog();
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
   * If set to "true", the compiler will generate more detailed progress information.
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
   * Set to "true" to produce one single output file for all generated compiled classes.
   * @parameter default-value="false"
   */
  private boolean mergeOutput;

  public abstract String getOutputFileName();

  protected abstract List getCompileSourceRoots();

  protected abstract File getOutputDirectory();

  /**
   * Runs the compile mojo
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    List compileSourceRoots = getCompileSourceRoots();

    if (compileSourceRoots.isEmpty()) {
      getLog().info("No sources to compile");

      return;
    }

    // ----------------------------------------------------------------------
    // Create the compiler configuration
    // ----------------------------------------------------------------------

    JoocConfiguration configuration = new JoocConfiguration();

    configuration.setDebug(debug);
    configuration.setVerbose(verbose);

    configuration.setOutputDirectory(getOutputDirectory());
    configuration.setMergeOutput(mergeOutput);
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
      log.debug("Source directories: " + compileSourceRoots.toString().replace(',', '\n'));

      if (configuration.isMergeOutput()) {
        log.debug("Output file: " + configuration.getOutputFile());
      } else {
        log.debug("Output directory: " + configuration.getOutputDirectory());
      }
    }

    if (configuration.isMergeOutput()) {
      if (configuration.getOutputFileName() == null) {
        getLog().error("<outputFileName> must not be empty when merging output into one file.");
        return;
      }

      if (configuration.isDebugLines()) {
        getLog().info("When output is merged into one file, debug mode 'lines' is not effective.");
      }

    } else {
      if (configuration.getOutputFileName() != null) {
        getLog().info("Generating one file per compilation unit. <outputFileName> will be ingored.");
      }
    }

    HashSet<File> sources = new HashSet<File>();

    sources.addAll( computeStaleSources(configuration, getSourceInclusionScanner(staleMillis)) );

    if (!sources.isEmpty() && configuration.isMergeOutput()) {
      getLog().info( "RESCANNING!" );

      sources.clear();
      sources.addAll( computeStaleSources(configuration, getSourceInclusionScanner(Jooc.INPUT_FILE_SUFFIX_NO_DOT)) );
    }

    configuration.setSourceFiles(new ArrayList<File>(sources));

    if (sources.isEmpty()) {
      getLog().info("Nothing to compile - all classes are up to date");
      return;
    }

    // create output directory if it does not exist
    if (!getOutputDirectory().exists())
      if (!getOutputDirectory().mkdirs())
        throw new MojoExecutionException("Failed to create output directory " + getOutputDirectory().getAbsolutePath());

    int result = compile(configuration);
    boolean compilationError = (result != Jooc.RESULT_CODE_OK);
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

  private Set computeStaleSources(JoocConfiguration configuration, SourceInclusionScanner scanner)  throws MojoExecutionException {
    CompilerOutputStyle outputStyle = configuration.isMergeOutput()
      ? CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES
      : CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE;

    File outputDirectory = getOutputDirectory();

    if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE) {

      scanner.addSourceMapping(new SuffixMapping(Jooc.INPUT_FILE_SUFFIX, Jooc.OUTPUT_FILE_SUFFIX));

    } else if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES) {

      scanner.addSourceMapping(new SingleTargetSourceMapping(Jooc.INPUT_FILE_SUFFIX, configuration.getOutputFileName()));

    } else {
      throw new MojoExecutionException( "Unknown compiler output style: '" + outputStyle + "'." );
    }

    Set staleSources = new HashSet();

    for (Iterator it = getCompileSourceRoots().iterator(); it.hasNext();) {
      File rootFile = (File) it.next();

      if (!rootFile.isDirectory()) {
        continue;
      }

      try {
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

  protected abstract SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding);

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
