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
   * @parameter expression="${project.build.outputDirectory}/classes"
   */
  private File outputDirectory;

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
  private Set includes = new HashSet();
  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set excludes = new HashSet();

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/${project.artifactId}.js"
   */
  private String outputFileName;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
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


  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(includes, excludes, staleMillis);
  }

  public String getOutputFileName() {
    return outputFileName;
  }
}
