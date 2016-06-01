package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.properties.PropcHelper;
import net.jangaroo.utils.CompilerUtils;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;

import java.io.File;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * A Maven source mapping from properties sources to their generated JavaScript.
 */
public class PropertiesSourceMapping implements SourceMapping {
  private static PropertiesSourceMapping ourInstance = new PropertiesSourceMapping();

  public static PropertiesSourceMapping getInstance() {
    return ourInstance;
  }

  private PropertiesSourceMapping() {
  }

  @Override
  public Set<File> getTargetFiles(File targetDir, String source) {
    String className = CompilerUtils.qNameFromRelativePath(source);
    String baseClassName = PropcHelper.computeBaseClassName(className);
    Locale locale = PropcHelper.computeLocale(className);
    File targetFile = PropcHelper.computeGeneratedPropertiesJsFile(targetDir, baseClassName, locale);
    return Collections.singleton(targetFile);
  }
}
