package net.jangaroo.exml.model;

import net.jangaroo.exml.ExmlParseException;
import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.parser.ExmlConfigToActionScriptParser;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class ConfigClassRegistry {
  private Map<String, ConfigClass> configClassesByName = new HashMap<String, ConfigClass>();
  private Set<File> scannedExmlFiles = new HashSet<File>();

  private File outputRootDir;
  private FileInputSource sourcePathInputSource;
  private InputSource classPathInputSource;
  private String configClassPackage;

  private static final String AS_SUFFIX = ".as";
  private static final String EXML_SUFFIX = ".exml";

  public ConfigClassRegistry(FileInputSource sourcePathInputSource, InputSource classPathInputSource, String configClassPackage, File outputRootDir) {
    this.sourcePathInputSource = sourcePathInputSource;
    this.classPathInputSource = classPathInputSource;
    this.configClassPackage = configClassPackage;
    this.outputRootDir = outputRootDir;
  }

  /**
   * setup the configClass registry by scanning for .exml files, parsing and creating them
   * This has to be called before you use the registry once.
   */
  public void scanAllExmlFiles() {
    File sourceRootDir = new File(sourcePathInputSource.getPath());

    Collection<File> files = FileUtils.listFiles(sourceRootDir, new SuffixFileFilter(EXML_SUFFIX), TrueFileFilter.INSTANCE);
    for (File exmlFile : files) {
      if (!scannedExmlFiles.contains(exmlFile)) {
        scannedExmlFiles.add(exmlFile);
        ConfigClass configClass = ExmlConfigToActionScriptParser.generateConfigClass(exmlFile, sourceRootDir, outputRootDir, configClassPackage);
        addConfigClassByName(configClass.getFullName(), configClass);
      }
    }
  }

  public ConfigClass getConfigClassByName(String name) {
    ConfigClass configClass = configClassesByName.get(name);
    if (configClass != null) {
      return configClass;
    }
    // The config class has not been registered so far.
    tryGenerateFromExml(name);
    configClass = configClassesByName.get(name);
    if (configClass != null) {
      return configClass;
    }
    // The given name does not denote a config class of an EXML component in the source tree.
    configClass = findActionScriptConfigClass(name);
    addConfigClassByName(name, configClass);
    return configClass;
  }

  private void addConfigClassByName(String name, ConfigClass configClass) {
    ConfigClass existingConfigClass = configClassesByName.get(name);
    if (existingConfigClass != null) {
      if (!existingConfigClass.equals(configClass)) {
        // todo: Keep track of source.
        throw new ExmlParseException("config class " + name + " declared in " + configClass.getComponentName() + " and " + existingConfigClass.getComponentName());
      }
    } else {
      configClassesByName.put(name, configClass);
    }
  }

  private void tryGenerateFromExml(String name) {
    if (name.startsWith(configClassPackage + ".")) {
      // The config class might originate from one of of this module's EXML files.
      FileInputSource outputDirInputSource = new FileInputSource(outputRootDir, outputRootDir);
      InputSource generatedConfigAsFile = outputDirInputSource.getChild(getInputSourceFileName(name, outputDirInputSource, AS_SUFFIX));
      if (generatedConfigAsFile != null) {
        // A candidate AS config class has already been generated.
        ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(generatedConfigAsFile);
        ConfigClass generatedAsConfigClass = configClassBuilder.buildConfigClass();
        if (generatedAsConfigClass != null) {
          // It is really a generated config class.
          // We can determine the name of the EXML component class
          // that was last used to create this config file.
          String componentName = generatedAsConfigClass.getComponentName();
          // We must parse the EXMl file again, because the parent class (and hence the
          // parent config class) might have changed.
          FileInputSource exmlInputSource = sourcePathInputSource.getChild(getInputSourceFileName(componentName, sourcePathInputSource, EXML_SUFFIX));
          if (exmlInputSource != null) {
            scannedExmlFiles.add(exmlInputSource.getFile());
            ConfigClass configClass = ExmlConfigToActionScriptParser.generateConfigClass(exmlInputSource.getFile(), sourcePathInputSource.getSourceDir(), outputRootDir, configClassPackage);
            addConfigClassByName(name, configClass);
            return;
          }
        }
        // The AS file should not exist. However, we do not consider this class
        // to be responsible to deleting outdated config files.
      }
      // The EXML was not found. Scan all EXML files to be sure the right one will be found.
      scanAllExmlFiles();
    }
  }

  private ConfigClass findActionScriptConfigClass(String name) {
    InputSource inputSource = findActionScriptSource(name);
    ConfigClass configClass = null;
    if (inputSource != null) {
      ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(inputSource);
      configClass = configClassBuilder.buildConfigClass();
    }
    if (configClass == null) {
      throw new ExmlParseException("No config class '" + name + "' found.");
    }
    return configClass;
  }

   private InputSource findActionScriptSource(final String name) {
    // scan sourcepath
    InputSource result = sourcePathInputSource.getChild(getInputSourceFileName(name, sourcePathInputSource, AS_SUFFIX));
    if (result == null) {
      // scan classpath
      result = classPathInputSource.getChild(getInputSourceFileName(name, classPathInputSource, AS_SUFFIX));
    }
    return result;
  }

  private String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return qname.replace('.', is.getFileSeparatorChar()) + extension;
  }
}
