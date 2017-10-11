package net.jangaroo.jooc.mvnplugin.proxy;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.JettyWrapper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Starts a Jetty server serving the static resources below the Sencha workspace root (i.e. the parent directory of the
 * {@code .remote-packages} directory).
 * <br>
 * If the parameters {@code jooProxyTargetUri} and {@code jooProxyPathSpec} are provided, all requests matching the
 * {@code jooProxyPathSpec} are proxied to the {@code jooProxyTargetUri}.
 */
@Mojo(name = "run")
public class RunMojo extends AbstractMojo {

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  /**
   * The host name of the started server. Defaults to 'localhost'.
   */
  @Parameter(property = "jooProxyHost")
  private String jooProxyHost = "localhost";

  /**
   * The port of the started server. Defaults to 8080.
   */
  @Parameter(property = "jooProxyPort")
  private int jooProxyPort = 8080;

  /**
   * The url to which all proxied requests are forwarded to.
   */
  @Parameter(property = "jooProxyTargetUri")
  private String jooProxyTargetUri;

  /**
   * The pattern that determines which requests should be proxied.
   */
  @Parameter(property = "jooProxyPathSpec")
  private String jooProxyPathSpec;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File baseDir = SenchaUtils.remotePackagesDir(session).getParentFile();

    JettyWrapper jettyWrapper = jooProxyTargetUri != null && jooProxyPathSpec != null
            ? new ProxyJettyWrapper(getLog(), jooProxyTargetUri, jooProxyPathSpec)
            : new JettyWrapper(getLog());
    try {
      jettyWrapper.start(jooProxyHost, jooProxyPort, baseDir);
      jettyWrapper.blockUntilInterrupted();
    } catch (JettyWrapper.JettyHelperException e) {
      throw new MojoExecutionException("Could not start Jetty", e);
    } finally {
      jettyWrapper.stop();
    }
  }
}
