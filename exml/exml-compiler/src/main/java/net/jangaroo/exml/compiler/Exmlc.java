package net.jangaroo.exml.compiler;

import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.generator.ExmlComponentClassGenerator;
import net.jangaroo.exml.generator.ExmlConfigPackageXsdGenerator;
import net.jangaroo.exml.generator.ExmlConfigClassGenerator;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.exml.parser.ExmlToModelParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 05.07.11 Time: 11:01 To change this template use File | Settings |
 * File Templates.
 */
public class Exmlc {

  private final ConfigClassRegistry configClassRegistry;
  private final ExmlToConfigClassParser exmlToConfigClassParser;
  private final ExmlConfigClassGenerator exmlConfigClassGenerator;
  private ExmlToModelParser exmlToModelParser;
  private ExmlComponentClassGenerator exmlComponentClassGenerator;
  private ExmlConfigPackageXsdGenerator exmlConfigPackageXsdGenerator;


  public Exmlc(ExmlConfiguration config) throws ExmlcException {
    try {
      this.configClassRegistry = new ConfigClassRegistry(config);
    } catch (IOException e) {
      throw new ExmlcException("unable to build config class registry: " + e.getMessage(), e);
    }

    exmlToConfigClassParser = new ExmlToConfigClassParser(config);
    exmlConfigClassGenerator = new ExmlConfigClassGenerator(config);

    exmlToModelParser = new ExmlToModelParser(configClassRegistry);
    exmlComponentClassGenerator = new ExmlComponentClassGenerator(config);

    exmlConfigPackageXsdGenerator = new ExmlConfigPackageXsdGenerator(config);
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

  public ExmlConfiguration getConfig() {
    return configClassRegistry.getConfig();
  }

  public void generateAllConfigClasses() throws ExmlcException {
    for (File sourceFile : getConfig().getSourceFiles()) {
      generateConfigClass(sourceFile);
    }
  }

  public File generateConfigClass(File source) throws ExmlcException {
    ConfigClass configClass;
    try {
      configClass = exmlToConfigClassParser.parseExmlToConfigClass(source);
    } catch (IOException e) {
      throw new ExmlcException("unable to parse EXML classes: " + e.getMessage(), source, e);
    }
    File targetFile = exmlConfigClassGenerator.computeConfigClassTarget(configClass.getName());

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

  public File generateComponentClass(File exmlSourceFile) throws ExmlcException {
    try {
      ExmlModel exmlModel = exmlToModelParser.parse(exmlSourceFile);
      return exmlComponentClassGenerator.generateClass(exmlModel);
    } catch (Exception e) {
      throw new ExmlcException("unable to generate component class: " + e.getMessage(), exmlSourceFile, e);
    }
  }

  public void generateAllComponentClasses() throws ExmlcException {
    for (File sourceFile : getConfig().getSourceFiles()) {
      if (sourceFile.getName().endsWith(ExmlConstants.EXML_SUFFIX)) {
        generateComponentClass(sourceFile);
      }
    }
  }

  public void generateXsd(File target) {
    try {
      exmlConfigPackageXsdGenerator.generateXsdFile(configClassRegistry, target);
    } catch (Exception e) {
      throw new ExmlcException("unable to generate xsd file: " + e.getMessage(), target, e);
    }
  }
}
