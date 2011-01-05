package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources from during the test-compile phase.
 *
 * @goal testCompile
 * @phase test-compile
 * @requiresDependencyResolution test
 *
 */
@SuppressWarnings({"UnusedDeclaration"})
public class TestCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.testOutputDirectory}/scripts/classes"
   */
  private File testOutputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.sourceDirectory}"
   */
  private File sourceDirectory;

  /**
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;

  /**
   * A list of test inclusion filters for the compiler.
   *
   * @parameter
   */

  private Set<String> testIncludes = new HashSet<String>();
  /**
   * A list of test exclusion filters for the compiler.
   *
   * @parameter
   */

  private Set<String> testExcludes = new HashSet<String>();

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   *
   * @parameter expression="${project.build.directory}/temp/joo-test/classes"
   */
  private File testTempOutputDirectory;

  /**
   * Absolute output filename of the merged javascript.  
   *
   * @parameter expression="${project.build.testOutputDirectory}/scripts/${project.groupId}.${project.artifactId}-test.classes.js"
   */
  private String testOutputFileName;

  /**
   * 
   * @return null as API stub generation does not make sense for test sources
   */
  @Override
  protected File getApiOutputDirectory() {
    return null;
  }

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(sourceDirectory, testSourceDirectory);
  }

  protected File getOutputDirectory() {
    return testOutputDirectory;
  }

  protected File getTempOutputDirectory() {
    return testTempOutputDirectory;
  }

  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(testIncludes, testExcludes, staleMillis);
  }


  public String getOutputFileName() {
    return testOutputFileName;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(super.getActionScriptClassPath());
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }
}