package net.jangaroo.exml.compiler;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.cli.ExmlcCommandLineParser;
import net.jangaroo.exml.generator.ExmlComponentClassGenerator;
import net.jangaroo.exml.generator.ExmlConfigClassGenerator;
import net.jangaroo.exml.generator.ExmlConfigPackageXsdGenerator;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.exml.parser.ExmlToModelParser;
import net.jangaroo.jooc.cli.CommandLineParseException;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public final class Exmlc implements net.jangaroo.exml.api.Exmlc {

  private ConfigClassRegistry configClassRegistry;
  private ExmlToConfigClassParser exmlToConfigClassParser;
  private ExmlConfigClassGenerator exmlConfigClassGenerator;
  private ExmlToModelParser exmlToModelParser;
  private ExmlComponentClassGenerator exmlComponentClassGenerator;
  private ExmlConfigPackageXsdGenerator exmlConfigPackageXsdGenerator;

  public Exmlc() {
  }

  public Exmlc(ExmlConfiguration config) {
    setConfig(config);
  }

  public static String parsePackageFromNamespace(String uri) {
    return uri.startsWith(EXML_CONFIG_URI_PREFIX) ? uri.substring(EXML_CONFIG_URI_PREFIX.length()) : null;
  }

  public static boolean isExmlNamespace(String uri) {
    return EXML_NAMESPACE_URI.equals(uri);
  }

  @Override
  public void setConfig(ExmlConfiguration config) {
    try {
      this.configClassRegistry = new ConfigClassRegistry(config);
    } catch (IOException e) {
      throw new ExmlcException("unable to build config class registry: " + e.getMessage(), e);
    }

    exmlToConfigClassParser = new ExmlToConfigClassParser(config);
    exmlConfigClassGenerator = new ExmlConfigClassGenerator();

    exmlToModelParser = new ExmlToModelParser(configClassRegistry);
    exmlComponentClassGenerator = new ExmlComponentClassGenerator(config);

    exmlConfigPackageXsdGenerator = new ExmlConfigPackageXsdGenerator(config);
  }

  @Override
  public ExmlConfiguration getConfig() {
    return configClassRegistry.getConfig();
  }

  public ExmlConfigPackageXsdGenerator getExmlConfigPackageXsdGenerator() {
    return exmlConfigPackageXsdGenerator;
  }

  public ExmlComponentClassGenerator getExmlComponentClassGenerator() {
    return exmlComponentClassGenerator;
  }

  public ConfigClassRegistry getConfigClassRegistry() {
    return configClassRegistry;
  }

  @Override
  public void generateAllConfigClasses() {
    for (File sourceFile : getConfig().getSourceFiles()) {
      generateConfigClass(sourceFile);
    }
  }

  @Override
  public File generateConfigClass(File source) {
    ConfigClass configClass;
    try {
      configClass = exmlToConfigClassParser.parseExmlToConfigClass(source);
    } catch (IOException e) {
      throw new ExmlcException("unable to parse EXML classes: " + e.getMessage(), source, e);
    }
    File targetFile = getConfig().computeConfigClassTarget(configClass.getName());

    // only recreate file if result file is older than the source file
    if (exmlConfigClassGenerator.mustGenerateConfigClass(source, targetFile)) {
      // generate the new config class ActionScript file
      try {
        exmlConfigClassGenerator.generateClass(configClass, targetFile);
      } catch (Exception e) {
        throw new ExmlcException("unable to generate config class: " + e.getMessage(), targetFile, e);
      }
    }

    return targetFile;
  }

  @Override
  public File generateComponentClass(File exmlSourceFile) {
    try {
      ExmlModel exmlModel = exmlToModelParser.parse(exmlSourceFile);
      return exmlComponentClassGenerator.generateClass(exmlModel);
    } catch (Exception e) {
      throw new ExmlcException("unable to generate component class: " + e.getMessage(), exmlSourceFile, e);
    }
  }

  @Override
  public void generateAllComponentClasses() {
    for (File sourceFile : getConfig().getSourceFiles()) {
      if (sourceFile.getName().endsWith(EXML_SUFFIX)) {
        generateComponentClass(sourceFile);
      }
    }
  }

  @Override
  public File generateXsd() {
    try {
      return exmlConfigPackageXsdGenerator.generateXsdFile(configClassRegistry);
    } catch (Exception e) {
      throw new ExmlcException("unable to generate xsd file: " + e.getMessage(), e);
    }
  }

  public static int run(String[] argv) {
    ExmlcCommandLineParser parser = new ExmlcCommandLineParser();
    ExmlConfiguration exmlConfiguration;
    try {
      exmlConfiguration = parser.parse(argv);
    } catch (CommandLineParseException e) {
      System.err.println(e.getMessage()); // NOSONAR this is a commandline tool
      return e.getExitCode();
    }

    if (exmlConfiguration != null) {
      Exmlc exmlc = new Exmlc(exmlConfiguration);
      exmlc.generateAllConfigClasses();
      exmlc.generateAllComponentClasses();
      exmlc.generateXsd();
    }
    return 0;
  }

  public static void main(String[] argv) {
    int result = run(argv);
    if (result != 0) {
      System.exit(result);
    }
  }
}
