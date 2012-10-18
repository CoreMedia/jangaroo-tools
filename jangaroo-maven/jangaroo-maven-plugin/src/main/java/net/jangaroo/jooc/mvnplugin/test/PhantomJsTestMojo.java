package net.jangaroo.jooc.mvnplugin.test;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import java.io.File;
import java.util.Properties;

/**
 * Executes unit tests using PhantomJS
 *
 * @requiresDependencyResolution test
 * @goal run-test-phantomjs
 * @phase test
 */
public class PhantomJsTestMojo extends TestMojoBase {

  private static final String INVOKER_JS    = "phantomjs-page-invoker.js";
  private static final String INVOKER_HTML  = "phantomjs-page.html";

  /**
   * Time in milliseconds to wait for the test to finish. Default is 30000ms.
   *
   * @parameter
   */
  private int timeout = 30000;

  /**
   * Location of the PhantomJS executable
   * @parameter expression="${phantomjs.executable}" default-value="phantomjs"
   * @required
   */
  private String executable;

  /**
   * Additional javascript to be invoked prior to invoking the tests. Can be used for setting up additional
   * infrastructure. Example
   * <code>
   * console.log('Setting up tests');
   * </code>
   *
   * @parameter default-value=""
   */
  private String setUpScript;

  /**
   * If set to true, then additional messages will be logged. For debugging purposes.
   * @parameter default-value="false"
   */
  private boolean verbose;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    try {

      unpackResources();
      runTests();
    }
    catch (MojoFailureException e) {
      throw e;
    }
    catch (MojoExecutionException e) {
      throw e;
    }
    catch (Exception e) {
      throw new MojoExecutionException(e.toString(), e);
    }
  }

  // ===========================================

  @Override
  protected Properties createPlaceholders() throws Exception {
    Properties properties = super.createPlaceholders();
    properties.setProperty("_phantom_timeout", Integer.toString(timeout));
    properties.setProperty("_phantom_setup", setUpScript != null ? setUpScript : "");
    properties.setProperty("_phantom_verbose", Boolean.toString(verbose));
    return properties;
  }


  // ===========================================

  private void runTests() throws Exception {

    if( isSkipTests() ) {
      getLog().info("Skipping tests");
      return;
    }

    Properties placeholders = createPlaceholders();

    // if there already is a a file src/test/resources/test.html provided by the user then this file is used
    boolean overwrite = false;

    writeTemplate(getClass().getResourceAsStream(INVOKER_JS), new File(getTestOutputDirectory(), INVOKER_JS), placeholders, overwrite);
    writeTemplate(getClass().getResourceAsStream(INVOKER_HTML), new File(getTestOutputDirectory(), INVOKER_HTML), placeholders, overwrite);

    PhantomJsTestRunner runner = new PhantomJsTestRunner(
                                              executable,
                                              getTestOutputDirectory(),
                                              new File(getTestOutputDirectory(), INVOKER_JS).getAbsolutePath(),
                                              getConfiguredTestClassName(),
                                              "",
                                              timeout,
                                              getLog());
    if (runner.canRun()) {

      runner.execute();
      String testResultXml = runner.getTestResult();
      writeTestResult(getProject().getArtifactId(), testResultXml);

      TestResult testResult = parseTestResult(testResultXml);
      if( testResult.getTests() == 0 ) {
        throw new MojoFailureException("No tests found");
      }
      else if( testResult.getFailures() + testResult.getErrors() > 0 ) {
        throw new MojoFailureException("Error running tests: "+testResult.getFailures()+" failures and "+testResult.getErrors()+" of "+testResult.getTests()+" tests");
      }
      else {
        getLog().info("Successfully executed "+testResult.getTests()+" tests in "+testResult.getTime()+" ms");
      }
     }
    else {
      getLog().error("Couldn't execute PhantomJS");
      throw new MojoFailureException("Couldn't execute ");
    }
  }


}
