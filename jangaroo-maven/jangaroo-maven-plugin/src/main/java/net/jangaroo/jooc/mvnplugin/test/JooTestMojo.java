package net.jangaroo.jooc.mvnplugin.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.mortbay.jetty.plugin.JettyWebAppContext;
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Executes JooUnit tests.
 * Unpacks all dependency to its output directory, generates a tests.html which starts up the class
 * <code>testSuiteName</code>. Since a real browser is the best JavaScript execution environment
 * the test now fires up a jetty on a random port between <code>jooUnitJettyPortLowerBound</code> and
 * <code>jooUnitJettyPortUpperBound</code> contacts a selenium server given by
 * <code>jooUnitSeleniumRCHost</code>. The Selenium Remote Control then starts a browser, navigates
 * the browser to the Jetty we just started and waits for <code>jooUnitTestExecutionTimeout</code>ms
 * for the results to appear on the browser screen.
 *
 * @goal test
 * @phase test
 * @requiresDependencyResolution test
 */
public class JooTestMojo extends JooTestMojoBase {

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private File testSourceDirectory;
  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  private boolean skip;
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
  @SuppressWarnings({"UnusedDeclaration"})
  private File testResultOutputDirectory;

  /**
   * @parameter
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private String testResultFileName;

  /**
   * Specifies the time in milliseconds to wait for the test results in the browser. Default is 30000ms.
   *
   * @parameter
   */
  @SuppressWarnings("FieldCanBeLocal")
  private int jooUnitTestExecutionTimeout = 30000;


  /**
   * Defines the Selenium RC host. Default is localhost.
   * If the system property SELENIUM_RC_HOST is set, it is used prior to the
   * maven parameter.
   *
   * @parameter
   */
  private String jooUnitSeleniumRCHost = "localhost";

  /**
   * Defines the Selenium RC port. Default is 4444.
   *
   * @parameter
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private int jooUnitSeleniumRCPort = 4444;

  /**
   * Selenium browser start command. Default is *firefox
   *
   * @parameter
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String jooUnitSeleniumBrowserStartCommand = "*firefox";

  /**
   * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on
   * occasion.
   *
   * @parameter expression="${maven.test.failure.ignore}"
   */
  private boolean testFailureIgnore;

  /**
   * The phantomjs executable. If not specified, it expects the phantomjs binary in the PATH.
   *
   * @parameter expression="${phantomjs.bin}" default-value="phantomjs"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String phantomBin;

  /**
   * The script to run in phantomjs launching the tests
   *
   * @parameter expression="${phantomjs.runner}" default-value="joo/phantomjs-joounit-runner.js"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String phantomTestRunner;

  /**
   * The test suite to run be run with phantomjs
   *
   * @parameter expression="${phantomjs.suite}"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String phantomTestSuite;

  /**
   * Additional arguments to be passed to phantomjs. Expected to be a JSON object, i.e.
   * <code>
   * {timeout:30000,loglevel:'all'}
   * </code>
   *
   * @parameter expression="${phantomjs.args}"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String phantomArgs;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skip && !skipTests) {
//      final PhantomJsTestRunner phantomJsTestRunner = new PhantomJsTestRunner(phantomBin, testOutputDirectory, phantomTestRunner, phantomTestSuite, phantomArgs, jooUnitTestExecutionTimeout, getLog());
//      getLog().info("trying to run phantomjs first: " + phantomJsTestRunner.toString());
//      if (phantomTestSuite != null && phantomJsTestRunner.isTestAvailable() && phantomJsTestRunner.canRun()) {
//        try {
//          final boolean exitCode = phantomJsTestRunner.execute();
//          String testResultXml = phantomJsTestRunner.getTestResult();
//          writeResultToFile(testResultXml);
//          evalTestOutput(testResultXml);
//          if (!exitCode) {
//            signalFailure();
//          }
//        } catch (CommandLineException e) {
//          throw wrap(e);
//        } catch (IOException e) {
//          throw wrap(e);
//        } catch (ParserConfigurationException e) {
//          throw wrap(e);
//        } catch (SAXException e) {
//          throw wrap(e);
//        }
//      } else
      if (isTestAvailable()) {
        executeSelenium();
      }
    }
  }

  void executeSelenium() throws MojoExecutionException, MojoFailureException {
    jooUnitSeleniumRCHost = System.getProperty("SELENIUM_RC_HOST", jooUnitSeleniumRCHost);
    try {
      //check wether the host is reachable
      //noinspection ResultOfMethodCallIgnored
      InetAddress.getAllByName(jooUnitSeleniumRCHost);
    } catch (UnknownHostException e) {
      throw new MojoExecutionException("Cannot resolve host " + jooUnitSeleniumRCHost +
              ". Please specify a host running the selenium remote control or skip tests" +
              " by -DskipTests", e);
    }
    getLog().info("JooTest report directory: " + testResultOutputDirectory.getAbsolutePath());
    Server server = jettyRunTest(true);
    String url = getJettyUrl(server);
    Selenium selenium = new DefaultSelenium(jooUnitSeleniumRCHost, jooUnitSeleniumRCPort, jooUnitSeleniumBrowserStartCommand, url);
    try {
      selenium.start();
      final String testsHtmlUrl = url + "/" + testsHtml.replace(File.separatorChar, '/');
      getLog().debug("Opening " + testsHtmlUrl);
      selenium.open(testsHtmlUrl);
      getLog().debug("Waiting for test results for " + jooUnitTestExecutionTimeout + "ms ...");
      selenium.waitForCondition("selenium.browserbot.getCurrentWindow().result != null || selenium.browserbot.getCurrentWindow().classLoadingError != null", "" + jooUnitTestExecutionTimeout);
      String classLoadingError = selenium.getEval("selenium.browserbot.getCurrentWindow().classLoadingError");
      if (classLoadingError != null && !classLoadingError.equals("null")) {
        throw new MojoExecutionException(classLoadingError);
      }

      String testResultXml = selenium.getEval("selenium.browserbot.getCurrentWindow().result");
      writeResultToFile(testResultXml);
      evalTestOutput(testResultXml);
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot write test results to file", e);
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("Cannot create a simple XML Builder", e);
    } catch (SAXException e) {
      throw new MojoExecutionException("Cannot parse test result", e);
    } catch (SeleniumException e) {
      if (!testFailureIgnore) {
        throw new MojoExecutionException("Selenium setup failure", e);
      } else {
        getLog().warn("Selenium setup failure", e);
      }
    } finally {
      selenium.stop();
      try {
        server.stop();
      } catch (Exception e) {
        getLog().error(e);
        // never mind we just couldn't step the selenium server.
      }
    }
  }

  void writeResultToFile(java.lang.String testResultXml) throws IOException {
    File result = new File(testResultOutputDirectory, getTestResultFileName());
    FileUtils.writeStringToFile(result, testResultXml);
    if (!result.setLastModified(System.currentTimeMillis())) {
      getLog().warn("could not set modification time of file " + result);
    }
  }

  private String getTestResultFileName() {
    if (this.testResultFileName != null) {
      return this.testResultFileName;
    } else {
      final String result = this.phantomTestSuite != null ? this.phantomTestSuite : "TEST-" + project.getArtifactId();
      return result + ".xml";
    }
  }

  void evalTestOutput(String testResultXml) throws ParserConfigurationException, IOException, SAXException, MojoFailureException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
    StringReader inStream = new StringReader(testResultXml);
    InputSource inSource = new InputSource(inStream);
    Document d = dBuilder.parse(inSource);
    NodeList nl = d.getChildNodes();
    NamedNodeMap namedNodeMap = nl.item(0).getAttributes();
    final String failures = namedNodeMap.getNamedItem("failures").getNodeValue();
    final String errors = namedNodeMap.getNamedItem("errors").getNodeValue();
    final String tests = namedNodeMap.getNamedItem("tests").getNodeValue();
    final String time = namedNodeMap.getNamedItem("time").getNodeValue();
    final String name = namedNodeMap.getNamedItem("name").getNodeValue();
    getLog().info(name + " tests run: " + tests + ", Failures: " + failures + ", Errors: " + errors + ", time: " + time + " ms");
    if (Integer.parseInt(errors) > 0 || Integer.parseInt(failures) > 0) {
      getLog().info(testResultXml);
      signalFailure();
    }
  }

  private void signalFailure() throws MojoFailureException {
    if (!testFailureIgnore) {
      throw new MojoFailureException("There are test failures");
    }
  }

  public void setSkip(boolean b) {
    this.skip = b;
  }

  public void setSkipTests(boolean b) {
    this.skipTests = b;
  }

  public void setTestSourceDirectory(File f) {
    this.testSourceDirectory = f;
  }

  public void setTestResources(ArrayList<org.apache.maven.model.Resource> resources) {
    this.testResources = resources;
  }

  public void setTestFailureIgnore(boolean b) {
    this.testFailureIgnore = b;
  }

  public void setTestOutputDirectory(File testOutputDirectory) {
    this.testOutputDirectory = testOutputDirectory;
  }
}
