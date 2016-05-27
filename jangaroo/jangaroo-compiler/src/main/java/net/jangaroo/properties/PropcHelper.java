package net.jangaroo.properties;

import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.util.Locale;

/**
 * A configuration object for the properties compiler.
 */
public class PropcHelper {

  public static final String PROPERTIES_CLASS_SUFFIX = "_properties";
  public static final String DEFAULT_LOCALE = "en";

  public static Locale computeLocale(String propertiesClassName) {
    String[] parts = getBundleName(propertiesClassName).split("_", 4);
    switch (parts.length) {
      case 4: return new Locale(parts[1], parts[2], parts[3]);
      case 3: return new Locale(parts[1], parts[2]);
      case 2: return new Locale(parts[1]);
    }
    return null;
  }

  public static String computeBaseClassName(String propertiesClassName) {
    String bundleName = getBundleName(propertiesClassName);
    int underscorePos = bundleName.indexOf('_');
    if (underscorePos != -1) {
      return propertiesClassName.substring(0, underscorePos) + PROPERTIES_CLASS_SUFFIX;
    }
    return propertiesClassName;
  }

  public static String getBundleName(String propertiesClassName) {
    return propertiesClassName.substring(0, propertiesClassName.length() - PROPERTIES_CLASS_SUFFIX.length());
  }

  public static File computeGeneratedPropertiesAS3File(File apiOutputDirectory, String className) {
    String generatedPropertiesClassFileName = CompilerUtils.fileNameFromQName(className, '/', ".as");
    return new File(apiOutputDirectory, generatedPropertiesClassFileName);
  }

  public static File computeGeneratedPropertiesJsFile(File outputDirectory, String className, Locale locale) {
    String localeSubDir = locale == null ? DEFAULT_LOCALE : locale.toString();
    String generatedPropertiesClassFileName = localeSubDir + '/' + CompilerUtils.fileNameFromQName(className, '/', ".js");
    return new File(outputDirectory, generatedPropertiesClassFileName);
  }

}
