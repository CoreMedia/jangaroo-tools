package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.PackageApplicationMojo;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import net.jangaroo.jooc.mvnplugin.Types;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
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
   * Output directory into whose joo/classes sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getPackageSourceDirectory}.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Location of Jangaroo test resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getPackageSourceDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-test-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-test-output/"
   */
  private File testPackageSourceDirectory;

  /**
   * Output directory for the jangaroo artifact unarchiver. All jangaroo dependencies will be unpacked into this
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

  public File getPackageSourceDirectory() {
    return Types.JANGAROO_TYPE.equals(project.getPackaging()) ? outputDirectory : testPackageSourceDirectory;
  }

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

  @Override
  protected void writeThisJangarooModuleScript(File scriptDirectory, Writer fw) throws IOException {
    super.writeThisJangarooModuleScript(scriptDirectory, fw);
    fw.write("joo.loadModule(\"" + project.getGroupId() + "\",\"" + project.getArtifactId() + "-test\");\n");
  }
}
