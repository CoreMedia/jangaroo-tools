package net.jangaroo.jooc.mvnplugin.util;

/**
 * A configuration for serving static resources.
 */
public final class StaticResourcesServletConfig extends ServletConfigBase {

  private String relativeResourceBase;

  /**
   * Creates a configuration for serving static resources.
   */
  public StaticResourcesServletConfig() {
  }

  /**
   * Creates a configuration for serving static resources.
   *
   * @param relativeResourceBase the relative path to the static resources
   * @param pathSpec             the pattern where the static resources should be served
   */
  public StaticResourcesServletConfig(String relativeResourceBase, String pathSpec) {
    super(pathSpec);
    this.relativeResourceBase = relativeResourceBase;
  }

  /**
   * Returns the relative path to the static resources.
   */
  public String getRelativeResourceBase() {
    return relativeResourceBase;
  }

  /**
   * Sets the relative path to the static resources.
   */
  public void setRelativeResourceBase(String relativeResourceBase) {
    this.relativeResourceBase = relativeResourceBase;
  }

}
