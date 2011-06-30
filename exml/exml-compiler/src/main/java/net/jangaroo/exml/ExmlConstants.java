package net.jangaroo.exml;

public final class ExmlConstants {
  public static final String EXML_NAMESPACE_URI = "http://net.jangaroo.com/extxml/0.1";
  public static final String EXML_COMPONENT_NODE_NAME = "component";
  public static final String EXML_CONFIG_URI_PREFIX = "exml:";
  public static final String EXML_OBJECT_NODE_NAME = "object";

  public static String parsePackageFromNamespace(String uri) {
    if (!uri.startsWith(ExmlConstants.EXML_CONFIG_URI_PREFIX)) {
      return null;
    }
    return uri.substring(ExmlConstants.EXML_CONFIG_URI_PREFIX.length());
  }
}
