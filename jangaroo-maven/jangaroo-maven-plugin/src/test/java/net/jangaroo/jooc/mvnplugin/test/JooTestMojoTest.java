package net.jangaroo.jooc.mvnplugin.test;

import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 */
public class JooTestMojoTest extends TestCase {
  private JooGenerateTestAppMojo jooGenerateTestAppMojo;
  private JooTestMojo jooTestMojo;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    jooGenerateTestAppMojo = new JooGenerateTestAppMojo();
    jooTestMojo = new JooTestMojo();
  }

  public void testSkip() throws MojoExecutionException, MojoFailureException {
    jooGenerateTestAppMojo.skip();
    // skip skips everything so no error expected
    jooGenerateTestAppMojo.execute();
    jooTestMojo.skip();
    // skip skips everything so no error expected
    jooTestMojo.execute();
  }

  public void testSkipTests() throws MojoExecutionException, MojoFailureException {
    jooGenerateTestAppMojo.skipTests();
    // skip skips everything so no error expected
    jooGenerateTestAppMojo.execute();
    jooTestMojo.skipTests();
    // skip skips everything so no error expected
    jooTestMojo.execute();
  }

  public void testNoTestsAvailable() throws MojoExecutionException, MojoFailureException, IOException {
    File f = File.createTempFile("JooTestMojoTest", "");
    Assert.assertTrue(f.delete());
    Assert.assertTrue(f.mkdirs());

    jooTestMojo.execute();
    Assert.assertTrue(f.delete());
  }

  public void testEvalTestOutputSuccess() throws MojoFailureException, IOException, SAXException, ParserConfigurationException {
    String testResult = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
            "<testsuite errors=\"0\" failures=\"0\" name=\"com.coremedia.ui.data::BeanImplTest\" tests=\"21\" time=\"2814\"></testsuite>";
    jooTestMojo.evalTestOutput(new StringReader(testResult));
  }

  public void testEvalTestOutputFailure() throws IOException, SAXException, ParserConfigurationException {
    String testResult = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
            "<testsuite errors=\"0\" failures=\"1\" name=\"com.coremedia.ui.data::BeanImplTest\" tests=\"21\" time=\"2814\"></testsuite>";
    try {
      jooTestMojo.evalTestOutput(new StringReader(testResult));
    } catch (MojoFailureException e) {
      return;
    }
    fail("Should reach that point (testing for exception)");
  }

  public void testEvalTestOutputFailureIgnoreFail() throws IOException, SAXException, ParserConfigurationException {
    String testResult = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
            "<testsuite errors=\"0\" failures=\"1\" name=\"com.coremedia.ui.data::BeanImplTest\" tests=\"21\" time=\"2814\"></testsuite>";
    try {
      jooTestMojo.testFailureIgnore();
      jooTestMojo.evalTestOutput(new StringReader(testResult));
    } catch (MojoFailureException e) {
      fail("Shouldn't fail since testFailureIgnore=true");
    }
  }


}
