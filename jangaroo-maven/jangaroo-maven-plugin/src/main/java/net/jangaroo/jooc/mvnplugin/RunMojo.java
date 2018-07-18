package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.proxy.AddDynamicPackagesServlet;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.JettyWrapper;
import net.jangaroo.jooc.mvnplugin.util.ProxyServletConfig;
import net.jangaroo.jooc.mvnplugin.util.StaticResourcesServletConfig;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.APP_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.LOCAL_PACKAGES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGES_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SEPARATOR;

/**
 * Starts a Jetty server serving the static resources of the workspace of an app or unit test app.
 * <br>
 * If the parameters {@code jooProxyTargetUri} and {@code jooProxyPathSpec} are provided, all requests matching the
 * {@code jooProxyPathSpec} are proxied to the {@code jooProxyTargetUri}.
 * This is convenient to proxy-in some HTTP(S)-based service.
 */
@Mojo(
        name = "run",
        requiresDependencyResolution = ResolutionScope.TEST
)
public class RunMojo extends AbstractSenchaMojo {

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

  public void setJooProxyTargetUri(String jooProxyTargetUri) {
    this.jooProxyTargetUri = jooProxyTargetUri.endsWith("/") ? jooProxyTargetUri : jooProxyTargetUri + "/";
  }

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
  private List<StaticResourcesServletConfig> jooStaticResourcesServletConfigs = Collections.emptyList();

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
    boolean isSwcPackaging = Type.JANGAROO_SWC_PACKAGING.equals(project.getPackaging());
    boolean isAppPackaging = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging());
    boolean isAppOverlayPackaging = Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(project.getPackaging());

    File baseDir = isAppPackaging || isAppOverlayPackaging ? new File(project.getBuild().getDirectory(), APP_DIRECTORY_NAME)
            : isSwcPackaging ? new File(project.getBuild().getTestOutputDirectory())
            : null;

    if (baseDir == null) {
      getLog().info(String.format("jangaroo:run does not support packaging '%s' (module %s:%s).", project.getPackaging(), project.getGroupId(), project.getArtifactId()));
      return;
    }

    JettyWrapper jettyWrapper = new JettyWrapper(getLog(), baseDir);

    List<StaticResourcesServletConfig> staticResourcesServletConfigs = new ArrayList<>(jooStaticResourcesServletConfigs);
    if (isSwcPackaging) {
      String senchaPackageName = SenchaUtils.getSenchaPackageName(project);
      staticResourcesServletConfigs.add(new StaticResourcesServletConfig(LOCAL_PACKAGES_PATH + senchaPackageName + SEPARATOR + "*"));
    } else if (isAppOverlayPackaging) {
      if ("/*".equals(jooProxyPathSpec)) {
        // If root path, the developer wants to proxy-in the base app, so all static resources are already there.
        // We just need to add all overlay packages as static resource folders:
        File[] packageDirs = new File(baseDir, PACKAGES_DIRECTORY_NAME).listFiles(File::isDirectory);
        if (packageDirs != null) {
          List<String> packageNames = Arrays.stream(packageDirs).map(File::getName).collect(Collectors.toList());
          for (String packageName : packageNames) {
            staticResourcesServletConfigs.add(new StaticResourcesServletConfig(LOCAL_PACKAGES_PATH + packageName + SEPARATOR + "*", SEPARATOR));
          }
          jettyWrapper.setAdditionalServlets(Collections.singletonMap(SEPARATOR + DYNAMIC_PACKAGES_FILENAME,
                  new AddDynamicPackagesServlet(jooProxyTargetUri + DYNAMIC_PACKAGES_FILENAME, packageNames)
          ));
        }
      } else {
        // if any other or no proxy path spec, we have to set up the static resources of the base app.
        Dependency baseAppDependency = findRequiredJangarooAppDependency(project);
        String baseAppVersionKey = ArtifactUtils.key(baseAppDependency.getGroupId(), baseAppDependency.getArtifactId(), baseAppDependency.getVersion());
        MavenProject baseAppProject = project.getProjectReferences().get(baseAppVersionKey);
        if (baseAppProject != null) {
          // base app is part of our Reactor, so we can determine its output directory:
          File appResourceDir = new File(baseAppProject.getBuild().getDirectory(), APP_DIRECTORY_NAME);
          jettyWrapper.addBaseDir(appResourceDir);
          getLog().info("Adding base app resource directory " + appResourceDir.getAbsolutePath());
        } else {
          String baseAppVersionlessKey = ArtifactUtils.versionlessKey(baseAppDependency.getGroupId(), baseAppDependency.getArtifactId());
          // base app is referenced externally, so we have to use the JAR artifact:
          Artifact baseAppArtifact = project.getArtifactMap().get(baseAppVersionlessKey);
          // TODO: null?
          File baseAppResourceJar = baseAppArtifact.getFile();
          // TODO: null?
          getLog().info("Adding base app JAR " + baseAppResourceJar.getAbsolutePath());
          jettyWrapper.setResourceJars(Collections.singletonList(baseAppResourceJar));
        }
        staticResourcesServletConfigs.add(new StaticResourcesServletConfig("/*", "/"));
      }
    }

    jettyWrapper.setStaticResourcesServletConfigs(staticResourcesServletConfigs);

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

      logJangarooAppUrl(baseDir, jettyWrapper, project);

      jettyWrapper.blockUntilInterrupted();
    } catch (JettyWrapper.JettyWrapperException e) {
      throw new MojoExecutionException("Could not start Jetty", e);
    } finally {
      jettyWrapper.stop();
    }
  }

  private void logJangarooAppUrl(File baseDir, JettyWrapper jettyWrapper, MavenProject project) {
    if (baseDir.exists()) {
      getLog().info("Found " + project.getPackaging() + " at: " + jettyWrapper.getUri());
    }
  }
}
