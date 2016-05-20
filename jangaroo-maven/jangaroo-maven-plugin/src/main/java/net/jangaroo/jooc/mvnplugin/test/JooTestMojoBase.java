package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.mortbay.jetty.plugin.JettyWebAppContext;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Base class for running tests either automatically (JooTestMojo) or start test Jetty and keep it running (JettyRunTestsMojo).
 */
public abstract class JooTestMojoBase extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  /**
   * Directory whose META-INF/RESOURCES/joo/classes sub-directory contains compiled classes.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  /**
   * Directory whose joo/classes sub-directory contains compiled test classes.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  protected File testOutputDirectory;

  /**
   * The JavaScript file relative to the test resources folder that runs your test suite.
   */
  @Parameter(defaultValue = "run-tests.js")
  protected String runTestsJs;

  /**
   * Whether to load the test application in debug mode (#joo.debug).
   */
  @Parameter(defaultValue = "false")
  protected boolean debugTests;

  /**
   * the project's test resources
   */
  @Parameter(defaultValue = "${project.testResources}")
  protected List<org.apache.maven.model.Resource> testResources;

  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   */
  @Parameter(property = "jooUnitJettyPortUpperBound", defaultValue = "10200")
  private int jooUnitJettyPortUpperBound;

  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   * When using goal <code>jetty-run-tests</code>, this lower bound is
   * always used.
   */
  @Parameter(property = "jooUnitJettyPortLowerBound", defaultValue = "10100")
  private int jooUnitJettyPortLowerBound;

  /**
   * The host name to use to reach the locally started Jetty listenes, usually the default, "localhost".
   */
  @Parameter(property = "jooUnitJettyHost", defaultValue = "localhost")
  private String jooUnitJettyHost;

  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Inject
  protected RepositorySystem repositorySystem;

  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Inject
  private ArtifactResolver artifactResolver;

  @Inject
  private ArchiverManager archiverManager;

  @Parameter(defaultValue = "${localRepository}", required = true)
  private ArtifactRepository localRepository;

  @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
  private List<ArtifactRepository> remoteRepositories;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isTestAvailable()) {
      getLog().info("Creating Jangaroo test app below " + testOutputDirectory);
      createWebapp(testOutputDirectory);
    } else {
      getLog().info("Skipping generation of Jangaroo test app: no tests found.");
    }
  }

  /**
   * Create the Jangaroo Web app in the given Web app directory.
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws org.apache.maven.plugin.MojoExecutionException if anything goes wrong
   */
  protected void createWebapp(File webappDirectory) throws MojoExecutionException {
    if (webappDirectory.mkdirs()) {
      getLog().debug("created app directory " + webappDirectory);
    }
    UnArchiver unArchiver;
    try {
      unArchiver = archiverManager.getUnArchiver(Type.ZIP_EXTENSION);
    } catch (NoSuchArchiverException e) {
      throw new MojoExecutionException("No ZIP UnArchiver?!", e);
    }
    ArtifactResolutionRequest artifactResolutionRequest = new ArtifactResolutionRequest();
    String myVersion = project.getPluginArtifactMap().get("net.jangaroo:jangaroo-maven-plugin").getVersion();
    artifactResolutionRequest.setArtifact(repositorySystem.createArtifact("net.jangaroo", "sencha-app-template", myVersion, "runtime", "jar"));
    artifactResolutionRequest.setLocalRepository(localRepository);
    artifactResolutionRequest.setRemoteRepositories(remoteRepositories);
    ArtifactResolutionResult result = artifactResolver.resolve(artifactResolutionRequest);
    File appTemplateArtifactFile = result.getArtifacts().iterator().next().getFile();

    unArchiver.setSourceFile(appTemplateArtifactFile);
    unArchiver.setDestDirectory(webappDirectory);
    unArchiver.extract();

    createApp();
  }

  private static boolean isTestDependency(Dependency dependency) {
    return Artifact.SCOPE_TEST.equals(dependency.getScope()) && Type.JAR_EXTENSION.equals(dependency.getType());
  }

  protected String getDefaultsJsonFileName() {
    return "default.test.app.json";
  }

  public void createApp() throws MojoExecutionException {
    File appJsonFile = new File(project.getBuild().getTestOutputDirectory(), "app.json");
    getLog().info(String.format("Generating Sencha App %s for unit tests...", appJsonFile.getPath()));

    SenchaAppConfigBuilder configBuilder = new SenchaAppConfigBuilder();
    try {
      configBuilder.destFile(project.getBuild().getTestOutputDirectory() + SenchaUtils.SEPARATOR + "app.json");
      configBuilder.defaults(getDefaultsJsonFileName());
      configBuilder.destFileComment("Auto-generated test application configuration. DO NOT EDIT!");

      // require the package to test:
      configBuilder.require(getSenchaPackageName(project));
      // add test scope dependencies:
      List<Dependency> projectDependencies = project.getDependencies();
      for (Dependency dependency : projectDependencies) {
        if (isTestDependency(dependency)) {
          configBuilder.require(getSenchaPackageName(dependency.getGroupId(), dependency.getArtifactId()));
        }
      }

      configBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not build test app.json", e);
    }
  }

  protected String getJettyUrl(Server server) {
    return "http://" + jooUnitJettyHost + ":" + server.getConnectors()[0].getPort();
  }

  protected boolean isTestAvailable() {
    for (org.apache.maven.model.Resource r : testResources) {
      File testFile = new File(r.getDirectory(), runTestsJs);
      if (testFile.exists()) {
        return true;
      }
    }
    getLog().info("The JavaScript test run file '" + runTestsJs + "' could not be found. Skipping.");
    return false;
  }

  protected Server jettyRunTest(boolean tryPortRange) throws MojoExecutionException {
    JettyWebAppContext handler;
    try {
      handler = new JettyWebAppContext();
      handler.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
      List<Resource> baseResources = new ArrayList<>();
      File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());
      baseResources.add(toResource(workspaceDir));
      handler.setBaseResource(new ResourceCollection(baseResources.toArray(new Resource[baseResources.size()])));
      getLog().info("Using base resources " + baseResources);
      ServletHolder servletHolder = new ServletHolder("default", DefaultServlet.class);
      servletHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");
      handler.addServlet(servletHolder, "/");
      getLog().info("Set servlet cache control to 'do not cache'.");
    } catch (Exception e) {
      throw wrap(e);
    }
    return startJetty(handler, tryPortRange);
  }

  private Resource toResource(File file) throws MojoExecutionException {
    try {
      return Resource.newResource(file);
    } catch (IOException e) {
      throw wrap(e);
    }
  }

  private Server startJetty(Handler handler, boolean tryPortRange) throws MojoExecutionException {
    if (tryPortRange && jooUnitJettyPortUpperBound != jooUnitJettyPortLowerBound) {
      return startJettyOnRandomPort(handler);
    } else {
      try {
        return startJettyOnPort(handler, jooUnitJettyPortLowerBound);
      } catch (Exception e) {
        throw wrapJettyException(e, jooUnitJettyPortLowerBound);
      }
    }
  }

  private Server startJettyOnRandomPort(Handler handler) throws MojoExecutionException {
    List<Integer> ports = new ArrayList<>(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound + 1);
    for (int i = jooUnitJettyPortLowerBound; i <= jooUnitJettyPortUpperBound; i++) {
      ports.add(i);
    }
    Collections.shuffle(ports);
    int lastPort = ports.get(ports.size() - 1);
    Exception finalException = null;
    for (int jooUnitJettyPort : ports) {
      try {
        return startJettyOnPort(handler, jooUnitJettyPort);
      } catch (Exception e) {
        if (jooUnitJettyPort != lastPort) {
          getLog().info(String.format("Starting Jetty on port %d failed. Retrying ...", jooUnitJettyPort));
        } else {
          finalException = e;
        }
      }
    }
    throw wrapJettyException(finalException, lastPort);
  }

  private Server startJettyOnPort(Handler handler, int jettyPort) throws Exception {
    Server server = new Server(jettyPort);
    try {
      server.setHandler(handler);
      server.start();
      getLog().info(String.format("Started Jetty for unit tests on port %d.", jettyPort));
    } catch (Exception e) {
      stopServerIgnoreException(server);
      throw e;
    }
    return server;
  }

  protected MojoExecutionException wrap(Exception e) {
    return new MojoExecutionException(e.toString(), e);
  }

  private MojoExecutionException wrapJettyException(Exception e, int jettyPort) {
    getLog().error(String.format("Starting Jetty on port %d failed.", jettyPort));
    return new MojoExecutionException(String.format("Cannot start jetty server on port %d.", jettyPort), e);
  }

  protected void stopServerIgnoreException(Server server) {
    try {
      server.stop();
    } catch (Exception e1) {
      getLog().warn("Stopping Jetty failed. Never mind.");
    }
  }

  protected String getTestUrl(Server server) throws MojoExecutionException {
    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());
    if (workspaceDir == null) {
      throw new MojoExecutionException("No Sencha workspace.json found starting from " + project.getBasedir());
    }
    String path = workspaceDir.toURI().relativize(testOutputDirectory.toURI()).getPath();
    StringBuilder builder = new StringBuilder(getJettyUrl(server))
            .append("/").append(path);
    if (debugTests) {
      builder.append("#joo.debug");
    }
    return builder.toString();
  }

  static {
    Resource.setDefaultUseCaches(false);
  }
}
