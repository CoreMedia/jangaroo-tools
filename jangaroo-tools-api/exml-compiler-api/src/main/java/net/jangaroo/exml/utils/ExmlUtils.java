package net.jangaroo.exml.utils;

import net.jangaroo.exml.api.Exmlc;

/**
 * Some useful utility functions for EXML handling.
 */
public class ExmlUtils {

  public static String parsePackageFromNamespace(String uri) {
    return uri.startsWith(Exmlc.EXML_CONFIG_URI_PREFIX) ? uri.substring(Exmlc.EXML_CONFIG_URI_PREFIX.length()) : null;
  }

  public static boolean isExmlNamespace(String uri) {
    return Exmlc.EXML_NAMESPACE_URI.equals(uri);
  }

  public static boolean isCodeExpression(String attributeValue) {
    return attributeValue.startsWith("{") && attributeValue.endsWith("}");
  }

  public static String getCodeExpression(String attributeValue) {
    return attributeValue.substring(1, attributeValue.length() - 1);
  }
}
