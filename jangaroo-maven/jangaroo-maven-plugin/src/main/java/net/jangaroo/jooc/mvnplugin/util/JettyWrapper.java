package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.lang3.Range;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.maven.plugin.JettyWebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.eclipse.jetty.util.resource.Resource.newResource;

public class JettyWrapper {

  private static final long WAIT_TIME_MILLIS = 1000L;

  private final Log log;

  private Server server;

  static {
    Resource.setDefaultUseCaches(false);
  }

  public JettyWrapper(Log log) {
    this.log = log;
  }

  public void start(String host, int port, File baseDir) throws JettyHelperException {
    start(host, Range.is(port), baseDir);
  }

  public void start(String host, Range<Integer> portRange, File baseDir) throws JettyHelperException {
    if (server != null) {
      stop();
    }

    Exception lastException = null;
    Handler handler = createHandler(baseDir);

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
      throw new JettyHelperException("Could not start server", lastException);
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

  public void waitUntilStarted(int timeoutMillis) throws JettyHelperException {
    if (server == null) {
      throw new JettyHelperException("Server not started");
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
      throw new JettyHelperException(String.format("Server did not start within %d ms", timeoutMillis));
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

  protected JettyWebAppContext createHandler(File baseDir) throws JettyHelperException {
    try {
      JettyWebAppContext handler = new JettyWebAppContext();

      handler.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");

      List<Resource> baseResources = new ArrayList<>();
      baseResources.add(newResource(baseDir));
      handler.setBaseResource(new ResourceCollection(baseResources.toArray(new Resource[baseResources.size()])));
      getLog().info("Using base resources " + baseResources);

      ServletHolder servletHolder = new ServletHolder("default", DefaultServlet.class);
      servletHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");
      handler.addServlet(servletHolder, "/");
      getLog().info("Set servlet cache control to 'do not cache'.");

      return handler;
    } catch (Exception e) {
      throw new JettyHelperException(e);
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

  public static final class JettyHelperException extends Exception {
    JettyHelperException(String message) {
      super(message);
    }

    JettyHelperException(String message, Throwable cause) {
      super(message, cause);
    }

    JettyHelperException(Throwable cause) {
      super(cause);
    }
  }
}
