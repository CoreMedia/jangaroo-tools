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
      File targetFile = new File(getTestOutputDirectory(), "test.html");
      writeTemplate(getClass().getResourceAsStream("manual-test.html"), targetFile, placeholders, overwrite);

      getLog().info("File "+targetFile+" can be opened in a web browser in order to execute the tests");

    }
    catch (Exception e) {
      throw new MojoFailureException("error unpacking resources", e);
    }
  }

}
