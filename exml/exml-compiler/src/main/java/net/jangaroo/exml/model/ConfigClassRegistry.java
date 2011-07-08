package net.jangaroo.exml.model;

import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.ExmlcException;
import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class ConfigClassRegistry {
  private Map<String, ConfigClass> configClassesByName = new HashMap<String, ConfigClass>();
  private Set<File> scannedExmlFiles = new HashSet<File>();

  private ExmlConfiguration config;
  private InputSource sourcePathInputSource;

  private JangarooParser jangarooParser;

  public ConfigClassRegistry(final ExmlConfiguration config) throws IOException {
    this.config = config;

    sourcePathInputSource = PathInputSource.fromFiles(config.getSourcePath(), new String[0]);
    InputSource classPathInputSource = PathInputSource.fromFiles(config.getClassPath(),
      new String[]{"", JangarooParser.JOO_API_IN_JAR_DIRECTORY_PREFIX});

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
    jangarooParser = new JangarooParser(parserOptions, new StdOutCompileLog()) {
      @Override
      protected InputSource findSource(String qname) {
        InputSource inputSource = super.findSource(qname);
        if (inputSource != null && inputSource instanceof FileInputSource && !((FileInputSource)inputSource).getSourceDir().equals(config.getOutputDirectory())) {
          // A regular source file (not a generated file) has been found. Use it.
          return inputSource;
        }
        // Just in case the requested class is a config class
        // that is generated from an EXML file, regenerate the config file before
        // it is too late. This will only affect files in the config package
        // and only generated files, so it is pretty safe.
        tryBuildConfigClassFromExml(qname);
        // Just in case the source was not found on the first attempt, fetch it again.
        return super.findSource(qname);
      }
    };
    List<File> fullSourcePath = new ArrayList<File>(config.getSourcePath());
    fullSourcePath.add(config.getOutputDirectory());
    jangarooParser.setUp(PathInputSource.fromFiles(fullSourcePath, new String[0]), classPathInputSource);
  }

  public ExmlConfiguration getConfig() {
    return config;
  }

  /**
   * Setup the config class registry by scanning for .exml files, parsing them and adding their models to this registry.
   * This has to be called before you use the registry once.
   */
  public void scanAllExmlFiles() {
    ExmlToConfigClassParser exmlToConfigClassParser = new ExmlToConfigClassParser(config);
    for (InputSource source : sourcePathInputSource.list()) {
      File exmlFile = ((FileInputSource) source).getFile();
      if (exmlFile.isFile() && exmlFile.getName().endsWith(ExmlConstants.EXML_SUFFIX)) {
        if (!scannedExmlFiles.contains(exmlFile)) {
          scannedExmlFiles.add(exmlFile);
          try {
            ConfigClass configClass = exmlToConfigClassParser.parseExmlToConfigClass(exmlFile);
            addConfigClass(configClass);
          } catch (IOException e) {
            // TODO Log and continue?
            throw new ExmlcException("could not read EXML file", e);
          }
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
    tryBuildConfigClassFromExml(name);
    configClass = configClassesByName.get(name);
    if (configClass != null) {
      return configClass;
    }
    // The given name does not denote a config class of an EXML component in the source tree.
    configClass = findActionScriptConfigClass(name);
    addConfigClass(configClass);
    return configClass;
  }

  private void addConfigClass(ConfigClass configClass) {
    String name = configClass.getFullName();
    ConfigClass existingConfigClass = configClassesByName.get(name);
    if (existingConfigClass != null) {
      if (!existingConfigClass.equals(configClass)) {
        // todo: Keep track of source.
        throw new ExmlcException("config class " + name + " declared in " + configClass.getComponentClassName() + " and " + existingConfigClass.getComponentClassName());
      }
    } else {
      configClassesByName.put(name, configClass);
    }
  }

  private void tryBuildConfigClassFromExml(String name) {
    if (name.startsWith(config.getConfigClassPackage() + ".")) {
      // The config class might originate from one of of this module's EXML files.
      FileInputSource outputDirInputSource = new FileInputSource(config.getOutputDirectory(), config.getOutputDirectory());
      InputSource generatedConfigAsFile = outputDirInputSource.getChild(JangarooParser.getInputSourceFileName(name, outputDirInputSource, JangarooParser.AS_SUFFIX));
      if (generatedConfigAsFile != null) {
        // A candidate AS config class has already been generated.
        CompilationUnit compilationUnit = Jooc.doParse(generatedConfigAsFile, new StdOutCompileLog(), SemicolonInsertionMode.QUIRKS);
        ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationUnit);
        ConfigClass generatedAsConfigClass = configClassBuilder.buildConfigClass();
        if (generatedAsConfigClass != null) {
          // It is really a generated config class.
          // We can determine the name of the EXML component class
          // that was last used to create this config file.
          String componentName = generatedAsConfigClass.getComponentClassName();
          // We must parse the EXMl file again, because the parent class (and hence the
          // parent config class) might have changed.
          FileInputSource exmlInputSource = (FileInputSource)sourcePathInputSource.getChild(JangarooParser.getInputSourceFileName(componentName, sourcePathInputSource, ExmlConstants.EXML_SUFFIX));
          if (exmlInputSource != null) {
            scannedExmlFiles.add(exmlInputSource.getFile());
            ConfigClass configClass;
            try {
              configClass = new ExmlToConfigClassParser(config).parseExmlToConfigClass(exmlInputSource.getFile());
            } catch (IOException e) {
              // TODO log
              throw new IllegalStateException(e);
            }
            addConfigClass(configClass);
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
      try {
        configClass = buildConfigClass(compilationsUnit);
      } catch (RuntimeException e) {
        throw new ExmlcException("while building config class " + name + ": " + e.getMessage(), e);
      }
    }
    if (configClass == null) {
      throw new ExmlcException("No config class '" + name + "' found.");
    }
    return configClass;
  }

  private ConfigClass buildConfigClass(CompilationUnit compilationsUnit) {
    ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationsUnit);
    return configClassBuilder.buildConfigClass();
  }

}
