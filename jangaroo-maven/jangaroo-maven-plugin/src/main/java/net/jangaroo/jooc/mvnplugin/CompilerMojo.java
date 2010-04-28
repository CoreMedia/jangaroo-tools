package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 *
 * @goal compile
 * @phase compile
 */
public class CompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/scripts/classes"
   */
  private File outputDirectory;

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   *
   * @parameter expression="${project.build.directory}/temp/joo/classes"
   */
  private File tempOutputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.sourceDirectory}"
   */
  private File sourceDirectory;
  /**
   * A list of inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> includes = new HashSet();
  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> excludes = new HashSet();

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/scripts/${project.artifactId}.js"
   */
  private String outputFileName;

  /**
   * Output directory for all generated ActionScript3 files to compile.
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;    

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(sourceDirectory,generatedSourcesDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }

  protected File getTempOutputDirectory() {
    return tempOutputDirectory;
  }

  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(includes, excludes, staleMillis);
  }

  public String getOutputFileName() {
    return outputFileName;
  }
}
