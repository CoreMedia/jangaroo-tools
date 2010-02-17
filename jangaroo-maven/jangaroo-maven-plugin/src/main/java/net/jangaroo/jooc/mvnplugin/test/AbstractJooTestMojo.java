package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Resource;

import java.io.File;
import java.util.List;

/**
 * 
 */
public abstract class AbstractJooTestMojo extends AbstractMojo {

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
   */
  protected File testOutputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  protected File testSourceDirectory;

  /**
   * the tests.html file relative to the test resources folder
   *
   * @parameter expression="tests.html"
   */
  protected String testsHtml;

   /**
   * the tests.html file relative to the test resources folder
   *
   * @parameter expression="${project.testResources}"
   */
  protected List<Resource> testResources;


  protected boolean isTestAvailable() {
   for(Resource r : testResources) {
      File testFile = new File(r.getDirectory(), testsHtml);
      if(testFile.exists()) {
        return true;
      }
    }
    getLog().info("The tests.html file '" + testsHtml + "' could not be found. Skipping.");
    return false;
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
