package net.jangaroo.jooc.mvnplugin.proxy;

import net.jangaroo.jooc.mvnplugin.util.JettyWrapper;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.maven.plugin.JettyWebAppContext;
import org.eclipse.jetty.servlet.ServletHolder;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

import java.io.File;

public class ProxyJettyWrapper extends JettyWrapper {

  private final String targetUri;
  private final String pathSpec;

  private String forwardedHeader = "true";
  private String loggingEnabled = "false";

  public ProxyJettyWrapper(Log log, String targetUri, String pathSpec) {
    super(log);
    this.targetUri = targetUri;
    this.pathSpec = pathSpec;
  }

  public void setForwardedHeader(String forwardedHeader) {
    this.forwardedHeader = forwardedHeader;
  }

  public void setLoggingEnabled(String loggingEnabled) {
    this.loggingEnabled = loggingEnabled;
  }

  @Override
  protected JettyWebAppContext createHandler(File baseDir) throws JettyHelperException {
    JettyWebAppContext handler = super.createHandler(baseDir);

    ServletHolder servletHolder = new ServletHolder("proxy", JangarooProxyServlet.class);

    servletHolder.setInitParameter("targetUri", targetUri.replaceAll("/$", ""));
    servletHolder.setInitParameter(ProxyServlet.P_FORWARDEDFOR, forwardedHeader);
    servletHolder.setInitParameter(ProxyServlet.P_LOG, loggingEnabled);
    servletHolder.setInitParameter(ProxyServlet.P_PRESERVECOOKIES, "true");

    handler.addServlet(servletHolder, pathSpec);
    getLog().info("Add JangarooProxyServlet");

    return handler;
  }
}
