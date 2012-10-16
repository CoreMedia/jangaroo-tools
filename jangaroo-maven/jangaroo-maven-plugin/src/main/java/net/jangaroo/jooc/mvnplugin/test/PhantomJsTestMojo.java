package net.jangaroo.jooc.mvnplugin.test;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  private boolean skipTests;

  /**
   * Output directory for test results.
   *
   * @parameter expression="${project.build.directory}/surefire-reports/"  default-value="${project.build.directory}/surefire-reports/"
   * @required
   */
  private File testResultOutputDirectory;


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

    if( skipTests ) {
      getLog().info("Skipping tests");
      return;
    }

    Properties placeholders = createPlaceholders();

    // if there already is a a file src/test/resources/test.html provided by the user then this file is used
    boolean overwrite = false;

    writeTemplate(getClass().getResourceAsStream(INVOKER_JS), new File(testOutputDirectory, INVOKER_JS), placeholders, overwrite);
    writeTemplate(getClass().getResourceAsStream(INVOKER_HTML), new File(testOutputDirectory, INVOKER_HTML), placeholders, overwrite);

    PhantomJsTestRunner runner = new PhantomJsTestRunner(executable, testOutputDirectory, new File(testOutputDirectory, INVOKER_JS).getAbsolutePath(), getTestClassName(), "", timeout, getLog());
    if (runner.canRun()) {

      runner.execute();
      String testResultXml = runner.getTestResult();
      writeResultToFile(getProject().getArtifactId(), testResultXml);

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

  private void writeResultToFile(String testSuite, String testResultXml) throws IOException {
    File result = new File(testResultOutputDirectory, "TEST-"+testSuite+".xml");
    FileUtils.writeStringToFile(result, testResultXml);
  }

  /**
   * Parses the test result from xml into an object
   */
  private TestResult parseTestResult(String testResultXml) throws ParserConfigurationException, IOException, SAXException {

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
    StringReader inStream = new StringReader(testResultXml);
    InputSource inSource = new InputSource(inStream);
    Document d = dBuilder.parse(inSource);
    NodeList nl = d.getChildNodes();
    NamedNodeMap namedNodeMap = nl.item(0).getAttributes();


    TestResult result = new TestResult();
    result.setFailures(Integer.parseInt(namedNodeMap.getNamedItem("failures").getNodeValue()));
    result.setErrors(Integer.parseInt(namedNodeMap.getNamedItem("errors").getNodeValue()));
    result.setTests(Integer.parseInt(namedNodeMap.getNamedItem("tests").getNodeValue()));
    result.setTime(Integer.parseInt(namedNodeMap.getNamedItem("time").getNodeValue()));
    result.setName(namedNodeMap.getNamedItem("name").getNodeValue());

    return result;

  }

  // ====================

  private static class TestResult {

    private String name;
    private int failures;
    private int errors;
    private int tests;
    private int time;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getFailures() {
      return failures;
    }

    public void setFailures(int failures) {
      this.failures = failures;
    }

    public int getErrors() {
      return errors;
    }

    public void setErrors(int errors) {
      this.errors = errors;
    }

    public int getTests() {
      return tests;
    }

    public void setTests(int tests) {
      this.tests = tests;
    }

    public int getTime() {
      return time;
    }

    public void setTime(int time) {
      this.time = time;
    }
  }

}
