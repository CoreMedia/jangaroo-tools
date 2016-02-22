package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.jetty.server.Server;

import java.io.File;

/**
 * Starts a Jetty server that serves the application to run JooUnit tests.
 * This is intended to be used for interactive debugging in the browser.
 * <p></p>Usage: <code>mvn jangaroo:jetty-run-test</code></p>
 *
 * @execute phase="test-compile"
 */
@Mojo(name = "jetty-run-tests",
        defaultPhase = LifecyclePhase.TEST,
        requiresDependencyResolution = ResolutionScope.TEST)
public class JettyRunTestsMojo extends JooTestMojoBase {

  public void execute() throws MojoExecutionException {
    Server server = jettyRunTest(false);
    String url = getTestUrl(server);
    getLog().info("Test-URL: " + url);
    try {
      server.join();
    } catch (InterruptedException e) {
      // okay, good-bye!
    }
    stopServerIgnoreException(server);
  }

}
