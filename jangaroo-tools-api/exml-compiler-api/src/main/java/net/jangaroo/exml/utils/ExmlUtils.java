package net.jangaroo.exml.utils;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

  /**
   * @deprecated 
   */
  @SuppressWarnings("UnusedDeclaration")
  public static boolean isCodeExpression(String attributeValue) {
    return CompilerUtils.isCodeExpression(attributeValue);
  }

  /**
   * @deprecated 
   */
  @SuppressWarnings("UnusedDeclaration")
  public static String getCodeExpression(String attributeValue) {
    return CompilerUtils.getCodeExpression(attributeValue);
  }

  public static void addImport(Set<String> imports, String importedClassName) {
    if (importedClassName != null && importedClassName.contains(".")) { // do not import top-level classes!
      imports.add(importedClassName);
    }
  }

  /**
   * Create a ComponentClass name from the given name. By convention all ComponentClass names are capitalized.
   *
   * @param name the name
   * @return return the new config-class name, matching the conventions.
   */
  public static String createComponentClassName(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    return name.substring(0,1).toUpperCase() + name.substring(1);
  }

  public static Set<ZipEntry> findXsdJarEntries(ZipFile jarFile) throws IOException {
    // find all *.xsd in jar's root folder:
    Enumeration<? extends ZipEntry> enumeration = jarFile.entries();
    Set<ZipEntry> result = new LinkedHashSet<ZipEntry>();
    while (enumeration.hasMoreElements()) {
      ZipEntry zipEntry = enumeration.nextElement();
      if (!zipEntry.isDirectory() && zipEntry.getName().indexOf('/') == -1 && zipEntry.getName().endsWith(".xsd")) {
        result.add(zipEntry);
      }
    }
    return result;
  }
}
