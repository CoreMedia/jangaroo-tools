package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Profile;
import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.FileResource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;
import java.util.List;
import java.net.URISyntaxException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

/**
 * @goal test
 * @phase test
 */
public class JooTestMojo extends AbstractJooTestMojo {


  /**
   * This is the list of projects currently slated to be built by Maven.
   *
   * @parameter expression="${reactorProjects}"
   * @required
   * @readonly
   */
  private List<MavenProject> projects;


  /**
   * Output directory for the janagroo artifact  unarchiver. All jangaroo dependencies will be unpacked into
   * this directory.
   *
   * @parameter expression="${project.build.directory}/surefire-reports/"  default-value="${project.build.testOutputDirectory}"
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
   * Defines the Selenium RC host. Default is hudson.
   *
   * @parameter
   */
  private String jooUnitSeleniumRCHost = "hudson";

  /**
   * Defines the Selenium RC port. Default is 4444.
   *
   * @parameter
   */
  private int jooUnitSeleniumRCPort = 4444;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isTestAvailable()) {
      if (!isIntegrationtestActive()) {
        getLog().info("+----------------------------------------------------------------------+");
        getLog().info("|  JooTestMojo is skipped due to inactive profile 'integrationtest'    |");
        getLog().info("+----------------------------------------------------------------------+");
      } else {
        try {
          InetAddress inetAddress = InetAddress.getAllByName(jooUnitSeleniumRCHost)[0];
        } catch (UnknownHostException e) {
          throw new MojoExecutionException("Cannot resolve host " + jooUnitSeleniumRCHost +
                  ". Please specify a host running the selenium remote control or skip tests" +
                  " by deactivating the integrationtest profile (mvn -P !integrationtest)!", e);
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
          getLog().debug("Opening " + url + "/tests.html");
          selenium.open(url + "/tests.html");
          getLog().debug("Waiting for test results for " + jooUnitTestExecutionTimeout + "ms ...");
          selenium.waitForCondition("selenium.browserbot.getCurrentWindow().result != null", "" + jooUnitTestExecutionTimeout);
          String testResultXml = selenium.getEval("selenium.browserbot.getCurrentWindow().result");
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

          File result = new File(testResultOutputDirectory, "TEST-" + testSuiteName + ".xml");
          FileUtils.writeStringToFile(result, testResultXml);
        } catch (IOException e) {
          throw new MojoExecutionException("Cannot write test results to file", e);
        } catch (ParserConfigurationException e) {
          throw new MojoExecutionException("Cannot create a simple XML Builder", e);
        } catch (SAXException e) {
          throw new MojoExecutionException("Cannot parse test result", e);
        } catch (SeleniumException e) {
          throw new MojoExecutionException("Selenium tests failed", e);
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

  private boolean isIntegrationtestActive() {
    for (MavenProject mavenProject : projects) {
      for (Profile profile : ((List<Profile>) mavenProject.getActiveProfiles())) {
        getLog().debug("Active Profile: " + profile.getId());
        if ("integrationtest".equals(profile.getId())) {
          return true;
        }
      }
    }
    return false;
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
