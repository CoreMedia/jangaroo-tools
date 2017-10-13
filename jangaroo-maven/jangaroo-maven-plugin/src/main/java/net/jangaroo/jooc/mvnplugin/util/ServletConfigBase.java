package net.jangaroo.jooc.mvnplugin.util;

public class ServletConfigBase {
  private String pathSpec;

  public ServletConfigBase() {
  }

  public ServletConfigBase(String pathSpec) {
    this.pathSpec = pathSpec;
  }

  /**
   * Returns the pattern that determines which requests are handled by the servlet.
   */
  public String getPathSpec() {
    return pathSpec;
  }

  /**
   * Sets the pattern that determines which requests are handled by the servlet.
   */
  public void setPathSpec(String pathSpec) {
    this.pathSpec = pathSpec;
  }
}
