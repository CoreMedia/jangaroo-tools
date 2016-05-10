package net.jangaroo.properties.api;

import net.jangaroo.utils.FileLocations;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * A configuration object for the properties compiler.
 */
public class PropcHelper {

  public static Locale computeLocale(File propertiesFile) {
    String propertiesFileName = CompilerUtils.removeExtension(propertiesFile.getName());
    String[] parts = propertiesFileName.split("_", 4);
    switch (parts.length) {
      case 4: return new Locale(parts[1], parts[2], parts[3]);
      case 3: return new Locale(parts[1], parts[2]);
      case 2: return new Locale(parts[1]);
    }
    return null;
  }

  public static String computeBaseClassName(FileLocations locations, File srcFile) {
    String className;
    try {
      className = CompilerUtils.qNameFromFile(locations.findSourceDir(srcFile), srcFile);
    } catch (IOException e) {
      throw new PropcException(e);
    }
    int underscorePos = className.indexOf('_');
    if (underscorePos != -1) {
      className = className.substring(0, underscorePos);
    }
    return className;
  }

  public static File computeGeneratedPropertiesAS3File(PropertiesCompilerConfiguration config, String className) {
    String generatedPropertiesClassFileName = CompilerUtils.fileNameFromQName(className, '/', "_properties.as");
    return new File(config.getApiOutputDirectory(), generatedPropertiesClassFileName);
  }

  public static File computeGeneratedPropertiesJsFile(PropertiesCompilerConfiguration config, String className, Locale locale) {
    String localeSubDir = locale == null ? config.getDefaultLocale() : locale.toString();
    String generatedPropertiesClassFileName = localeSubDir + '/' + CompilerUtils.fileNameFromQName(className, '/', "_properties.js");
    return new File(config.getOutputDirectory(), generatedPropertiesClassFileName);
  }

}
