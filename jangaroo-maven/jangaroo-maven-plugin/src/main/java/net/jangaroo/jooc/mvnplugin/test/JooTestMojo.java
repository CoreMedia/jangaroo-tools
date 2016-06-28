package net.jangaroo.jooc.mvnplugin.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.mortbay.jetty.plugin.JettyWebAppContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.inject.Inject;
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
import java.util.Collections;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Executes JooUnit tests.
 * Unpacks all dependency to its output directory, generates a tests app which runs the script
 * given by <code>runTestsJs</code>. This usually runs JooUnit's BrowserRunner with a given
 * TestSuite.
 * Then the Test Mojo starts a Jetty on a random port between <code>jooUnitJettyPortLowerBound</code> and
 * <code>jooUnitJettyPortUpperBound</code> and prints out the resulting test app URL.
 * <p>Tests are executed in one of three ways:</p>
 * <ol>
 *   <li>
 *     If available, PhantomJS is started with a script that loads the test app and stores the
 *     test result in the report file in XML format.
 *   </li>
 *   <li>
 *     Otherwise, the Mojo tries to start a browser via the Selenium server given by
 *     <code>jooUnitSeleniumRCHost</code> with the test app URL and tries to collect the test result
 *     through Selenium.
 *   </li>
 *   <li>
 *     Another option is to set the system property <code>interactiveJooUnitTests</code>
 *     (<code>mvn test -DinteractiveJooUnitTests</code>), which simply halts Maven execution
 *     and gives you the opportunity to load and debug the test app in a browser.
 *   </li>
 * </ol>
 * Option 1 and 2 respect the configured <code>jooUnitTestExecutionTimeout</code> (ms)
 * and interrupt the tests after that time, resulting in a test failure.
 */
@Mojo(name = "test",
        defaultPhase = LifecyclePhase.TEST,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class JooTestMojo extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  /**
   * Directory whose META-INF/RESOURCES/joo/classes sub-directory contains compiled classes.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  /**
   * Directory whose joo/classes sub-directory contains compiled test classes.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  protected File testOutputDirectory;

  /**
   * The JavaScript file relative to the test resources folder that runs your test suite.
   */
  @Parameter(defaultValue = "run-tests.js")
  protected String runTestsJs;

  /**
   * Whether to load the test application in debug mode (#joo.debug).
   */
  @Parameter(defaultValue = "false")
  protected boolean debugTests;

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
   */
  @Parameter(property = "jooUnitJettyPortUpperBound", defaultValue = "10200")
  private int jooUnitJettyPortUpperBound;

  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   * When setting the flag <code>interactiveJooUnitTests</code>, this lower bound is
   * always used.
   */
  @Parameter(property = "jooUnitJettyPortLowerBound", defaultValue = "10100")
  private int jooUnitJettyPortLowerBound;

  /**
   * The host name to use to reach the locally started Jetty listenes, usually the default, "localhost".
   */
  @Parameter(property = "jooUnitJettyHost", defaultValue = "localhost")
  private String jooUnitJettyHost;

  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Inject
  protected RepositorySystem repositorySystem;


  @Parameter(defaultValue = "${localRepository}", required = true)
  private ArtifactRepository localRepository;

  @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
  private List<ArtifactRepository> remoteRepositories;

  /**
   * Source directory to scan for files to compile.
   */
  @Parameter(defaultValue = "${project.build.testSourceDirectory}")
  private File testSourceDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   */
  @Parameter(defaultValue = "${maven.test.skip}")
  private boolean skip;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   */
  @Parameter(defaultValue = "${skipTests}")
  private boolean skipTests;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   */
  @Parameter(defaultValue = "${skipJooUnitTests}")
  private boolean skipJooUnitTests;

  /**
   * Set this to 'true' to build and serve the tests app, output the tests app URL, then wait for the developer
   * to manually run and debug the tests in a browser.
   */
  @Parameter(defaultValue = "${interactiveJooUnitTests}")
  private boolean interactiveJooUnitTests;

  /**
   * Output directory for test results.
   */
  @SuppressWarnings({"UnusedDeclaration"})
  @Parameter(defaultValue = "${project.build.directory}/surefire-reports/")
  private File testResultOutputDirectory;

  @SuppressWarnings({"UnusedDeclaration"})
  @Parameter
  private String testResultFileName;

  /**
   * Specifies the time in milliseconds to wait for the test results in the browser. Default is 30000ms.
   */
  @SuppressWarnings("FieldCanBeLocal")
  @Parameter(defaultValue = "30000")
  private int jooUnitTestExecutionTimeout = 30000;

  /**
   * Specifies the number of retries when receiving unexpected result from phantomjs (crash?).
   * Default is 5.
   */
  @SuppressWarnings("FieldCanBeLocal")
  @Parameter(defaultValue = "5")
  private int jooUnitMaxRetriesOnCrashes = 5;

  /**
   * Defines the Selenium RC host. Default is localhost.
   * If the system property SELENIUM_RC_HOST is set, it is used prior to the
   * maven parameter.
   */
  @Parameter(defaultValue = "localhost")
  private String jooUnitSeleniumRCHost = "localhost";

  /**
   * Defines the class of the test suite for JooUnit tests.
   */
  @Parameter
  private String testSuite = null;

  /**
   * Defines the Selenium RC port. Default is 4444.
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  @Parameter(defaultValue = "4444")
  private int jooUnitSeleniumRCPort = 4444;

  /**
   * Selenium browser start command. Default is *firefox
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  @Parameter(defaultValue = "*firefox")
  private String jooUnitSeleniumBrowserStartCommand = "*firefox";

  /**
   * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on
   * occasion.
   */
  @Parameter(defaultValue = "${maven.test.failure.ignore}")
  private boolean testFailureIgnore;

  /**
   * The phantomjs executable. If not specified, it expects the phantomjs binary in the PATH.
   * If not phantomjs executable (or an outdated one) is found, falls back to Selenium.
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  @Parameter(property = "phantomjs.bin", defaultValue = "phantomjs")
  private String phantomBin;

  /**
   * The log level to use for Sencha Cmd.
   * The log level for Maven is kind of the base line which determines which log entries are actually shown in the output.
   * When you Maven log level is "info", no "debug" messages for Sencha Cmd are logged.
   * If no log level is given, the Maven log level will be used.
   */
  @Parameter(property = "senchaLogLevel")
  private String senchaLogLevel;

  public void execute() throws MojoExecutionException, MojoFailureException {
    boolean doSkip = skip || skipTests || skipJooUnitTests;
    if (doSkip || testSuite == null) {
      getLog().info("Skipping generation of Jangaroo test app: " + (doSkip ? "tests skipped." : "no tests found."));
    } else {
      getLog().info("Creating Jangaroo test app below " + testOutputDirectory);
      createWebApp(testOutputDirectory);

      // sencha -cw target\test-classes config -prop skip.sass=1 -prop skip.resources=1 then app refresh
      new SenchaCmdExecutor(testOutputDirectory, "config -prop skip.sass=1 -prop skip.resources=1 then app refresh", getLog(), senchaLogLevel).execute();

      Server server = jettyRunTest(!interactiveJooUnitTests);
      String url = getTestUrl(server);
      getLog().info("Test-URL: " + url);

      try {
        if (interactiveJooUnitTests) {
          try {
            server.join();
          } catch (InterruptedException e) {
            // okay, good-bye!
          }
        } else {
          runTests(url);
        }
      } finally {
        stopServerIgnoreException(server);
      }
    }
  }

  private void runTests(String url) throws MojoFailureException, MojoExecutionException {
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
    }
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
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws org.apache.maven.plugin.MojoExecutionException if anything goes wrong
   */
  protected void createWebApp(File webappDirectory) throws MojoExecutionException {
    // only generate app if senchaCfg does not exist
    if (SenchaUtils.doesSenchaAppExist(webappDirectory)) {
      getLog().info("Sencha app already exists, skip generating one");
      return;
    }

    FileHelper.ensureDirectory(webappDirectory);

    String senchaAppName = getSenchaPackageName(project);
    String arguments = "generate app"
            + " -ext"
            + " -" + SenchaUtils.TOOLKIT_CLASSIC
            + " --template " + getSenchaPackageName(SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID, SenchaUtils.SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID) + "/tpl"
            + " --path=\"\""
            + " --refresh=false"
            + " -DmoduleName=" + getSenchaPackageName(project)
            + " -DtestSuite=" + testSuite
            + " " + senchaAppName;
    getLog().info("Generating Sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(webappDirectory, arguments, getLog(), null);
    senchaCmdExecutor.execute();


    createApp();
  }

  private static boolean isTestDependency(Dependency dependency) {
    return Artifact.SCOPE_TEST.equals(dependency.getScope()) && Type.JAR_EXTENSION.equals(dependency.getType());
  }

  protected String getDefaultsJsonFileName() {
    return "default.test.app.json";
  }

  public void createApp() throws MojoExecutionException {
    File appJsonFile = new File(project.getBuild().getTestOutputDirectory(), "app.json");
    getLog().info(String.format("Generating Sencha App %s for unit tests...", appJsonFile.getPath()));

    SenchaAppConfigBuilder configBuilder = new SenchaAppConfigBuilder();
    try {
      configBuilder.destFile(project.getBuild().getTestOutputDirectory() + SenchaUtils.SEPARATOR + "app.json");
      configBuilder.defaults(getDefaultsJsonFileName());
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
      throw new MojoExecutionException("Could not build test app.json", e);
    }
  }

  protected String getJettyUrl(Server server) {
    return "http://" + jooUnitJettyHost + ":" + server.getConnectors()[0].getPort();
  }

  protected Server jettyRunTest(boolean tryPortRange) throws MojoExecutionException {
    JettyWebAppContext handler;
    try {
      handler = new JettyWebAppContext();
      handler.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
      List<org.eclipse.jetty.util.resource.Resource> baseResources = new ArrayList<>();
      File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());
      baseResources.add(toResource(workspaceDir));
      handler.setBaseResource(new ResourceCollection(baseResources.toArray(new org.eclipse.jetty.util.resource.Resource[baseResources.size()])));
      getLog().info("Using base resources " + baseResources);
      ServletHolder servletHolder = new ServletHolder("default", DefaultServlet.class);
      servletHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");
      handler.addServlet(servletHolder, "/");
      getLog().info("Set servlet cache control to 'do not cache'.");
    } catch (Exception e) {
      throw wrap(e);
    }
    return startJetty(handler, tryPortRange);
  }

  private org.eclipse.jetty.util.resource.Resource toResource(File file) throws MojoExecutionException {
    try {
      return org.eclipse.jetty.util.resource.Resource.newResource(file);
    } catch (IOException e) {
      throw wrap(e);
    }
  }

  private Server startJetty(Handler handler, boolean tryPortRange) throws MojoExecutionException {
    if (tryPortRange && jooUnitJettyPortUpperBound != jooUnitJettyPortLowerBound) {
      return startJettyOnRandomPort(handler);
    } else {
      try {
        return startJettyOnPort(handler, jooUnitJettyPortLowerBound);
      } catch (Exception e) {
        throw wrapJettyException(e, jooUnitJettyPortLowerBound);
      }
    }
  }

  private Server startJettyOnRandomPort(Handler handler) throws MojoExecutionException {
    List<Integer> ports = new ArrayList<>(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound + 1);
    for (int i = jooUnitJettyPortLowerBound; i <= jooUnitJettyPortUpperBound; i++) {
      ports.add(i);
    }
    Collections.shuffle(ports);
    int lastPort = ports.get(ports.size() - 1);
    Exception finalException = null;
    for (int jooUnitJettyPort : ports) {
      try {
        return startJettyOnPort(handler, jooUnitJettyPort);
      } catch (Exception e) {
        if (jooUnitJettyPort != lastPort) {
          getLog().info(String.format("Starting Jetty on port %d failed. Retrying ...", jooUnitJettyPort));
        } else {
          finalException = e;
        }
      }
    }
    throw wrapJettyException(finalException, lastPort);
  }

  private Server startJettyOnPort(Handler handler, int jettyPort) throws Exception {
    Server server = new Server(jettyPort);
    try {
      server.setHandler(handler);
      server.start();
      getLog().info(String.format("Started Jetty for unit tests on port %d.", jettyPort));
    } catch (Exception e) {
      stopServerIgnoreException(server);
      throw e;
    }
    return server;
  }

  protected MojoExecutionException wrap(Exception e) {
    return new MojoExecutionException(e.toString(), e);
  }

  private MojoExecutionException wrapJettyException(Exception e, int jettyPort) {
    getLog().error(String.format("Starting Jetty on port %d failed.", jettyPort));
    return new MojoExecutionException(String.format("Cannot start jetty server on port %d.", jettyPort), e);
  }

  protected void stopServerIgnoreException(Server server) {
    try {
      server.stop();
    } catch (Exception e1) {
      getLog().warn("Stopping Jetty failed. Never mind.");
    }
  }

  protected String getTestUrl(Server server) throws MojoExecutionException {
    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());
    if (workspaceDir == null) {
      throw new MojoExecutionException("No Sencha workspace.json found starting from " + project.getBasedir());
    }
    String path = workspaceDir.toURI().relativize(testOutputDirectory.toURI()).getPath();
    StringBuilder builder = new StringBuilder(getJettyUrl(server))
            .append("/").append(path);
    if (debugTests) {
      builder.append("#joo.debug");
    }
    return builder.toString();
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
      selenium.waitForCondition("selenium.browserbot.getCurrentWindow().result != null || selenium.browserbot.getCurrentWindow().classLoadingError != null", Integer.toString(jooUnitTestExecutionTimeout));
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

  public void setTestSourceDirectory(File f) {
    this.testSourceDirectory = f;
  }

  public void setTestResources(List<org.apache.maven.model.Resource> resources) {
    this.testResources = resources;
  }

  public void setTestFailureIgnore(boolean b) {
    this.testFailureIgnore = b;
  }

  static {
    org.eclipse.jetty.util.resource.Resource.setDefaultUseCaches(false);
  }
}
