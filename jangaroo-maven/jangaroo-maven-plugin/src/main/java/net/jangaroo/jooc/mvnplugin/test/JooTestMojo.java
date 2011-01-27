package net.jangaroo.jooc.mvnplugin.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.FileResource;
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
import java.net.URISyntaxException;
import java.net.UnknownHostException;
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
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal", "ResultOfMethodCallIgnored"})
public class JooTestMojo extends AbstractMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;
  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. Defaults to ${project.build.outputDirectory}
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;
  /**
   * Output directory for the jangaroo artifact unarchiver. All jangaroo dependencies will be unpacked into this
   * directory.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   */
  protected File testOutputDirectory;
  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  protected File testSourceDirectory;
  /**
   * the tests.html file relative to the test resources folder
   *
   * @parameter expression="tests.html"
   */
  protected String testsHtml;
  /**
  * the tests.html file relative to the test resources folder
  *
  * @parameter expression="${project.testResources}"
  */
 protected List<Resource> testResources;
  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  protected boolean skip;
  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  protected boolean skipTests;

  /**
   * This is the list of projects currently slated to be built by Maven.
   *
   * @parameter expression="${reactorProjects}"
   * @required
   * @readonly
   */
  private List<MavenProject> projects;


  /**
   * Output directory for test results.
   *
   * @parameter expression="${project.build.directory}/surefire-reports/"  default-value="${project.build.directory}/surefire-reports/"
   * @required
   */
  private File testResultOutputDirectory;

  /**
   * The jetty port is selected randomly within an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   *
   * @parameter
   */
  private int jooUnitJettyPortUpperBound = 10200;

  /**
   * The jetty port is selected randomly within an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   *
   * @parameter
   */
  private int jooUnitJettyPortLowerBound = 10100;

  /**
   * Specifies the time in milliseconds to wait for the test results in the browser. Default is 3000ms.
   *
   * @parameter
   */
  private int jooUnitTestExecutionTimeout = 3000;


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
  private int jooUnitSeleniumRCPort = 4444;

  /**
   * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on
   * occasion.
   *
   * @parameter expression="${maven.test.failure.ignore}"
   */
  protected boolean testFailureIgnore;

  protected boolean isTestAvailable() {
   for(Resource r : testResources) {
      File testFile = new File(r.getDirectory(), testsHtml);
      if(testFile.exists()) {
        return true;
      }
    }
    getLog().info("The tests.html file '" + testsHtml + "' could not be found. Skipping.");
    return false;
  }

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skip && !skipTests) {
      if (isTestAvailable()) {
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
        getLog().info("JooTest report directory:" + testResultOutputDirectory.getAbsolutePath());
        ResourceHandler handler = new ResourceHandler();
        try {
          handler.setBaseResource(new FileResource(testOutputDirectory.toURI().toURL()));
        } catch (IOException e) {
          throw new MojoExecutionException(e.toString(), e);
        } catch (URISyntaxException e) {
          throw new MojoExecutionException(e.toString(), e);
        }
        Server server = startJetty(handler);
        Selenium selenium;
        String url;
        try {
          url = "http://" + InetAddress.getLocalHost().getHostName() + ":" + server.getConnectors()[0].getPort();
        } catch (UnknownHostException e) {
          throw new MojoExecutionException("I just don't know my own hostname ... ", e);
        }
        selenium = new DefaultSelenium(jooUnitSeleniumRCHost, jooUnitSeleniumRCPort, "*firefox", url);
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
          evalTestOutput(testResultXml);
          File result = new File(testResultOutputDirectory, "TEST-" + project.getArtifactId() + ".xml");
          FileUtils.writeStringToFile(result, testResultXml);
          result.setLastModified(System.currentTimeMillis());
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
    String failures = namedNodeMap.getNamedItem("failures").getNodeValue();
    String errors = namedNodeMap.getNamedItem("errors").getNodeValue();
    String tests = namedNodeMap.getNamedItem("tests").getNodeValue();
    //String skipped = namedNodeMap.getNamedItem("skipped").getNodeValue();
    getLog().info("Tests run: " + tests + ", Failures: " + failures + ", Errors: " + errors + ", Skipped: 0" /*+ skipped*/);
    if (!testFailureIgnore && (Integer.parseInt(errors) > 0 || Integer.parseInt(failures) > 0)) {
      throw new MojoFailureException("There are test failures");
    }
  }


  private Server startJetty(Handler handler) throws MojoExecutionException {
    Server server;
    if (jooUnitJettyPortUpperBound != jooUnitJettyPortLowerBound) {
      Random r = new Random(System.currentTimeMillis());
      int jooUnitJettyPort = r.nextInt(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound) + jooUnitJettyPortLowerBound;

      server = new Server(jooUnitJettyPort);
      try {
        server.setHandler(handler);
        server.start();
      } catch (Exception e) {
        getLog().info("Failed starting Jetty on port " + jooUnitJettyPort + " failed. Retrying ...");
        try {
          server.stop();
        } catch (Exception e1) {
          getLog().error("Stopping Jetty failed. Never mind.");
        }
        jooUnitJettyPort = r.nextInt(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound) + jooUnitJettyPortLowerBound;

        server = new Server(jooUnitJettyPort);
        try {
          server.setHandler(handler);
          server.start();
        } catch (Exception e1) {
          getLog().info("Failed starting Jetty on port " + jooUnitJettyPort + " failed. Retrying ...");
          try {
            server.stop();
          } catch (Exception e2) {
            getLog().error("Stopping Jetty failed. Never mind.");
          }
          jooUnitJettyPort = r.nextInt(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound) + jooUnitJettyPortLowerBound;

          server = new Server(jooUnitJettyPort);
          try {
            server.setHandler(handler);
            server.start();
          } catch (Exception e2) {
            getLog().error("Failed starting Jetty on port " + jooUnitJettyPort + " failed. Stop retrying!!");
            try {
              server.stop();
            } catch (Exception e3) {
              getLog().error("Stopping Jetty failed. Never mind.");
            }
            throw new MojoExecutionException("Cannot start jetty server");
          }
        }
      }


    } else {
      server = new Server(jooUnitJettyPortLowerBound);
      try {
        server.setHandler(handler);
        server.start();
      } catch (Exception e) {
        getLog().error("Failed starting Jetty on port " + jooUnitJettyPortLowerBound + " failed.");
        try {
          server.stop();
        } catch (Exception e1) {
          getLog().error("Stopping Jetty failed. Never mind.");
        }
        throw new MojoExecutionException("Cannot start jetty server on port " + jooUnitJettyPortLowerBound);
      }
    }
    return server;
  }
}
