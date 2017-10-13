package net.jangaroo.jooc.mvnplugin.proxy;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.JettyWrapper;
import net.jangaroo.jooc.mvnplugin.util.ProxyServletConfig;
import net.jangaroo.jooc.mvnplugin.util.StaticResourcesServletConfig;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Collections;
import java.util.List;

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
  @Parameter(property = "jooJettyHost")
  private String jooJettyHost = "localhost";

  /**
   * The port of the started server. Defaults to 8080.
   */
  @Parameter(property = "jooJettyPort")
  private int jooJettyPort = 8080;

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

  /**
   * The configurations for serving static resources.
   * The resource base paths have to be relative to the Sencha workspace root.
   * <br>
   * Per default all resources below the Sencha workspace root are served at '/'.
   * <p>
   *   <b>Experimental</b>
   * </p>
   */
  @Parameter
  private List<StaticResourcesServletConfig> jooStaticResourcesServletConfigs;

  /**
   * The configurations for a proxy servlet.
   * Used only when {@code jooProxyTargetUri} and {@code jooProxyPathSpec} are not set
   * <p>
   *   <b>Experimental</b>
   * </p>
   */
  @Parameter
  private List<ProxyServletConfig> jooProxyServletConfigs;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File baseDir = SenchaUtils.remotePackagesDir(session).getParentFile();

    JettyWrapper jettyWrapper = new JettyWrapper(getLog(), baseDir);

    jettyWrapper.setStaticResourcesServletConfigs(jooStaticResourcesServletConfigs);

    if (jooProxyTargetUri != null && jooProxyPathSpec != null) {
      jettyWrapper.setProxyServletConfigs(Collections.singletonList(
              new ProxyServletConfig(jooProxyTargetUri, jooProxyPathSpec)));
    } else {
      jettyWrapper.setProxyServletConfigs(jooProxyServletConfigs);
    }

    try {
      jettyWrapper.start(jooJettyHost, jooJettyPort);
      jettyWrapper.blockUntilInterrupted();
    } catch (JettyWrapper.JettyWrapperException e) {
      throw new MojoExecutionException("Could not start Jetty", e);
    } finally {
      jettyWrapper.stop();
    }
  }
}
