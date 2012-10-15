package net.jangaroo.jooc.mvnplugin.test;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
      writeTestHtmlFile();
    }
    catch (IOException e) {
      throw new MojoFailureException("error unpacking resources", e);
    }
  }

  /**
   * Copies the test.html to the test output directory
   * @throws IOException
   */
  private void writeTestHtmlFile() throws IOException{

    // -- 1. read in template
    StringBuilder templateStringBuilder = new StringBuilder();
    InputStream template = getClass().getResourceAsStream("manual-test.html");
    InputStreamReader reader = new InputStreamReader(template);
    char[] buffer = new char[1024];
    int length;
    while( (length = reader.read(buffer)) > -1 ) {
      templateStringBuilder.append(buffer, 0, length);
    }
    template.close();
    String templateString = templateStringBuilder.toString();


    // -- 2. adjust template
    templateString = templateString.replace("this.is.a.pseudo.TestSuite", getTestClassName());


    // -- 3. write template
    FileUtils.write(new File(getTestOutputDirectory(), "test.html"), templateString);
  }


}
