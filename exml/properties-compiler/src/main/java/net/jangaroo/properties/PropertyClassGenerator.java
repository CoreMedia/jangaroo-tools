/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import net.jangaroo.utils.CompilerUtils;
import net.jangaroo.utils.FileLocations;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PropertyClassGenerator {
  private static Configuration cfg = new Configuration();

  private static final String OUTPUT_CHARSET = "UTF-8";

  static {
    /* Create and adjust freemarker configuration */
    cfg.setClassForTemplateLoading(PropertyClassGenerator.class, "/net/jangaroo/properties/templates");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
  }

  private FileLocations locations;


  public PropertyClassGenerator(FileLocations locations) {
    this.locations = locations;
  }

  public void generatePropertiesClass(PropertiesClass propertiesClass, Writer out) throws IOException, TemplateException {
    Template template = cfg.getTemplate("properties_class.ftl");
    Environment env = template.createProcessingEnvironment(propertiesClass, out);
    env.setOutputEncoding(OUTPUT_CHARSET);
    env.process();
  }

  public File generateJangarooClass(PropertiesClass pl) {
    File outputFile = computeGeneratedPropertiesClassFile(pl.getResourceBundle().getFullClassName(), pl.getLocale());
    
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(outputFile), OUTPUT_CHARSET);
      generatePropertiesClass(pl, writer);
      return outputFile;
    } catch (Exception e) {
      throw new PropcException(e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        //
      }
    }
  }

  public Map<File,Set<File>> generate() {
    Map<File,Set<File>> outputFileMap = new LinkedHashMap<File, Set<File>>();
    for (File srcFile : locations.getSourceFiles()) {
      File outputFile = generate(srcFile);
      outputFileMap.put(srcFile, Collections.singleton(outputFile));
    }
    return outputFileMap;
  }

  public File generate(File propertiesFile) {
    PropertiesConfiguration p = new PropertiesConfiguration();
    p.setDelimiterParsingDisabled(true);
    Reader r = null;
    try {
      r = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8"));
      p.load(r);
    } catch (IOException e) {
      throw new PropcException("Error while parsing properties file", propertiesFile, e);
    } catch (ConfigurationException e) {
      throw new PropcException("Error while parsing properties file", propertiesFile, e);
    } finally {
      try {
        if (r != null) {
          r.close();
        }
      } catch (IOException e) {
        //not really
      }
    }

    ResourceBundleClass bundle = new ResourceBundleClass(computeBaseClassName(propertiesFile));

    // Create properties class, which registers itself with the bundle.
    return generateJangarooClass(new PropertiesClass(bundle, computeLocale(propertiesFile), p, propertiesFile));
  }

  public File computeGeneratedPropertiesClassFile(File propertiesFile) {
    return computeGeneratedPropertiesClassFile(computeBaseClassName(propertiesFile), computeLocale(propertiesFile));
  }

  private File computeGeneratedPropertiesClassFile(String className, Locale locale) {
    StringBuilder suffix = new StringBuilder("_properties");
    if (locale != null) {
      suffix.append("_").append(locale);
    }
    suffix.append(".as");
    String generatedPropertiesClassFileName = CompilerUtils.fileNameFromQName(className, '/', suffix.toString());
    return new File(locations.getOutputDirectory(), generatedPropertiesClassFileName); 
  }

  private String computeBaseClassName(File srcFile) {
    String className;
    try {
      className = CompilerUtils.qNameFromFile(locations.findSourceDir(srcFile), srcFile);
    } catch (IOException e) {
      throw new PropcException(e);
    }
    int underscorePos = className.indexOf('_');
    if (underscorePos != -1) {
      className = className.substring(0, underscorePos);
    }
    return className;
  }

  private static Locale computeLocale(File propertiesFile) {
    String propertiesFileName = CompilerUtils.removeExtension(propertiesFile.getName());
    String[] parts = propertiesFileName.split("_", 4);
    switch (parts.length) {
      case 4: return new Locale(parts[1], parts[2], parts[3]);
      case 3: return new Locale(parts[1], parts[2]);
      case 2: return new Locale(parts[1]);
    }
    return null;
  }
}
