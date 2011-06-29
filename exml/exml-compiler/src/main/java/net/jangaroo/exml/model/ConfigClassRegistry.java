package net.jangaroo.exml.model;

import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.parser.ExmlConfigToActionScriptParser;
import net.jangaroo.jooc.input.InputSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class ConfigClassRegistry {
  private Map<String, ConfigClass> configClassesByName = new HashMap<String, ConfigClass>();

  private File outputRootDir;
  private InputSource sourcePathInputSource;
  private InputSource classPathInputSource;
  private String configClassPackage;

  private static final String AS_SUFFIX = ".as";
  private static final String EXML_SUFFIX = ".exml";

  public ConfigClassRegistry(InputSource sourcePathInputSource, InputSource classPathInputSource, String configClassPackage, File outputRootDir) {
    this.sourcePathInputSource = sourcePathInputSource;
    this.classPathInputSource = classPathInputSource;
    this.configClassPackage = configClassPackage;
    this.outputRootDir = outputRootDir;
  }

  /**
   * setup the configClass registry by scanning for .exml files, parsing and creating them
   * This has to be called before you use the registry once.
   */
  public void setupConfigClassRegistry() {
    File sourceRootDir = new File(sourcePathInputSource.getPath());

    Collection<File> files = FileUtils.listFiles(sourceRootDir, new SuffixFileFilter(EXML_SUFFIX), TrueFileFilter.INSTANCE);
    for (File exmlfile : files) {
      ConfigClass configClass = ExmlConfigToActionScriptParser.generateConfigClass(exmlfile, sourceRootDir, outputRootDir, configClassPackage);
      configClassesByName.put(configClass.getFullName(), configClass);
    }
  }

  public ConfigClass getConfigClassByName(String name) {
    ConfigClass configClass = configClassesByName.get(name);
    if(configClass == null) {
      configClass = findConfigClass(name);
      configClassesByName.put(name, configClass);
    }
    return configClass;
  }

  private ConfigClass findConfigClass(String name) {
    InputSource inputSource = findSource(name);
    ConfigClass configClass = null;
    if(inputSource != null ) {
      if(inputSource.getName().endsWith(AS_SUFFIX)) {
        ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(inputSource);
        configClass = configClassBuilder.buildConfigClass();
      }
    }
    if(configClass == null) {
      throw new RuntimeException("No config class '" + name + "' found.");
    }
    return configClass;
  }

   private InputSource findSource(final String qname) {
    // scan sourcepath
    InputSource result = sourcePathInputSource.getChild(getInputSourceFileName(qname, sourcePathInputSource, AS_SUFFIX));
    if (result == null) {
      // scan classpath
      result = classPathInputSource.getChild(getInputSourceFileName(qname, classPathInputSource, AS_SUFFIX));
    }
    return result;
  }

  private String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return qname.replace('.', is.getFileSeparatorChar()) + extension;
  }
}
