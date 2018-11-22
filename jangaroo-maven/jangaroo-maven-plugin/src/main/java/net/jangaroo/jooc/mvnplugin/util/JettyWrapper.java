package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.mvnplugin.proxy.JangarooProxyServlet;
import org.apache.commons.lang3.Range;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.maven.plugin.JettyWebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AllowSymLinkAliasChecker;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

import javax.servlet.Servlet;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper class for controlling a Jetty server.
 */
public class JettyWrapper {

  private static final long WAIT_TIME_MILLIS = 1000L;

  public static final String ROOT_PATH_SPEC = "/*";
  private static final StaticResourcesServletConfig DEFAULT_RESOURCES_SERVLET_CONFIG =
          new StaticResourcesServletConfig(ROOT_PATH_SPEC, "/");

  static {
    Resource.setDefaultUseCaches(false);
  }

  private final Log log;

  private List<File> baseDirs = new ArrayList<>();
  private List<StaticResourcesServletConfig> staticResourcesServletConfigs;
  private List<ProxyServletConfig> proxyServletConfigs;
  private Map<String, Servlet> additionalServlets;

  private Server server;
  private List<File> resourceJars = new ArrayList<>();

  private boolean hasRootPathServlet = false;

  /**
   * Creates a Wrapper for controlling a Jetty server.
   *
   * @param log     the log used by this wrapper
   * @param baseDir the base directory for serving static resources
   */
  public JettyWrapper(Log log, File baseDir) {
    this.log = log;
    baseDirs.add(baseDir);
  }

  public void addBaseDir(File baseDir) {
    baseDirs.add(baseDir);
  }

  public void setStaticResourcesServletConfigs(List<StaticResourcesServletConfig> staticResourcesServletConfigs) {
    this.staticResourcesServletConfigs = staticResourcesServletConfigs;
  }

  public void addResourceJar(File resourceJar) {
    resourceJars.add(resourceJar);
  }

  public void setProxyServletConfigs(List<ProxyServletConfig> proxyServletConfigs) {
    this.proxyServletConfigs = proxyServletConfigs;
  }

  public void setAdditionalServlets(Map<String, Servlet> additionalServlets) {
    this.additionalServlets = additionalServlets;
  }

  public void start(String host, int port) throws JettyWrapperException {
    start(host, Range.is(port));
  }

  public void start(String host, Range<Integer> portRange) throws JettyWrapperException {
    if (server != null) {
      stop();
    }

    Exception lastException = null;
    Handler handler = createHandler();

    List<Integer> shuffledPorts = shufflePortRange(portRange);
    for (Integer port : shuffledPorts) {
      try {
        server = null;
        server = createServer(host, port, handler);
        server.start();
        break;
      } catch (Exception e) {
        getLog().debug("Could not start server", e);
        lastException = e;
      }
    }

    if (server == null || !server.isRunning()) {
      throw new JettyWrapperException("Could not start server", lastException);
    }
  }

  public void stop() {
    if (server != null) {
      try {
        server.stop();
      } catch (Exception e) {
        getLog().warn("Could not stop server", e);
      }
    }
  }

  public URI getUri() {
    return server.getURI();
  }

  public void waitUntilStarted(int timeoutMillis) throws JettyWrapperException {
    if (server == null) {
      throw new JettyWrapperException("Server not started");
    }

    getLog().info(String.format("Waiting for Server startup (timeout = %d ms)", timeoutMillis));

    int waitedMillis = 0;

    try {
      while (waitedMillis < timeoutMillis && !server.isStarted()) {
        getLog().debug("Server not ready yet. Waiting ...");
        Thread.sleep(WAIT_TIME_MILLIS);
        waitedMillis += WAIT_TIME_MILLIS;
      }
    } catch (InterruptedException e) {
      getLog().warn("Interrupted while waiting for startup", e);
      Thread.currentThread().interrupt();
    }

    if (!server.isStarted()) {
      throw new JettyWrapperException(String.format("Server did not start within %d ms", timeoutMillis));
    }
  }

  public void blockUntilInterrupted() {
    try {
      server.join();
    } catch (InterruptedException ignore) {
      Thread.currentThread().interrupt();
    }
  }

  private Server createServer(String host, int port, Handler handler) {
    InetSocketAddress address = new InetSocketAddress(host, port);
    getLog().info("Creating Jetty server at " + address);
    Server server = new Server(address);
    server.setHandler(handler);
    return server;
  }

  private JettyWebAppContext createHandler() throws JettyWrapperException {
    try {
      JettyWebAppContext handler = new JettyWebAppContext();

      handler.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");

      List<Resource> baseResources = baseDirs.stream().map(Resource::newResource).collect(Collectors.toList());
      baseResources = new ArrayList<>(baseResources);
      if (resourceJars != null && !resourceJars.isEmpty()) {
        for (File webInfLibJar : resourceJars) {
          baseResources.add(Resource.newResource("jar:"+Resource.toURL(webInfLibJar).toString()+"!/META-INF/resources"));
        }
      }
      handler.setBaseResource(new ResourceCollection(baseResources.toArray(new Resource[0])));
      getLog().info("Using base resources " + baseResources);

      if (staticResourcesServletConfigs != null && !staticResourcesServletConfigs.isEmpty()) {
        for (StaticResourcesServletConfig config : staticResourcesServletConfigs) {
          addDefaultServlet(handler, config);
        }
      }

      if (proxyServletConfigs != null && !proxyServletConfigs.isEmpty()) {
        for (ProxyServletConfig config : proxyServletConfigs) {
          addProxyServlet(handler, config);
        }
      }

      if (additionalServlets != null && !additionalServlets.isEmpty()) {
        for (Map.Entry<String, Servlet> servletEntry : additionalServlets.entrySet()) {
          addServlet(handler, new ServletHolder(servletEntry.getValue()), servletEntry.getKey());
        }
      }

      if (!hasRootPathServlet) {
        addDefaultServlet(handler, DEFAULT_RESOURCES_SERVLET_CONFIG);
      }

      return handler;
    } catch (Exception e) {
      throw new JettyWrapperException(e);
    }
  }

  private void addDefaultServlet(JettyWebAppContext webAppContext, StaticResourcesServletConfig config) {
    ServletHolder servletHolder = new ServletHolder(DefaultServlet.class);

    servletHolder.setInitParameter("relativeResourceBase", config.getRelativeResourceBase());
    servletHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");

    webAppContext.addAliasCheck(new AllowSymLinkAliasChecker());
    addServlet(webAppContext, servletHolder, config.getPathSpec());
    getLog().info(String.format("Serving static resources: %s -> %s",
            config.getPathSpec(), config.getRelativeResourceBase()));
  }

  private void addProxyServlet(JettyWebAppContext webAppContext, ProxyServletConfig config) {
    String pathSpec = config.getPathSpec();
    ServletHolder servletHolder = new ServletHolder(pathSpec, JangarooProxyServlet.class); // TODO: Is this really a good servlet name?

    servletHolder.setInitParameter("targetUri", config.getTargetUri().replaceAll("/$", ""));
    servletHolder.setInitParameter(ProxyServlet.P_FORWARDEDFOR, String.valueOf(config.isForwardedHeaderEnabled()));
    servletHolder.setInitParameter(ProxyServlet.P_LOG, String.valueOf(config.isLoggingEnabled()));
    servletHolder.setInitParameter(ProxyServlet.P_PRESERVECOOKIES, "true");

    addServlet(webAppContext, servletHolder, pathSpec);
    getLog().info(String.format("Proxy requests: %s -> %s",
            pathSpec, config.getTargetUri()));
  }

  private void addServlet(JettyWebAppContext webAppContext, ServletHolder servletHolder, String pathSpec) {
    webAppContext.addServlet(servletHolder, pathSpec);
    if (ROOT_PATH_SPEC.equals(pathSpec)) {
      hasRootPathServlet = true;
    }
  }

  private List<Integer> shufflePortRange(Range<Integer> portRange) {
    List<Integer> shuffledPorts = new ArrayList<>();
    for (int i = portRange.getMinimum(); i <= portRange.getMaximum(); i++) {
      shuffledPorts.add(i);
    }
    Collections.shuffle(shuffledPorts);
    return shuffledPorts;
  }

  protected Log getLog() {
    return log;
  }

  public static final class JettyWrapperException extends Exception {
    JettyWrapperException(String message) {
      super(message);
    }

    JettyWrapperException(String message, Throwable cause) {
      super(message, cause);
    }

    JettyWrapperException(Throwable cause) {
      super(cause);
    }
  }

}
