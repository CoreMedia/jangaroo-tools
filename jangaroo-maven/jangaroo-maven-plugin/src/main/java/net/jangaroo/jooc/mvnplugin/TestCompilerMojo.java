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
   * Test output directory into whose joo/classes sub-directory compiled classes are generated.
   *
   * @parameter expression="${project.build.testOutputDirectory}"
   */
  private File testOutputDirectory;

  /**
   * Location of Jangaroo test resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-test-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-test-output/"
   */
  private File testPackageSourceDirectory;

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
   * @parameter expression="${project.build.directory}/temp/jangaroo-test-output/classes"
   */
  private File tempTestClassesOutputDirectory;

  /**
   * This parameter specifies the path and name of the output file containing all
   * compiled classes, relative to the testOutputDirectory.
   *
   * @parameter expression="joo/${project.groupId}.${project.artifactId}-test.classes.js"
   */
  private String moduleTestClassesJsFile;

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
    return isJangarooPackaging() ? testOutputDirectory : testPackageSourceDirectory;
  }


  protected File getTempClassesOutputDirectory() {
    return tempTestClassesOutputDirectory;
  }

  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(testIncludes, testExcludes, staleMillis);
  }


  public String getModuleClassesJsFileName() {
    return moduleTestClassesJsFile;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(super.getActionScriptClassPath());
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }
}