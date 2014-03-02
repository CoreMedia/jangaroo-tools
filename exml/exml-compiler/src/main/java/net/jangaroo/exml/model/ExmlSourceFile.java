package net.jangaroo.exml.model;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.generator.ExmlComponentClassGenerator;
import net.jangaroo.exml.generator.ExmlConfigClassGenerator;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.exml.parser.ExmlToModelParser;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;

/**
 * An EXML source file to compile into a config class and a target class.
 */
public class ExmlSourceFile {

  private ConfigClassRegistry configClassRegistry;
  private File sourceFile;
  private String configClassName;
  private String targetClassName;
  private File generatedConfigClassFile;
  private File generatedTargetClassFile;

  public ExmlSourceFile(ConfigClassRegistry configClassRegistry, File sourceFile) throws IOException {
    this.configClassRegistry = configClassRegistry;
    this.sourceFile = sourceFile;
    configClassName = CompilerUtils.qName(configClassRegistry.getConfig().getConfigClassPackage(),
            CompilerUtils.uncapitalize(CompilerUtils.removeExtension(sourceFile.getName())));
    File sourceDir = configClassRegistry.getConfig().findSourceDir(sourceFile);
    String exmlClassName = CompilerUtils.qNameFromFile(sourceDir, sourceFile);
    targetClassName = CompilerUtils.qName(CompilerUtils.packageName(exmlClassName),
            ExmlUtils.createComponentClassName(CompilerUtils.className(exmlClassName)));
  }

  public File getSourceFile() {
    return sourceFile;
  }

  public String getConfigClassName() {
    return configClassName;
  }

  public String getTargetClassName() {
    return targetClassName;
  }

  public ConfigClass getConfigClass() {
    return configClassRegistry.getConfigClassByName(configClassName);
  }

  public ConfigClass parseExmlToConfigClass() {
    try {
      ConfigClass configClass = new ExmlToConfigClassParser().parseExmlToConfigClass(getSourceFile());
      configClass.setFullName(getConfigClassName());
      configClass.setComponentClassName(getTargetClassName());
      return configClass;
    } catch (IOException e) {
      // TODO Log and continue?
      throw new ExmlcException("could not read EXML file", e);
    }
  }
  
  public File generateConfigClass() {
    if (generatedConfigClassFile == null) {
      ConfigClass configClass = getConfigClass();
      generatedConfigClassFile = configClassRegistry.getConfig().computeConfigClassTarget(configClass.getName());
      // only recreate file if result file is older than the source file
      if (mustGenerate(generatedConfigClassFile)) {
        // generate the new config class ActionScript file
        try {
          new ExmlConfigClassGenerator().generateClass(configClass, generatedConfigClassFile);
        } catch (Exception e) {
          throw new ExmlcException("unable to generate config class: " + e.getMessage(), generatedConfigClassFile, e);
        }
      }
    }
    return generatedConfigClassFile;
  }

  public File generateTargetClass() {
    if (generatedTargetClassFile == null) {
      // only generate component class if it is not already present as source:
      File classFile = new File(ExmlUtils.createComponentClassName(CompilerUtils.removeExtension(sourceFile.getName())) + Jooc.AS_SUFFIX);
      if (!classFile.exists()) {
        try {
          ExmlConfiguration config = configClassRegistry.getConfig();
          generatedTargetClassFile = config.computeGeneratedComponentClassFile(sourceFile);
          if (mustGenerate(generatedTargetClassFile)) {
            ExmlModel exmlModel = new ExmlToModelParser(configClassRegistry).parse(sourceFile);
            return new ExmlComponentClassGenerator(config).generateClass(exmlModel, generatedTargetClassFile);
          }
        } catch (Exception e) {
          throw new ExmlcException("unable to generate component class: " + e.getMessage(), sourceFile, e);
        }
      }
    }
    return generatedTargetClassFile;
  }
  
  private boolean mustGenerate(File generatedFile) {
    return sourceFile != null && generatedFile != null &&
            (!generatedFile.exists() || generatedFile.lastModified() < sourceFile.lastModified());
  }



}
