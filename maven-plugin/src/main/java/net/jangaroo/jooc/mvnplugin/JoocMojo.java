package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JoocConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources from .js2 to .js, based on the maven-compiler-plugin.
 *
 * @goal compile
 * @phase compile
 */
public class JoocMojo extends AbstractMojo {
  private Log log = getLog();

  /**
   * Indicates whether the build will fail if there are compilation errors; defaults to true.
   *
   * @parameter expression="${maven.compiler.failOnError}" default-value="true"
   */
  private boolean failOnError = true;

  /**
   * @parameter expression="${maven.compile.debug}" default-value="true"
   */
  private boolean debug;

  /**
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
   * @parameter expression="source"
   */
  private String debuglevel;

  /**
   * @parameter expression="${project.build.directory}/${project.build.finalName}/js"
   */
  private File outputDirectory;

  /**
   * @parameter expression="${basedir}/src/main/js2"
   */
  private File sourceDirectory;

  /**
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */

  /**
   * A list of inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set includes = new HashSet();

  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set excludes = new HashSet();

  protected List getCompileSourceRoots() {
    return Collections.singletonList(sourceDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }


  public void execute() throws MojoExecutionException, MojoFailureException {
    List compileSourceRoots = getCompileSourceRoots();

    if (compileSourceRoots.isEmpty()) {
      getLog().info("No sources to compile");

      return;
    }

    if (getLog().isDebugEnabled()) {
      log.debug("Source directories: " + compileSourceRoots.toString().replace(',', '\n'));
      log.debug("Output directory: " + getOutputDirectory());
    }

    // ----------------------------------------------------------------------
    // Create the compiler configuration
    // ----------------------------------------------------------------------

    JoocConfiguration compilerConfiguration = new JoocConfiguration();

    compilerConfiguration.setOutputDirectory(getOutputDirectory());
    compilerConfiguration.setDebug(debug);
    compilerConfiguration.setVerbose(verbose);

    if (debug && StringUtils.isNotEmpty(debuglevel)) {
      if (!(debuglevel.equalsIgnoreCase("none") || debuglevel.equalsIgnoreCase("lines")
        || debuglevel.equalsIgnoreCase("source"))) {
        throw new IllegalArgumentException("The specified debug level: '" + debuglevel
          + "' is unsupported. " + "Legal values are 'none', 'lines', and 'source'.");
      }
      compilerConfiguration.setDebugLevel(debuglevel);
    }


    Set staleSources =  computeStaleSources(getSourceInclusionScanner(staleMillis));
    compilerConfiguration.setSourceFiles(staleSources);

    if (staleSources.isEmpty()) {
      getLog().info("Nothing to compile - all classes are up to date");
      return;
    }

    // create output directory if it does not exist
    if (!getOutputDirectory().exists())
      if (!getOutputDirectory().mkdirs())
        throw new MojoExecutionException("Failed to create output directory " + getOutputDirectory().getAbsolutePath());

    int result = compile(compilerConfiguration);
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

  private int compile(JoocConfiguration compilerConfiguration) {
    final Set<File> sources = compilerConfiguration.getSourceFiles();
    log.info("Compiling " + sources.size() +
            " joo source file"
            + (sources.size() == 1 ? "" : "s")
            + (getOutputDirectory() != null ? " to " + getOutputDirectory() : ""));

    Jooc jooc = new Jooc();
    final String[] commandLine = compilerConfiguration.getCommandLine();
    if (verbose) {
      StringBuffer cmdLine = new StringBuffer(100);
      cmdLine.append("jooc ");
      for (String joocArg : commandLine) {
        cmdLine.append(" ");
        cmdLine.append(joocArg);
      }
      log.debug(cmdLine.toString());
    }
    return jooc.run(commandLine);
  }

  private Set computeStaleSources(SourceInclusionScanner scanner)  throws MojoExecutionException {
    SourceMapping mapping;

    File outputDirectory;

    mapping = new SuffixMapping(Jooc.JS2_SUFFIX, Jooc.JS_SUFFIX);

    outputDirectory = getOutputDirectory();

    scanner.addSourceMapping(mapping);

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

  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    SourceInclusionScanner scanner = null;

    if (includes.isEmpty() && excludes.isEmpty()) {
      scanner = new StaleSourceScanner(staleMillis);
    } else {
      if (includes.isEmpty()) {
        includes.add("**/*.js2");
      }
      scanner = new StaleSourceScanner(staleMillis, includes, excludes);
    }

    return scanner;
  }

  protected SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding) {
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
