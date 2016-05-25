package net.jangaroo.properties.api;

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
    String[] parts = propertiesClassName.split("_", 4);
    switch (parts.length) {
      case 4: return new Locale(parts[1], parts[2], parts[3]);
      case 3: return new Locale(parts[1], parts[2]);
      case 2: return new Locale(parts[1]);
    }
    return null;
  }

  public static String computeBaseClassName(String propertiesClassName) {
    int underscorePos = propertiesClassName.indexOf('_');
    if (underscorePos != -1) {
      return propertiesClassName.substring(0, underscorePos);
    }
    return propertiesClassName;
  }

  public static File computeGeneratedPropertiesAS3File(PropertiesCompilerConfiguration config, String className) {
    String generatedPropertiesClassFileName = CompilerUtils.fileNameFromQName(className, '/', PROPERTIES_CLASS_SUFFIX + ".as");
    return new File(config.getApiOutputDirectory(), generatedPropertiesClassFileName);
  }

  public static File computeGeneratedPropertiesJsFile(File outputDirectory, String className, Locale locale) {
    String localeSubDir = locale == null ? DEFAULT_LOCALE : locale.toString();
    String generatedPropertiesClassFileName = localeSubDir + '/' + CompilerUtils.fileNameFromQName(className, '/', PROPERTIES_CLASS_SUFFIX + ".js");
    return new File(outputDirectory, generatedPropertiesClassFileName);
  }

}
