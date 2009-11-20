package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: sannies
 * Date: 15.09.2009
 * Time: 20:04:43
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJooTestMojo extends AbstractMojo {
  /**
   * Classname of the Actionscript TestSuite that will start all tests.
   *
   * @parameter default-value="suite.TestSuite"
   */
  protected String testSuiteName;

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;

  /**
   * Output directory for the janagroo artifact  unarchiver. All jangaroo dependencies will be unpacked into
   * this directory.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   * @required
   */
  protected File testOutputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  protected File testSourceDirectory;


  protected boolean isTestAvailable() {
    File testSuite = new File(testSourceDirectory, testSuiteName.replace(".", File.separator) + ".as");
    if (!testSuite.exists()) {
      getLog().info("The testSuite '" + testSuite + "' could not be found. Skipping.");
    }
    return testSuite.exists();
  }

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  protected boolean skip;


  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  protected boolean skipTests;


}
