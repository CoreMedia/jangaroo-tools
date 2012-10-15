package net.jangaroo.jooc.mvnplugin.test;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
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
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Executes unit tests using PhantomJS
 *
 * @requiresDependencyResolution test
 * @goal run-test-phantomjs
 * @phase test
 */
public class PhantomJsTestMojo extends TestMojoBase {

  public static enum Mode {
    direct,
    html
  }



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
   * The (actionscript) test's class name to run, e.g. com.mycompany.MyTestSuite. Needs to be an implementation of
   * flexunit.framework.Test (e.g. flexunit.framework.TestSuite)
   *
   *
   * @parameter
   * @required
   */
  private String test;

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
   * Additional arguments to be passed to phantomjs. Expected to be a JSON object, i.e.
   * <code>
   * {loglevel:'all'}
   * </code>
   *
   * @parameter expression="${phantomjs.args}" default-value="{loglevel:'all'}"
   */
  private String args;


  /**
   * The execution mode: Either directly ("direct") or embedded into a html page ("html")
   * @parameter default-value="direct"
   */
  private Mode mode;


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

  private void runTests() throws Exception {

    if( skipTests ) {
      getLog().info("Skipping tests");
      return;
    }

    String bootstrapScript = mode == Mode.direct ? "joo/phantomjs-joounit-runner.js" : "joo/phantomjs-joounit-page-runner.js";

    PhantomJsTestRunner runner = new PhantomJsTestRunner(executable, testOutputDirectory, bootstrapScript, test, args, timeout, getLog());
    getLog().info("trying to run phantomjs first: " + runner.toString());
    if (runner.canRun()) {

      boolean exitCode = runner.execute();
      String testResultXml = runner.getTestResult();
      writeResultToFile(test, testResultXml);
      evalTestOutput(testResultXml);
      if (!exitCode) {
        throw new MojoFailureException("There are test failures");
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

  private void evalTestOutput(String testResultXml) throws ParserConfigurationException, IOException, SAXException, MojoFailureException {
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
      throw new MojoFailureException("There are test failures");
    }
  }
}
