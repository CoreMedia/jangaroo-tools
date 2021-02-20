package net.jangaroo.properties;

import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.util.Locale;

/**
 * A configuration object for the properties compiler.
 */
public class PropcHelper {

  public static final String DEFAULT_LOCALE = "en";

  public static Locale computeLocale(String propertiesClassName) {
    String[] parts = getLocalBundleName(propertiesClassName).split("_", 4);
    switch (parts.length) {
      case 4: return new Locale(parts[1], parts[2], parts[3]);
      case 3: return new Locale(parts[1], parts[2]);
      case 2: return new Locale(parts[1]);
    }
    return null;
  }

  public static String computeBaseClassName(String propertiesClassName) {
    String localBundleName = getLocalBundleName(propertiesClassName);
    int underscorePos = localBundleName.indexOf('_');
    if (underscorePos != -1) {
      return CompilerUtils.qName(CompilerUtils.packageName(propertiesClassName),
              localBundleName.substring(0, underscorePos) + CompilerUtils.PROPERTIES_CLASS_SUFFIX);
    }
    return propertiesClassName;
  }

  public static String getBundleName(String propertiesClassName) {
    return propertiesClassName.substring(0, propertiesClassName.length() - CompilerUtils.PROPERTIES_CLASS_SUFFIX.length());
  }

  private static String getLocalBundleName(String propertiesClassName) {
    return CompilerUtils.className(getBundleName(propertiesClassName));
  }

  public static File computeGeneratedPropertiesAS3File(File apiOutputDirectory, String className, Locale locale) {
    String generatedPropertiesClassFileName = CompilerUtils.fileNameFromQName(insertNonDefaultLocale(className, locale), '/', ".as");
    return new File(apiOutputDirectory, generatedPropertiesClassFileName);
  }

  public static File computeGeneratedPropertiesJsFile(File outputDirectory, String className, Locale locale) {
    String subDirWithTrailingSlash = localeOrDefaultLocaleString(locale) + '/';
    String generatedPropertiesClassFileName = subDirWithTrailingSlash + CompilerUtils.fileNameFromQName(className, '/', ".js");
    return new File(outputDirectory, generatedPropertiesClassFileName);
  }

  public static String localeOrDefaultLocaleString(Locale locale) {
    return locale == null ? DEFAULT_LOCALE : locale.toString();
  }

  public static String insertNonDefaultLocale(String className, Locale locale) {
    return locale == null || DEFAULT_LOCALE.equals(locale.toString())
            ? className
            : className.substring(0, className.length() - CompilerUtils.PROPERTIES_CLASS_SUFFIX.length()) + "_" + locale.toString() + CompilerUtils.PROPERTIES_CLASS_SUFFIX;
  }

}
