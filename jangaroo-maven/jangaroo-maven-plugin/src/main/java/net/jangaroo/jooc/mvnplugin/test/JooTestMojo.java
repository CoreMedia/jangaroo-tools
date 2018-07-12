package net.jangaroo.jooc.mvnplugin.test;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.WebDriverManagerException;
import net.jangaroo.jooc.mvnplugin.AbstractSenchaMojo;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.JettyWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Range;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

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
@Mojo(name = "test",
        defaultPhase = LifecyclePhase.TEST,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class JooTestMojo extends AbstractSenchaMojo {

  private static final int JETTY_START_TIMEOUT_MILLIS = 30000;

  private static final String DEFAULT_TEST_APP_JSON = "default.test.app.json";

  /**
   * Directory whose joo/classes sub-directory contains compiled test classes.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  protected File testOutputDirectory;

  /**
   * the project's test resources
   */
  @Parameter(defaultValue = "${project.testResources}")
  protected List<Resource> testResources;

  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   * Default value is 10200.
   */
  @Parameter(property = "jooUnitJettyPortUpperBound")
  private int jooUnitJettyPortUpperBound = 10200;

  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   * When setting the flag <code>interactiveJooUnitTests</code>, this lower bound is
   * always used.
   * Default value is 10100.
   */
  @Parameter(property = "jooUnitJettyPortLowerBound")
  private int jooUnitJettyPortLowerBound = 10100;

  /**
   * The host name to use to reach the locally started Jetty, usually the default, "localhost".
   */
  @Parameter(property = "jooUnitJettyHost")
  private String jooUnitJettyHost = "localhost";

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
   * Set this to 'true' to build and serve the tests app, output the tests app URL, then wait for the developer
   * to manually run and debug the tests in a browser.
   */
  @Parameter(property = "interactiveJooUnitTests")
  private boolean interactiveJooUnitTests;

  /**
   * Output directory for test results.
   */
  @Parameter(defaultValue = "${project.build.directory}/surefire-reports/")
  private File testResultOutputDirectory;

  @Parameter
  private String testResultFileName;

  /**
   * Specifies the time in milliseconds to wait for the test results in the browser. Default is 30000 ms.
   */
  @Parameter(property = "jooUnitTestExecutionTimeout")
  private int jooUnitTestExecutionTimeout = 30000;

  /**
   * Specifies the number of retries when receiving unexpected result from phantomjs (crash?).
   * Default is 5.
   */
  @Parameter(property = "jooUnitMaxRetriesOnCrashes")
  private int jooUnitMaxRetriesOnCrashes = 5;

  /**
   * Defines the Selenium WebDriver browser to use. One of "chrome", "firefox", "opera", "edge", "iexplorer".
   * The WebDriver implementation for the chosen browser is automatically downloaded via WebDriverManager.
   * See the <a href="https://github.com/bonigarcia/webdrivermanager#readme">WebDriverManager README</a> for
   * all available configuration properties if the default configuration do not work for you.
   * 
   * <p>Default is to not use WebDriver at all, but use PhantomJS (see {@link #phantomBin}).</p>
   */
  @Parameter(property = "jooUnitWebDriverBrowser")
  private String jooUnitWebDriverBrowser = "";

  /**
   * Defines the class of the test suite for JooUnit tests.
   */
  @Parameter
  private String testSuite = null;

  /**
   * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on
   * occasion.
   */
  @Parameter(property = "maven.test.failure.ignore")
  private boolean testFailureIgnore;

  /**
   * The phantomjs executable. If not specified, it expects the phantomjs binary in the PATH.
   * If no phantomjs executable (or an outdated one) is found, falls back to Selenium WebDriver with
   * jooUnitWebDriverBrowser "chrome".
   */
  @SuppressWarnings({"UnusedDeclaration"})
  @Parameter(property = "phantomjs.bin")
  private String phantomBin = "phantomjs";

  /**
   * The resource timeout (maximum time in ms that a resource request may take)
   * to use for phantomjs when running JooUnit tests. Defaults to 10000 ms (10 s).
   * This has been introduced because phantomjs sometimes dead-locks when loading resources.
   * The limit should be sufficiently large so that it is only reached when such dead-lock
   * is encountered. A resource timeout is handled as if phantomjs crashed, i.e.
   * <code>jooUnitMaxRetriesOnCrashes</code> applies. This is intended, because resource
   * load dead-locks have been observed to recur two or more times in a row before they
   * vanish.
   */
  @Parameter(property = "jooUnitResourceTimeout")
  private int jooUnitResourceTimeout = 10000;

  /**
   * Set this to true to enable phantomjs debug output.
   */
  @Parameter(property = "phantomjsDebug")
  private boolean phantomjsDebug;


  /**
   * Set this to false to disable phantomjs web security.
   */
  @Parameter(property = "phantomjsWebSecurity")
  private boolean phantomjsWebSecurity = true;

  public void execute() throws MojoExecutionException, MojoFailureException {
    boolean doSkip = skip || skipTests || skipJooUnitTests;
    if (doSkip || testSuite == null) {
      getLog().info("Skipping generation of Jangaroo test app: " + (doSkip ? "tests skipped." : "no tests found."));
    } else {
      getLog().info("Creating Jangaroo test app below " + testOutputDirectory);
      createWebApp(testOutputDirectory);

      // sencha -cw target\test-classes config -prop skip.sass=1 -prop skip.resources=1 then app refresh
      new SenchaCmdExecutor(testOutputDirectory, "config -prop skip.sass=1 -prop skip.resources=1 then app refresh", getLog(), getSenchaLogLevel()).execute();

      File baseDir = new File(project.getBuild().getDirectory(), SenchaUtils.TEST_APP_DIRECTORY_NAME);

      JettyWrapper jettyWrapper = new JettyWrapper(getLog(), baseDir);
      Range<Integer> portRange = interactiveJooUnitTests
              ? Range.is(jooUnitJettyPortLowerBound)
              : Range.between(jooUnitJettyPortLowerBound, jooUnitJettyPortUpperBound);
      try {
        jettyWrapper.start(jooUnitJettyHost, portRange);

        String url = getTestUrl(jettyWrapper.getUri(), baseDir);
        getLog().info("Test-URL: " + url);

        if (interactiveJooUnitTests) {
          jettyWrapper.blockUntilInterrupted();
        } else {
          jettyWrapper.waitUntilStarted(JETTY_START_TIMEOUT_MILLIS);
          runTests(url);
        }
      } catch (JettyWrapper.JettyWrapperException e) {
        throw new MojoExecutionException("Could not start Jetty", e);
      } finally {
        jettyWrapper.stop();
      }
    }
  }

  private void runTests(String url) throws MojoFailureException, MojoExecutionException {
    if (jooUnitWebDriverBrowser.isEmpty()) {
      try {
        File testResultOutputFile = new File(testResultOutputDirectory, getTestResultFileName());
        File phantomTestRunner = new File(testResultOutputDirectory, "phantomjs-joounit-page-runner.js");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/net/jangaroo/jooc/mvnplugin/phantomjs-joounit-page-runner.js"), phantomTestRunner);
        final PhantomJsTestRunner phantomJsTestRunner = new PhantomJsTestRunner(phantomBin, url, testResultOutputFile.getPath(), phantomTestRunner.getPath(), jooUnitTestExecutionTimeout, jooUnitMaxRetriesOnCrashes, jooUnitResourceTimeout, phantomjsDebug, phantomjsWebSecurity, getLog());
        if (phantomJsTestRunner.canRun()) {
          executePhantomJs(testResultOutputFile, phantomJsTestRunner);
          return;
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot create local copy of phantomjs-joounit-page-runner.js", e);
      }
    }
    // jooUnitWebDriverBrowser configured or no PhantomJS: use Selenium WebDriver
    executeSelenium(url);
  }

  private void executePhantomJs(File testResultOutputFile, PhantomJsTestRunner phantomJsTestRunner) throws MojoFailureException, MojoExecutionException {
    getLog().info("running phantomjs: " + phantomJsTestRunner.toString());
    try {
      final boolean exitCode = phantomJsTestRunner.execute();
      if (exitCode) {
        evalTestOutput(new FileReader(testResultOutputFile));
      } else {
        signalError();
      }
    } catch (CommandLineException | IOException | ParserConfigurationException | SAXException e) {
      throw wrap(e);
    }
  }

  /**
   * Create the Jangaroo Web app in the given Web app directory.
   *
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws org.apache.maven.plugin.MojoExecutionException if anything goes wrong
   */
  protected void createWebApp(File webappDirectory) throws MojoExecutionException {
    // only generate app if senchaCfg does not exist
    if (SenchaUtils.doesSenchaAppExist(webappDirectory)) {
      getLog().info("Sencha app already exists, skip generating one");
      return;
    }
    getLog().info(String.format("Generating Sencha App %s for unit tests...", webappDirectory));
    FileHelper.ensureDirectory(webappDirectory);
    SenchaUtils.generateSenchaTestAppFromTemplate(webappDirectory, project, getSenchaPackageName(project), testSuite, toolkit, getLog(), getSenchaLogLevel());
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

  protected MojoExecutionException wrap(Exception e) {
    return new MojoExecutionException(e.toString(), e);
  }

  protected String getTestUrl(URI serverUri, File workspaceDir) throws MojoExecutionException {
    String path = workspaceDir.toURI().relativize(testOutputDirectory.toURI()).getPath();
    String serverUriString = serverUri.toString();
    if (!serverUriString.endsWith("/")) {
      serverUriString += "/";
    }
    // "?cache" because phantomjs@2.1.1. seems to have a problem with the cached resources
    return serverUriString + path + "?cache";
  }

  private void executeSelenium(String testsHtmlUrl) throws MojoExecutionException, MojoFailureException {
    getLog().info("JooTest report directory: " + testResultOutputDirectory.getAbsolutePath());
    WebDriver driver;
    try {
      driver = createWebDriver();
    } catch (IllegalArgumentException e) {
      throw new MojoExecutionException("Unknown jooUnitWebDriverBrowser configuration value '" + jooUnitWebDriverBrowser + "'.");
    } catch (WebDriverManagerException e) {
      throw new MojoExecutionException("Failed to set up WebDriver.", e);
    }
    try {
      getLog().debug("Opening " + testsHtmlUrl);
      driver.get(testsHtmlUrl);
      getLog().debug("Waiting for test results for " + jooUnitTestExecutionTimeout + "ms ...");

      JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
      new WebDriverWait(driver, jooUnitTestExecutionTimeout / 1000).until(new Function<WebDriver, Boolean>() {
        @Nullable
        @Override
        public Boolean apply(@Nullable WebDriver webDriver) {
          return (Boolean) javascriptExecutor.executeScript("return window.result != null || window.classLoadingError != null");
        }
      });
      String classLoadingError = (String) javascriptExecutor.executeScript("return window.classLoadingError");
      if (classLoadingError != null && !classLoadingError.equals("null")) {
        throw new MojoExecutionException(classLoadingError);
      }

      String testResultXml = (String) javascriptExecutor.executeScript("return window.result");
      writeResultToFile(testResultXml);
      evalTestOutput(new StringReader(testResultXml));
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot write test results to file", e);
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("Cannot create a simple XML Builder", e);
    } catch (SAXException e) {
      throw new MojoExecutionException("Cannot parse test result", e);
    } catch (WebDriverException e) {
      throw new MojoExecutionException("WebDriver exception during test execution.", e);
    } finally {
      driver.quit();
    }
  }

  private WebDriver createWebDriver() throws IllegalArgumentException, WebDriverManagerException {
    DriverManagerType driverManagerType = jooUnitWebDriverBrowser.isEmpty() ? DriverManagerType.CHROME
            : DriverManagerType.valueOf(jooUnitWebDriverBrowser.toUpperCase()); // may throw IllegalArgumentException
    getLog().info("Setting up WebDriver for " + jooUnitWebDriverBrowser + ".");
    WebDriverManager.getInstance(driverManagerType).setup();
    switch (driverManagerType) {
      case CHROME:
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        return new ChromeDriver(chromeOptions);
      case FIREFOX:
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(true);
        return new FirefoxDriver(firefoxOptions);
      case EDGE:
        return new EdgeDriver(); // no headless mode yet :-(
      case IEXPLORER:
        return new InternetExplorerDriver(); // no headless mode :-(
      case OPERA:
        return new OperaDriver(); // no headless mode :-(
    }
    throw new IllegalArgumentException();
  }

  File writeResultToFile(String testResultXml) throws IOException {
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

  private static void signalError() throws MojoExecutionException {
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

  public void setTestResources(List<org.apache.maven.model.Resource> resources) {
    this.testResources = resources;
  }

  public void setTestFailureIgnore(boolean b) {
    this.testFailureIgnore = b;
  }

}
