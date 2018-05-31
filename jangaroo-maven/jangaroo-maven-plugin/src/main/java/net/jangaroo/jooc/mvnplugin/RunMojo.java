package net.jangaroo.jooc.mvnplugin;

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
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.Type.JANGAROO_APP_PACKAGING;
import static net.jangaroo.jooc.mvnplugin.Type.JANGAROO_SWC_PACKAGING;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.*;

/**
 * Starts a Jetty server serving the static resources of the workspace of an app or unit test app.
 * <br>
 * If the parameters {@code jooProxyTargetUri} and {@code jooProxyPathSpec} are provided, all requests matching the
 * {@code jooProxyPathSpec} are proxied to the {@code jooProxyTargetUri}.
 * This is convenient to proxy-in some HTTP(S)-based service.
 */
@Mojo(name = "run")
public class RunMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  /**
   * The host name of the started server. Defaults to 'localhost'.
   * To expose the server on all network interfaces, use 0.0.0.0 instead.
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
    List<MavenProject> projects = session.getProjects();
    boolean isAppPackaging = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging());

    File baseDir = projects.size() == 1
            ? new File(project.getBuild().getDirectory(), isAppPackaging ? APP_DIRECTORY_NAME : TEST_APP_DIRECTORY_NAME)
            : SenchaUtils.remotePackagesDir(session).getParentFile();

    JettyWrapper jettyWrapper = new JettyWrapper(getLog(), baseDir);

    jettyWrapper.setStaticResourcesServletConfigs(jooStaticResourcesServletConfigs);

    if (jooProxyServletConfigs != null && !jooProxyServletConfigs.isEmpty()) {
      jettyWrapper.setProxyServletConfigs(jooProxyServletConfigs);
    } else if (jooProxyTargetUri != null && jooProxyPathSpec != null) {
      jettyWrapper.setProxyServletConfigs(Collections.singletonList(
              new ProxyServletConfig(jooProxyTargetUri, jooProxyPathSpec)));
    } else if (jooProxyTargetUri != null){
      getLog().warn("Ignoring 'jooProxyTargetUri' since there is no 'jooProxyPathSpec'.");
    } else if (jooProxyPathSpec != null){
      getLog().warn("Ignoring 'jooProxyPathSpec' since there is no 'jooProxyTargetUri'.");
    }

    try {
      jettyWrapper.start(jooJettyHost, jooJettyPort);

      getLog().info("Started Jetty server at: " + jettyWrapper.getUri());

      projects.forEach(project -> logJangarooAppUrl(baseDir, jettyWrapper, project));

      jettyWrapper.blockUntilInterrupted();
    } catch (JettyWrapper.JettyWrapperException e) {
      throw new MojoExecutionException("Could not start Jetty", e);
    } finally {
      jettyWrapper.stop();
    }
  }

  private void logJangarooAppUrl(File baseDir, JettyWrapper jettyWrapper, MavenProject project) {
    String packaging = project.getPackaging();

    if (JANGAROO_APP_PACKAGING.equals(packaging) || JANGAROO_SWC_PACKAGING.equals(packaging)) {
      String moduleTargetPath = baseDir.toURI().relativize(new File(project.getBuild().getDirectory()).toURI()).getPath();
      if (JANGAROO_APP_PACKAGING.equals(packaging)) {
        String appPath = moduleTargetPath + APP_TARGET_DIRECTORY + WELCOME_FILE_PATH;
        if (new File(baseDir, appPath).exists()) {
          getLog().info("Found Jangaroo app at: " + jettyWrapper.getUri() + appPath);
        }
      } else {
        String testAppPath = moduleTargetPath + TEST_APP_TARGET_DIRECTORY + WELCOME_FILE_PATH;
        if (new File(baseDir, testAppPath).exists()) {
          getLog().info("Found Jangaroo test app at: " + jettyWrapper.getUri() + testAppPath);
        }
      }
    }
  }
}
