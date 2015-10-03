package net.jangaroo.exml.compiler;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.cli.ExmlcCommandLineParser;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.jooc.cli.CommandLineParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 */
public final class Exmlc implements net.jangaroo.exml.api.Exmlc {

  private ConfigClassRegistry configClassRegistry;

  public Exmlc() {
  }

  public Exmlc(ExmlConfiguration config) {
    setConfig(config);
  }

  public static AnnotationAt parseAnnotationAtValue(String annotationAt) {
    if (annotationAt == null || annotationAt.length() == 0) {
      return null;
    }
    try {
      return AnnotationAt.valueOf(annotationAt.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ExmlcException("EXML attribute '" + EXML_ANNOTATION_AT_ATTRIBUTE +
              "' must have one the values 'config', 'target', or 'both', not '" + annotationAt + "'.");
    }
  }

  public static PublicApiMode parsePublicApiMode(String publicApiMode) {
    if (publicApiMode == null || publicApiMode.length() == 0) {
      return null;
    }
    try {
      return PublicApiMode.valueOf(publicApiMode.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ExmlcException("EXML attribute '" + EXML_PUBLIC_API_ATTRIBUTE +
              "' must have one the values 'false', 'config', or 'true', not '" + publicApiMode + "'.");
    }
  }

  @Override
  public void setConfig(ExmlConfiguration config) {
    try {
      this.configClassRegistry = new ConfigClassRegistry(config);
    } catch (IOException e) {
      throw new ExmlcException("unable to build config class registry: " + e.getMessage(), e);
    }
  }

  @Override
  public ExmlConfiguration getConfig() {
    return configClassRegistry.getConfig();
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
    return configClassRegistry.generateConfigClass(source);
  }

  @Override
  public File generateComponentClass(File exmlSourceFile) {
    return configClassRegistry.generateTargetClass(exmlSourceFile);
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
    // Maybe even the directory does not exist.
    File targetPackageFolder = getConfig().getResourceOutputDirectory();
    if(!targetPackageFolder.exists()) {
      //noinspection ResultOfMethodCallIgnored
      targetPackageFolder.mkdirs(); // NOSONAR
    }

    File result = new File(targetPackageFolder, getConfig().getConfigClassPackage() + ".xsd");

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);
      configClassRegistry.generateXsd(writer);
    } catch (Exception e) {
      throw new ExmlcException("unable to generate xsd file: " + e.getMessage(), e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        //never happen
      }
    }
    return result;
  }

  public File[] convertToMxml() {
    return new ExmlToMxml(configClassRegistry).convert();
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
      if (exmlConfiguration.isConvertToMxml()) {
        exmlc.convertToMxml();
      } else {
        exmlc.generateAllConfigClasses();
        exmlc.generateAllComponentClasses();
        exmlc.generateXsd();
      }
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
