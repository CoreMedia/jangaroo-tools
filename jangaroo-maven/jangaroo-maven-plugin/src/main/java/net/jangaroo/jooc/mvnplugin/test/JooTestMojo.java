package net.jangaroo.jooc.mvnplugin.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.eclipse.jetty.server.Server;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
 * @threadSafe
 */
public class JooTestMojo extends JooTestMojoBase {
  /**
   * The Maven session.
   *
   * @parameter expression="${session}"
   * @required
   * @readonly
   */
  protected MavenSession session;


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
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipJooUnitTests}"
   */
  private boolean skipJooUnitTests;

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
   * Specifies the number of retries when receiving unexpected result from phantomjs (crash?).
   * Default is 5.
   *
   * @parameter
   */
  @SuppressWarnings("FieldCanBeLocal")
  private int jooUnitMaxRetriesOnCrashes = 5;

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
   * If not phantomjs executable (or an outdated one) is found, falls back to Selenium.
   *
   * @parameter expression="${phantomjs.bin}" default-value="phantomjs"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private String phantomBin;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skip && !skipTests && !skipJooUnitTests && isTestAvailable()) {
      Server server = jettyRunTest(true);
      String url = getTestUrl(server);

      try {
        File testResultOutputFile = new File(testResultOutputDirectory, getTestResultFileName());
        File phantomTestRunner = new File(testResultOutputDirectory, "phantomjs-joounit-page-runner.js");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/net/jangaroo/jooc/mvnplugin/phantomjs-joounit-page-runner.js"), phantomTestRunner);
        final PhantomJsTestRunner phantomJsTestRunner = new PhantomJsTestRunner(phantomBin, url, testResultOutputFile.getPath(), phantomTestRunner.getPath(), jooUnitTestExecutionTimeout, jooUnitMaxRetriesOnCrashes, getLog());
        if (phantomJsTestRunner.canRun()) {
          executePhantomJs(testResultOutputFile, phantomJsTestRunner);
        } else {
          executeSelenium(url);
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot create local copy of phantomjs-joounit-page-runner.js", e);
      } finally {
        try {
          server.stop();
        } catch (Exception e) {
          // never mind we just couldn't step the selenium server.
          getLog().error("Could not stop test Jetty.", e);
        }
      }
    }
  }

  private void executePhantomJs(File testResultOutputFile, PhantomJsTestRunner phantomJsTestRunner) throws MojoFailureException, MojoExecutionException {
    getLog().debug("synchronizing on " + session.getContainer() + " (" + Integer.toHexString(System.identityHashCode(session.getContainer())) + ")");
    synchronized (session.getContainer()) {
      getLog().info("running phantomjs: " + phantomJsTestRunner.toString());
      try {
        final boolean exitCode = phantomJsTestRunner.execute();
        if (exitCode) {
          evalTestOutput(new FileReader(testResultOutputFile));
        } else {
          signalError();
        }
      } catch (CommandLineException e) {
        throw wrap(e);
      } catch (IOException e) {
        throw wrap(e);
      } catch (ParserConfigurationException e) {
        throw wrap(e);
      } catch (SAXException e) {
        throw wrap(e);
      }
    }
  }

  void executeSelenium(String testsHtmlUrl) throws MojoExecutionException, MojoFailureException {
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
    Selenium selenium = new DefaultSelenium(jooUnitSeleniumRCHost, jooUnitSeleniumRCPort, jooUnitSeleniumBrowserStartCommand, testsHtmlUrl);
    try {
      selenium.start();
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
      evalTestOutput(new StringReader(testResultXml));
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot write test results to file", e);
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("Cannot create a simple XML Builder", e);
    } catch (SAXException e) {
      throw new MojoExecutionException("Cannot parse test result", e);
    } catch (SeleniumException e) {
      throw new MojoExecutionException("Selenium setup exception", e);
    } finally {
      selenium.stop();
    }
  }

  File writeResultToFile(java.lang.String testResultXml) throws IOException {
    File result = new File(testResultOutputDirectory, getTestResultFileName());
    FileUtils.writeStringToFile(result, testResultXml);
    if (!result.setLastModified(System.currentTimeMillis())) {
      getLog().warn("could not set modification time of file " + result);
    }
    return result;
  }

  private String getTestResultFileName() {
    return testResultFileName != null ? testResultFileName : "TEST-" + project.getArtifactId() + ".xml";
  }

  void evalTestOutput(Reader inStream) throws ParserConfigurationException, IOException, SAXException, MojoFailureException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
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
      signalFailure();
    }
  }

  private void signalError() throws MojoExecutionException {
    throw new MojoExecutionException("There are errors");
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
