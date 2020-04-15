package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.AbstractSenchaMojo;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isSenchaDependency;

/**
 * Executes JooUnit tests.
 * Unpacks all dependency to its output directory, generates a tests app which runs the script
 * 'run-tests.js'. This runs JooUnit's BrowserRunner with the given TestSuite.
 * Then the Test Mojo starts a Jetty on a random port between <code>jooUnitJettyPortLowerBound</code> and
 * <code>jooUnitJettyPortUpperBound</code> and prints out the resulting test app URL.
 * <p>Tests are executed in one of three ways:</p>
 * <ol>
 * <li>
 * If available, PhantomJS is started with a script that loads the test app and stores the
 * test result in the report file in XML format.
 * </li>
 * <li>
 * Otherwise, the Mojo tries to start a browser via the Selenium server given by
 * <code>jooUnitSeleniumRCHost</code> with the test app URL and tries to collect the test result
 * through Selenium.
 * </li>
 * <li>
 * Another option is to set the system property <code>interactiveJooUnitTests</code>
 * (<code>mvn test -DinteractiveJooUnitTests</code>), which simply halts Maven execution
 * and gives you the opportunity to load and debug the test app in a browser.
 * </li>
 * </ol>
 * Option 1 and 2 respect the configured <code>jooUnitTestExecutionTimeout</code> (ms)
 * and interrupt the tests after that time, resulting in a test failure.
 */
@Mojo(name = "generateTestApp",
        defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class JooGenerateTestAppMojo extends AbstractSenchaMojo {

  private static final String DEFAULT_TEST_APP_JSON = "default.test.app.json";

  /**
   * Directory whose joo/classes sub-directory contains compiled test classes.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  protected File testOutputDirectory;

  @Parameter
  private String toolkit = SenchaUtils.TOOLKIT_CLASSIC;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   */
  @Parameter(property = "maven.test.skip")
  private boolean skip;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   */
  @Parameter(property = "skipTests")
  private boolean skipTests;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   */
  @Parameter(property = "skipJooUnitTests")
  private boolean skipJooUnitTests;

  /**
   * Defines the class of the test suite for JooUnit tests.
   */
  @Parameter
  private String testSuite = null;

  public void execute() throws MojoExecutionException {
    boolean doSkip = skip || skipTests || skipJooUnitTests;
    if (doSkip || testSuite == null) {
      getLog().info("Skipping generation of Jangaroo test app: " + (doSkip ? "tests skipped." : "no tests found."));
    } else {
      getLog().info("Creating Jangaroo test app below " + testOutputDirectory);
      createWebApp(testOutputDirectory);

      // sencha -cw target\test-classes config -prop skip.sass=1 -prop skip.resources=1 then app refresh
      new SenchaCmdExecutor(testOutputDirectory, "config -prop skip.sass=1 -prop skip.resources=1 then app refresh", getSenchaJvmArgs(), getLog(), getSenchaLogLevel()).execute();
    }
  }

  /**
   * Create the Jangaroo Web app in the given Web app directory.
   *
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws MojoExecutionException if anything goes wrong
   */
  private void createWebApp(File webappDirectory) throws MojoExecutionException {
    // only generate app if senchaCfg does not exist
    if (SenchaUtils.doesSenchaAppExist(webappDirectory)) {
      getLog().info("Sencha app already exists, skip generating one");
      return;
    }
    getLog().info(String.format("Generating Sencha App %s for unit tests...", webappDirectory));
    FileHelper.ensureDirectory(webappDirectory);
    SenchaUtils.generateSenchaTestAppFromTemplate(webappDirectory, project, getSenchaPackageName(project), testSuite, toolkit, getLog(), getSenchaLogLevel(), getSenchaJvmArgs());
    createAppJson();
  }

  private boolean isTestDependency(Dependency dependency) {
    return Artifact.SCOPE_TEST.equals(dependency.getScope()) && isSenchaDependency(dependency)
            && !isExtFrameworkDependency(dependency);
  }

  private void createAppJson() throws MojoExecutionException {
    File appJsonFile = new File(project.getBuild().getTestOutputDirectory(), SenchaUtils.SENCHA_APP_FILENAME);
    getLog().info(String.format("Generating Sencha App %s for unit tests...", appJsonFile.getPath()));

    SenchaAppConfigBuilder configBuilder = new SenchaAppConfigBuilder();
    try {
      configBuilder.destFile(appJsonFile);
      configBuilder.defaults(DEFAULT_TEST_APP_JSON);
      configBuilder.destFileComment("Auto-generated test application configuration. DO NOT EDIT!");

      // require the package to test:
      configBuilder.require(getSenchaPackageName(project));
      // add test scope dependencies:
      List<Dependency> projectDependencies = project.getDependencies();
      for (Dependency dependency : projectDependencies) {
        if (isTestDependency(dependency)) {
          configBuilder.require(getSenchaPackageName(dependency.getGroupId(), dependency.getArtifactId()));
        }
      }
      configBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not build test " + SenchaUtils.SENCHA_APP_FILENAME, e);
    }
  }

  void skip() {
    this.skip = true;
  }

  void skipTests() {
    this.skipTests = true;
  }

}
