package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compiles AS as part of the WAR lifecycle. Not used in jangaroo lifecycle.
 *
 * @goal war-compile
 * @phase compile
 */
public class WarCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${scripts}" default-value="${project.build.directory}/${project.build.finalName}/scripts/classes"
   */
  private File outputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="src/main/joo"
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
   * @parameter default-value="${project.build.directory}/${project.build.finalName}/scripts/${project.artifactId}.js"
   */
  private String outputFileName;

  protected List getCompileSourceRoots() {
    return Collections.singletonList(sourceDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }


  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(includes, excludes, staleMillis);
  }

  protected SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding) {
    return getSourceInclusionScanner(includes, excludes, inputFileEnding);
  }

  public String getOutputFileName() {
    return outputFileName;
  }

}
