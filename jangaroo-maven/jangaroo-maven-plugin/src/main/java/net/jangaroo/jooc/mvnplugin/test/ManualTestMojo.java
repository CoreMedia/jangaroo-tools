package net.jangaroo.jooc.mvnplugin.test;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * A pseudo test mojo that simply unpacks resources and provides a test.html for manual invocation
 *
 * @requiresDependencyResolution test
 * @goal run-test-manual
 * @phase test
 */
public class ManualTestMojo extends TestMojoBase {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {

      unpackResources();

      // prepare test.html
      Properties tokens = new Properties();
      tokens.setProperty("TEST_CLASSNAME_PLACEHOLDER", getTestClassName());
      copyAndReplace(getClass().getResourceAsStream("manual-test.html"), new File(getTestOutputDirectory(), "test.html"), tokens);

    }
    catch (IOException e) {
      throw new MojoFailureException("error unpacking resources", e);
    }
  }

}
