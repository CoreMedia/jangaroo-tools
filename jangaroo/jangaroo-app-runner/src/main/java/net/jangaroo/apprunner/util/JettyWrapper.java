package net.jangaroo.apprunner.util;

import net.jangaroo.apprunner.proxy.JangarooProxyServlet;
import org.apache.commons.lang3.Range;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.slf4j.Logger;

import javax.servlet.Servlet;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.addAll;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Wrapper class for controlling a Jetty server.
 */
public class JettyWrapper {

  private static final long WAIT_TIME_MILLIS = 1000L;
  /**
   * For the special port of zero, retry this given number of times
   * to get a new random port, in case other services already blocked
   * that port meanwhile.
   */
  private static final int RANDOM_PORT_RETRY_LIMIT = 20;
  private static final Integer RANDOM_PORT_IDENTIFIER = 0;

  public static final String ROOT_PATH_SPEC = "/*";
  private static final StaticResourcesServletConfig DEFAULT_RESOURCES_SERVLET_CONFIG =
          new StaticResourcesServletConfig(ROOT_PATH_SPEC, "/");

  static {
    Resource.setDefaultUseCaches(false);
  }

  private static final Logger LOG = getLogger(lookup().lookupClass());

  private List<Path> baseDirs = new ArrayList<>();
  private List<StaticResourcesServletConfig> staticResourcesServletConfigs;
  private List<ProxyServletConfig> proxyServletConfigs;
  private Map<String, Servlet> additionalServlets;

  private Server server;
  private List<File> resourceJars = new ArrayList<>();

  private boolean hasRootPathServlet = false;

  private Class<? extends WebAppContext> webAppContextClass = WebAppContext.class;

  /**
   * Creates a Wrapper for controlling a Jetty server.
   *
   * @param baseDirs the base directories for serving static resources
   */
  public JettyWrapper(Path... baseDirs) {
    addBaseDirs(baseDirs);
  }

  public void addBaseDir(Path baseDir) {
    baseDirs.add(baseDir);
  }

  @SuppressWarnings("WeakerAccess")
  public void addBaseDirs(Path... baseDirs) {
    addAll(this.baseDirs, baseDirs);
  }

  public void setWebAppContextClass(Class<? extends WebAppContext> webAppContextClass) {
    this.webAppContextClass = webAppContextClass;
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
        tryServerStart(host, port, handler);
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

  /**
   * Tries to start the server.
   *
   * @param host    host for server
   * @param port    port for server; zero will use a random port (and retry several times upon failure)
   * @param handler server handler
   * @throws Exception exception on failure; on retries, the last exception
   */
  private void tryServerStart(String host, Integer port, Handler handler) throws Exception {
    int retryLimit = RANDOM_PORT_IDENTIFIER.equals(port) ? RANDOM_PORT_RETRY_LIMIT : 1;
    Exception lastException = null;

    for (int i = 0; i < retryLimit; i++) {
      try {
        server = null;
        server = createServer(host, port, handler);
        server.start();
        break;
      } catch (Exception e) {
        lastException = e;
      }
    }

    if (lastException != null) {
      throw lastException;
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

  private WebAppContext createHandler() throws JettyWrapperException {
    try {
      WebAppContext handler = webAppContextClass.newInstance();

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

  private void addDefaultServlet(ServletContextHandler webAppContext, StaticResourcesServletConfig config) {
    ServletHolder servletHolder = new ServletHolder(DefaultServlet.class);

    servletHolder.setInitParameter("relativeResourceBase", config.getRelativeResourceBase());
    servletHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");

    webAppContext.addAliasCheck(new AllowSymLinkAliasChecker());
    addServlet(webAppContext, servletHolder, config.getPathSpec());
    getLog().info(String.format("Serving static resources: %s -> %s",
            config.getPathSpec(), config.getRelativeResourceBase()));
  }

  private void addProxyServlet(ServletContextHandler webAppContext, ProxyServletConfig config) {
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

  private void addServlet(ServletContextHandler webAppContext, ServletHolder servletHolder, String pathSpec) {
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

  protected Logger getLog() {
    return LOG;
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
