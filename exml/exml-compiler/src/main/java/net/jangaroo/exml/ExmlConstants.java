package net.jangaroo.exml;

import java.io.File;

public final class ExmlConstants {
  public static final String EXML_NAMESPACE_URI = "http://www.jangaroo.net/exml/0.8";

  public static final String EXML_CONFIG_URI_PREFIX = "exml:";

  public static final String EXML_COMPONENT_NODE_NAME = "component";

  public static final String EXML_OBJECT_NODE_NAME = "object";
  
  public static final String EXML_IMPORT_NODE_NAME = "import";
  public static final String EXML_IMPORT_CLASS_ATTRIBUTE = "class";

  public static final String EXML_DESCRIPTION_NODE_NAME = "description";

  public static final String EXML_CFG_NODE_NAME = "cfg";
  public static final String EXML_CFG_NAME_ATTRIBUTE = "name";
  public static final String EXML_CFG_TYPE_ATTRIBUTE = "type";
  public static final String EXML_SUFFIX = ".exml";

  public static String parsePackageFromNamespace(String uri) {
    return uri.startsWith(EXML_CONFIG_URI_PREFIX) ? uri.substring(EXML_CONFIG_URI_PREFIX.length()) : null;
  }

  public static boolean isExmlNamespace(String uri) {
    return EXML_NAMESPACE_URI.equals(uri);
  }

}
