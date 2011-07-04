package net.jangaroo.exml.model;

import net.jangaroo.exml.ExmlParseException;
import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.generator.ExmlConfigClassGenerator;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.config.FileLocations;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
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

  private FileLocations locations;
  private FileInputSource sourcePathInputSource;
  private String configClassPackage;

  private JangarooParser jangarooParser;

  private static final String AS_SUFFIX = ".as";
  private static final String EXML_SUFFIX = ".exml";

  public ConfigClassRegistry(FileLocations locations, FileInputSource sourcePathInputSource, InputSource classPathInputSource, String configClassPackage) {
    this.locations = locations;
    this.sourcePathInputSource = sourcePathInputSource;
    this.configClassPackage = configClassPackage;

    ParserOptions parserOptions = new ParserOptions() {
      @Override
      public SemicolonInsertionMode getSemicolonInsertionMode() {
        return SemicolonInsertionMode.QUIRKS;
      }

      @Override
      public boolean isVerbose() {
        return false;
      }
    };
    jangarooParser = new JangarooParser(parserOptions, new StdOutCompileLog());
    jangarooParser.setUp(sourcePathInputSource, classPathInputSource);
  }

  public FileLocations getLocations() {
    return locations;
  }

  public String getConfigClassPackage() {
    return configClassPackage;
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
        ConfigClass configClass = null;
        try {
          configClass = new ExmlConfigClassGenerator(locations, configClassPackage).generateConfigClass(exmlFile);
          addConfigClassByName(configClass.getFullName(), configClass);
        } catch (IOException e) {
          // TODO Log and continue
          throw new IllegalStateException(e);
        }
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
      FileInputSource outputDirInputSource = new FileInputSource(locations.getOutputDirectory(), locations.getOutputDirectory());
      InputSource generatedConfigAsFile = outputDirInputSource.getChild(getInputSourceFileName(name, outputDirInputSource, AS_SUFFIX));
      if (generatedConfigAsFile != null) {
        // A candidate AS config class has already been generated.
        CompilationUnit compilationUnit = Jooc.doParse(generatedConfigAsFile, new StdOutCompileLog(), SemicolonInsertionMode.QUIRKS);
        ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationUnit);
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
            ConfigClass configClass = null;
            try {
              configClass = new ExmlConfigClassGenerator(locations, configClassPackage).generateConfigClass(exmlInputSource.getFile());
            } catch (IOException e) {
              // TODO log
              throw new IllegalStateException(e);
            }
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
    CompilationUnit compilationsUnit = jangarooParser.getCompilationsUnit(name);
    ConfigClass configClass = null;
    if (compilationsUnit != null) {
      configClass = buildConfigClass(compilationsUnit);
    }
    if (configClass == null) {
      throw new ExmlParseException("No config class '" + name + "' found.");
    }
    return configClass;
  }

  private ConfigClass buildConfigClass(CompilationUnit compilationsUnit) {
    ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationsUnit);
    return configClassBuilder.buildConfigClass();
  }

  private String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return qname.replace('.', is.getFileSeparatorChar()) + extension;
  }
}
