package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.PackageApplicationMojo;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Prepares the Javascript Testenvironment including generation of the HTML page and decompression of jangaroo
 * dependencies. This plugin is executed in the <code>generate-test-resources</code> phase of the jangaroo lifecycle.
 *
 * @requiresDependencyResolution test
 * @goal unpack-jangaroo-test-dependencies
 * @phase generate-test-resources
 */
@SuppressWarnings({"UnusedDeclaration"})
public class JooGenerateTestResourcesMojo extends PackageApplicationMojo {

  /**
   * Output directory for the janagroo artifact  unarchiver. All jangaroo dependencies will be unpacked into this
   * directory.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   */
  protected File testOutputDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you enable it using the
   * "maven.test.skip" property, because maven.test.skip disables both running the tests and compiling the tests.
   * Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  protected boolean skip;

  /**
   * the tests.html file relative to the test resources folder
   *
   * @parameter expression="${project.testResources}"
   */
  protected List<Resource> testResources;

  protected boolean isTestAvailable() {
    return true; // TODO
  }

  public void execute() throws MojoExecutionException {
    if (!skip) {
      try {
        if (isTestAvailable()) {
          getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
          createWebapp(testOutputDirectory);
          for (Resource r : testResources) {
            File testResourceDirectory = new File(r.getDirectory());
            if (testResourceDirectory.exists()) {
              FileUtils.copyDirectoryStructureIfModified(testResourceDirectory, testOutputDirectory);
            }
          }
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
      }
    } else {
      getLog().info("Skipping generation of test resources");
    }
  }

}
