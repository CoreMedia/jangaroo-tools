package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.PackageApplicationMojo;
import org.apache.maven.plugin.MojoExecutionException;
import net.jangaroo.jooc.mvnplugin.Types;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Prepares the Javascript Testenvironment including generation of the HTML page and decompression of jangaroo
 * dependencies. This plugin is executed in the <code>generate-test-resources</code> phase of the jangaroo lifecycle.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Mojo(name = "unpack-jangaroo-test-dependencies",
        defaultPhase = LifecyclePhase.TEST_COMPILE,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class JooGenerateTestResourcesMojo extends PackageApplicationMojo {

  /**
   * Output directory into whose joo/classes sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getPackageSourceDirectory}.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  /**
   * Location of Jangaroo test resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getPackageSourceDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-test-output/
   */
  @Parameter (defaultValue = "${project.build.directory}/jangaroo-test-output/")
  private File testPackageSourceDirectory;

  /**
   * Output directory to assemble the test Web-app.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  private File testOutputDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you enable it using the
   * "maven.test.skip" property, because maven.test.skip disables both running the tests and compiling the tests.
   * Consider using the skipTests parameter instead.
   */
  @Parameter(defaultValue = "${maven.test.skip}")
  private boolean skip;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   */
  @Parameter(defaultValue = "${skipTests}")
  private boolean skipTests;

  public File getPackageSourceDirectory() {
    return Types.JANGAROO_TYPE.equals(project.getPackaging()) ? outputDirectory : testPackageSourceDirectory;
  }

  protected boolean isTestAvailable() {
    return true; // TODO
  }

  public void execute() throws MojoExecutionException {
    if (!skip && !skipTests) {
      if (isTestAvailable()) {
        getLog().info("Creating test jangaroo-application.js below " + testOutputDirectory);
        createWebapp(testOutputDirectory);
      }
    } else {
      getLog().info("Skipping generation of test jangaroo-application.js");
    }
  }

  @Override
  protected void writeThisJangarooModuleScript(File scriptDirectory, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    super.writeThisJangarooModuleScript(new File(outputDirectory, "META-INF/resources/joo"), jangarooApplicationWriter, jangarooApplicationAllWriter);
    writeModule(scriptDirectory, project.getGroupId(), project.getArtifactId() + "-test", project.getVersion(),
            jangarooApplicationWriter, jangarooApplicationAllWriter);
  }
}
