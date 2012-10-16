package net.jangaroo.jooc.mvnplugin.test;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import java.io.File;
import java.io.IOException;
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

      // if there already is a a file src/test/resources/test.html provided by the user then this file is used
      boolean overwrite = false;

      // prepare test.html
      Properties placeholders = createPlaceholders();
      writeTemplate(getClass().getResourceAsStream("manual-test.html"), new File(getTestOutputDirectory(), "test.html"), placeholders, overwrite);

    }
    catch (Exception e) {
      throw new MojoFailureException("error unpacking resources", e);
    }
  }

}
