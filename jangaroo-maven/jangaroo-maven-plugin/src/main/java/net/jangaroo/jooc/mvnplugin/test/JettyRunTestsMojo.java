package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jetty.server.Server;

import java.io.File;

/**
 * Starts a Jetty server that serves the application to run JooUnit tests.
 * This is intended to be used for interactive debugging in the browser.
 * <p></p>Usage: <code>mvn jangaroo:jetty-run-test</code></p>
 *
 * @goal jetty-run-tests
 * @execute phase="test-compile"
 * @phase test
 * @requiresDependencyResolution test
 */
public class JettyRunTestsMojo extends JooTestMojoBase {

  public void execute() throws MojoExecutionException {
    Server server = jettyRunTest(false);
    String url = getJettyUrl(server);
    getLog().info("Test-URL: " + url + "/" + testsHtml.replace(File.separatorChar, '/'));
    try {
      server.join();
    } catch (InterruptedException e) {
      // okay, good-bye!
    }
    stopServerIgnoreException(server);
  }

}
