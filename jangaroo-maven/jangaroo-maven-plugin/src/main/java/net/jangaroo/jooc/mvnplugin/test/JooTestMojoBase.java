package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.mortbay.jetty.plugin.JettyWebAppContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for running tests either automatically (JooTestMojo) or start test Jetty and keep it running (JettyRunTestsMojo).
 */
public abstract class JooTestMojoBase extends AbstractMojo {
  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  @SuppressWarnings({"UnusedDeclaration"})
  protected MavenProject project;
  /**
   * Directory whose META-INF/RESOURCES/joo/classes sub-directory contains compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private File outputDirectory;
  /**
   * Directory whose joo/classes sub-directory contains compiled test classes.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   */
  protected File testOutputDirectory;
  /**
   * the tests.html file relative to the test resources folder
   *
   * @parameter default-value="tests.html"
   */
  @SuppressWarnings({"UnusedDeclaration"})
  protected String testsHtml;
  /**
   * Whether to load the test application in debug mode (#joo.debug).
   *
   * @parameter default-value=false
   */
  @SuppressWarnings({"UnusedDeclaration"})
  protected boolean debugTests;
  /**
   * the project's test resources
   *
   * @parameter expression="${project.testResources}"
   */
  protected List<org.apache.maven.model.Resource> testResources;
  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   *
   * @parameter expression="${jooUnitJettyPortUpperBound}" default-value=10200
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private int jooUnitJettyPortUpperBound;
  /**
   * To avoid port clashes when multiple tests are running at the same
   * time on the same machine, the jetty port is selected randomly within
   * an range of <code>[jooUnitJettyPortLowerBound:jooUnitJettyPortUpperBound]</code>.
   * Every port is tried until a free one is found or all ports in the range
   * are occupied (which results in the build to fail).
   * When using goal <code>jetty-run-tests</code>, this lower bound is
   * always used.
   *
   * @parameter expression="${jooUnitJettyPortLowerBound}" default-value=10100
   */
  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  private int jooUnitJettyPortLowerBound;
  /**
   * The host name to use to reach the locally started Jetty listenes, usually the default, "localhost".
   *
   * @parameter expression="${jooUnitJettyHost}" default-value="localhost"
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private String jooUnitJettyHost;

  protected String getJettyUrl(Server server) {
    return "http://" + jooUnitJettyHost + ":" + server.getConnectors()[0].getPort();
  }

  protected boolean isTestAvailable() {
    for (org.apache.maven.model.Resource r : testResources) {
      File testFile = new File(r.getDirectory(), testsHtml);
      if (testFile.exists()) {
        return true;
      }
    }
    getLog().info("The tests.html file '" + testsHtml + "' could not be found. Skipping.");
    return false;
  }

  protected Server jettyRunTest(boolean tryPortRange) throws MojoExecutionException {
    JettyWebAppContext handler;
    try {
      handler = new JettyWebAppContext();
      handler.setWebInfLib(findJars());
      handler.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
      List<Resource> baseResources = new ArrayList<Resource>();
      baseResources.add(toResource(new File(outputDirectory, "META-INF/resources")));
      baseResources.add(toResource(testOutputDirectory));
      for (org.apache.maven.model.Resource r : testResources) {
        File testResourceDirectory = new File(r.getDirectory());
        if (testResourceDirectory.exists()) {
           baseResources.add(toResource(testResourceDirectory));
        }
      }
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

  protected List<File> findJars() throws DependencyResolutionRequiredException {
    List<File> jars = new ArrayList<File>();
    for (Object jarUrl : project.getTestClasspathElements()) {
      File file = new File((String)jarUrl);
      if (file.isFile()) { // should be a jar--don't add folders!
        jars.add(file);
        getLog().info("Test classpath: " + jarUrl);
      } else {
        getLog().info("Ignoring test classpath: " + jarUrl);
      }
    }
    return jars;
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
    List<Integer> ports = new ArrayList<Integer>(jooUnitJettyPortUpperBound - jooUnitJettyPortLowerBound + 1);
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
    StringBuilder builder = new StringBuilder(getJettyUrl(server))
            .append("/").append(testsHtml.replace(File.separatorChar, '/'));
    if (debugTests) {
      builder.append("#joo.debug");
    }
    return builder.toString();
  }

  static {
    Resource.setDefaultUseCaches(false);
  }
}
