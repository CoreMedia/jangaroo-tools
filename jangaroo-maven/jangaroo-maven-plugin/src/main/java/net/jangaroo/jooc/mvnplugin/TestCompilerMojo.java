package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mojo to compile Jangaroo sources from during the test-compile phase.
 *
 * @goal testCompile
 * @phase test-compile
 */
public class TestCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.testOutputDirectory}/classes"
   */
  private File testOutputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File sourceDirectory;
  /**
   * A list of test inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set testIncludes = new HashSet();
  /**
   * A list of test exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set testExcludes = new HashSet();
  /**
   * Absolute output filename of the merged javascript.  
   *
   * @parameter expression="${project.build.testOutputDirectory}/${project.artifactId}-test.js"
   */
  private String testOutputFileName;

  protected List<File> getCompileSourceRoots() {
    return Collections.singletonList(sourceDirectory);
  }

  protected File getOutputDirectory() {
    return testOutputDirectory;
  }


  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(testIncludes, testExcludes, staleMillis);
  }


  public String getOutputFileName() {
    return testOutputFileName;
  }
}