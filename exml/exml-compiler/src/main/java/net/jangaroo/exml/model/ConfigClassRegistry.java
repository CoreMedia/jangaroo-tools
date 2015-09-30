package net.jangaroo.exml.model;

import freemarker.template.TemplateException;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.config.ValidationMode;
import net.jangaroo.exml.generator.ExmlConfigPackageXsdGenerator;
import net.jangaroo.exml.parser.ExmlValidator;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class ConfigClassRegistry {
  private Map<String, ConfigClass> configClassesByName = new HashMap<String, ConfigClass>();

  private ExmlConfiguration config;
  private InputSource sourcePathInputSource;

  private JangarooParser jangarooParser;
  private ExmlConfigPackageXsdGenerator exmlConfigPackageXsdGenerator;
  private Map<String, ExmlSourceFile> exmlFilesByConfigClassName;
  private Set<ConfigClass> sourceConfigClasses;

  public ConfigClassRegistry(final ExmlConfiguration config) throws IOException {
    this.config = config;

    sourcePathInputSource = PathInputSource.fromFiles(config.getSourcePath(), new String[0], true);

    ParserOptions parserOptions = new CCRParserOptions();
    jangarooParser = new JangarooParser(parserOptions, new StdOutCompileLog()) {
      @Override
      protected InputSource findSource(String qname) {
        InputSource inputSource = super.findSource(qname);
        if (inputSource != null) {
          // A regular source file (not a generated file) has been found. Use it.
          return inputSource;
        }
        // Just in case the requested class is a class
        // that is generated from an EXML file, regenerate the file before
        // it is too late. This will only affect generated files, so it is pretty safe.
        tryGenerateClass(qname);
        // Just in case the source was not found on the first attempt, fetch it again.
        return super.findSource(qname);
      }
    };
    List<File> fullClassPath = new ArrayList<File>(config.getClassPath());
    fullClassPath.add(config.getOutputDirectory());
    InputSource classPathInputSource = PathInputSource.fromFiles(fullClassPath,
      new String[]{"", JangarooParser.JOO_API_IN_JAR_DIRECTORY_PREFIX}, false);
    jangarooParser.setUp(sourcePathInputSource, classPathInputSource);
    exmlConfigPackageXsdGenerator = new ExmlConfigPackageXsdGenerator();
  }

  private void tryGenerateClass(String qname) {
    ExmlSourceFile exmlSourceFile = getExmlSourceFilesByConfigClassName().get(qname);
    if (exmlSourceFile != null) {
      exmlSourceFile.generateConfigClass();
    } else {
      // is there an EXML file for the qname interpreted as a target class name?
      InputSource exmlInputSource = sourcePathInputSource.getChild(JangarooParser.getInputSourceFileName(qname, sourcePathInputSource, Exmlc.EXML_SUFFIX));
      if (exmlInputSource != null) {
        String configClassName = computeConfigClassNameFromTargetClassName(qname);
        exmlSourceFile = getExmlSourceFilesByConfigClassName().get(configClassName);
        if (exmlSourceFile != null) {
          exmlSourceFile.generateTargetClass();
        }
      }
    }
  }

  public ExmlConfiguration getConfig() {
    return config;
  }

  /**
   * Returns the list of all config classes in the source path, defined in EXML or ActionScript.
   * @return list of registered Config classes
   */
  public Collection<ConfigClass> getSourceConfigClasses() {
    if (sourceConfigClasses == null) {
      //  Determine the set of all source config classes by scanning for all .exml and .as files in the sourcepath,
      //  parsing them and adding their models to this registry.
      sourceConfigClasses = new LinkedHashSet<ConfigClass>();
      Map<String, File> sourceFilesByName = new LinkedHashMap<String, File>();
      scanExmlFiles(sourceFilesByName);
      scanAsFiles(sourceFilesByName);
    }
    return sourceConfigClasses;
  }

  private void scanExmlFiles(Map<String, File> sourceFilesByName) {
    for (ExmlSourceFile exmlFile : getExmlSourceFilesByConfigClassName().values()) {
      addSourceConfigClass(sourceFilesByName, exmlFile.getSourceFile(), exmlFile.getConfigClass());
    }
  }

  private void scanAsFiles(Map<String, File> sourceFilesByName) {
    InputSource configPackageInputSource = sourcePathInputSource.getChild(config.getConfigClassPackage().replace('.', File.separatorChar));
    if (configPackageInputSource != null) {
      for (InputSource source : configPackageInputSource.list()) {
        File file = ((FileInputSource) source).getFile();
        if (file.isFile() && file.getName().endsWith(Jooc.AS_SUFFIX)) {
          try {
            File sourceDir = getConfig().findSourceDir(file);
            String qName = CompilerUtils.qNameFromFile(sourceDir, file);
            ConfigClass actionScriptConfigClass = findActionScriptConfigClass(qName);
            if (actionScriptConfigClass != null) {
              addSourceConfigClass(sourceFilesByName, file, actionScriptConfigClass);
            }
          } catch (IOException e) {
            throw new ExmlcException("could not read AS file", e);
          }
        }
      }
    }
  }

  private void addSourceConfigClass(Map<String, File> sourceFilesByName, File sourceFile, ConfigClass configClass) {
    if (configClass != null) {
      String qname = configClass.getFullName();
      File existingConfigClassSourceFile = sourceFilesByName.get(qname);
      if (existingConfigClassSourceFile != null) {
        throw new ExmlcException(String.format("Config class '%s' already declared in file %s.", qname, existingConfigClassSourceFile.getPath()), sourceFile, null);
      } else {
        sourceFilesByName.put(qname, sourceFile);
        sourceConfigClasses.add(configClass);
      }
    }
  }
  
  public File generateConfigClass(File exmlFile) {
    return getExmlSourceFile(exmlFile).generateConfigClass();
  }

  public ExmlSourceFile getExmlSourceFile(File exmlFile) {
    return getExmlSourceFilesByConfigClassName().get(computeConfigClassName(exmlFile));
  }

  // unfortunately, we cannot determine the name of the source EXML file, so we have to scane the whole source path:
  private Map<String, ExmlSourceFile> getExmlSourceFilesByConfigClassName() {
    if (exmlFilesByConfigClassName == null) {
      exmlFilesByConfigClassName = new LinkedHashMap<String, ExmlSourceFile>();
      try {
        buildConfigClassNameToExmlSourceFileMap(sourcePathInputSource);
      } catch (IOException e) {
        throw new ExmlcException("Unable to scan source directory for EXML files.", e);
      }
    }
    return exmlFilesByConfigClassName;
  }

  private void buildConfigClassNameToExmlSourceFileMap(InputSource inputSource) throws IOException {
    for (InputSource source : inputSource.list()) {
      File exmlFile = ((FileInputSource) source).getFile();
      if (exmlFile.isFile()) {
        if (exmlFile.getName().endsWith(Exmlc.EXML_SUFFIX)) {
          exmlFilesByConfigClassName.put(computeConfigClassName(exmlFile), new ExmlSourceFile(this, exmlFile));
        }
      } else {
        // Recurse into the tree.
        buildConfigClassNameToExmlSourceFileMap(source);
      }
    }
  }

  private String computeConfigClassName(File exmlFile) {
    return CompilerUtils.qName(
            getConfig().getConfigClassPackage(),
            CompilerUtils.uncapitalize(CompilerUtils.removeExtension(exmlFile.getName())));
  }

  private String computeConfigClassNameFromTargetClassName(String targetClassName) {
    return CompilerUtils.qName(getConfig().getConfigClassPackage(),
            CompilerUtils.uncapitalize(CompilerUtils.className(targetClassName)));
  }

  /**
   * Get a ConfigClass for the given name. Returns null if no class was found
   * @param name the name of the class
   * @return the configClass or null if none was found
   */
  public ConfigClass getConfigClassByName(String name) {
    ConfigClass configClass = configClassesByName.get(name);
    if (configClass != null) {
      return configClass;
    }
    // The config class has not been registered so far.

    // The config class might originate from one of of this module's EXML files.
    ExmlSourceFile exmlFile = getExmlSourceFilesByConfigClassName().get(name);
    if (exmlFile != null) {
      return addConfigClass(exmlFile.parseExmlToConfigClass());
    }
    // The given name does not denote a config class of an EXML component in the source tree.
    configClass = findActionScriptConfigClass(name);
    return configClass;
  }

  private ConfigClass addConfigClass(ConfigClass configClass) {
    if (configClass != null) {
      configClass.setConfigClassRegistry(this);
      configClassesByName.put(configClass.getFullName(), configClass);
    }
    return configClass;
  }

  private ConfigClass findActionScriptConfigClass(String name) {
    CompilationUnit compilationsUnit = jangarooParser.getCompilationUnit(name);
    if (compilationsUnit != null) {
      try {
        return buildConfigClass(compilationsUnit);
      } catch (RuntimeException e) {
        throw new ExmlcException("while building config class '" + name + "': " + e.getMessage(), e);
      }
    }
    return null;
  }

  private ConfigClass buildConfigClass(CompilationUnit compilationUnit) {
    ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationUnit);
    return addConfigClass(configClassBuilder.buildConfigClass());
  }

  public File generateTargetClass(File exmlSourceFile) {
    return getExmlSourceFile(exmlSourceFile).generateTargetClass();
  }

  public void generateXsd(Writer output) throws IOException, TemplateException {
    exmlConfigPackageXsdGenerator.generateXsdFile(getSourceConfigClasses(), config.getConfigClassPackage(), output);
    if (getConfig().getValidationMode() != ValidationMode.OFF) {
      try {
        new ExmlValidator(getConfig()).validateAllExmlFiles();
      } catch (Exception e) {
        throw new ExmlcException("unable to start validation", e);
      }
    }
  }


  private static class CCRParserOptions implements ParserOptions {
    @Override
    public SemicolonInsertionMode getSemicolonInsertionMode() {
      return SemicolonInsertionMode.QUIRKS;
    }

    @Override
    public boolean isVerbose() {
      return false;
    }
  }
}
