package net.jangaroo.exml;

public final class ExmlConstants {
  public static final String EXML_NAMESPACE_URI = "http://net.jangaroo.com/extxml/0.1";

  public static final String EXML_CONFIG_URI_PREFIX = "exml:";

  public static final String EXML_COMPONENT_NODE_NAME = "component";

  public static final String EXML_OBJECT_NODE_NAME = "object";
  
  public static final String EXML_IMPORT_NODE_NAME = "import";
  public static final String EXML_IMPORT_CLASS_ATTRIBUTE = "class";

  public static final String EXML_DESCRIPTION_NODE_NAME = "description";

  public static final String EXML_CFG_NODE_NAME = "cfg";
  public static final String EXML_CFG_NAME_ATTRIBUTE = "name";
  public static final String EXML_CFG_TYPE_ATTRIBUTE = "type";

  public static String parsePackageFromNamespace(String uri) {
    if (!uri.startsWith(ExmlConstants.EXML_CONFIG_URI_PREFIX)) {
      return null;
    }
    return uri.substring(ExmlConstants.EXML_CONFIG_URI_PREFIX.length());
  }

  public static boolean isExmlNamespace(String uri) {
    return EXML_NAMESPACE_URI.equals(uri);
  }
}
