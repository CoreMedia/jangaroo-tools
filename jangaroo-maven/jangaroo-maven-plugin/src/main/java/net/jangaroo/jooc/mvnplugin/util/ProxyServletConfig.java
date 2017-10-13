package net.jangaroo.jooc.mvnplugin.util;

/**
 * A configuration for a proxy servlet.
 */
public final class ProxyServletConfig extends ServletConfigBase {

  private String targetUri;
  private boolean loggingEnabled = false;
  private boolean forwardedHeaderEnabled = true;

  /**
   * Creates a configuration for a proxy servlet.
   */
  public ProxyServletConfig() {
  }

  /**
   * Creates a configuration for a proxy servlet.
   *
   * @param targetUri the url to which all proxied requests are forwarded to
   * @param pathSpec  the pattern that determines which requests should be proxied
   */
  public ProxyServletConfig(String targetUri, String pathSpec) {
    super(pathSpec);
    this.targetUri = targetUri;
  }

  /**
   * Returns the url to which all proxied requests are forwarded to.
   */
  public String getTargetUri() {
    return targetUri;
  }

  /**
   * Sets the url to which all proxied requests are forwarded to.
   */
  public void setTargetUri(String targetUri) {
    this.targetUri = targetUri;
  }

  /**
   * Returns true if logging is enabled.
   */
  public boolean isLoggingEnabled() {
    return loggingEnabled;
  }

  /**
   * Enable / disable logging. Defaults to {@code false}.
   */
  public void setLoggingEnabled(boolean loggingEnabled) {
    this.loggingEnabled = loggingEnabled;
  }

  /**
   * Returns true if X-Forwarded headers are enabled.
   */
  public boolean isForwardedHeaderEnabled() {
    return forwardedHeaderEnabled;
  }

  /**
   * Enable X-Forwarded headers. Defaults to {@code true}.
   */
  public void setForwardedHeaderEnabled(boolean forwardedHeaderEnabled) {
    this.forwardedHeaderEnabled = forwardedHeaderEnabled;
  }
}
