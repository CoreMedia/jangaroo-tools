/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.properties.api.Propc;
import net.jangaroo.properties.api.PropcException;
import net.jangaroo.properties.api.PropcHelper;
import net.jangaroo.properties.api.PropertiesCompilerConfiguration;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PropertyClassGenerator implements Propc {
  private static Configuration cfg = new Configuration();

  private static final String OUTPUT_CHARSET = "UTF-8";

  static {
    /* Create and adjust freemarker configuration */
    cfg.setClassForTemplateLoading(PropertyClassGenerator.class, "/net/jangaroo/properties/templates");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
  }

  private PropertiesCompilerConfiguration config;


  @SuppressWarnings({"UnusedDeclaration" })
  public PropertyClassGenerator() {
  }

  public PropertyClassGenerator(PropertiesCompilerConfiguration config) {
    this.config = config;
  }

  @Override
  public PropertiesCompilerConfiguration getConfig() {
    return config;
  }

  @Override
  public void setConfig(PropertiesCompilerConfiguration config) {
    this.config = config;
  }

  public void generateApi(String propertiesClassName, InputStream sourceInputStream, OutputStreamWriter writer) throws IOException {
    PropertiesClass propertiesClass = parse(propertiesClassName, sourceInputStream);
    generatePropertiesClass(propertiesClass, writer, true);
  }

  public void generatePropertiesClass(PropertiesClass propertiesClass, Writer out, boolean api) throws IOException {
    Template template = cfg.getTemplate(api ? "properties_class.ftl" : "properties_js.ftl");
    try {
      Environment env = template.createProcessingEnvironment(propertiesClass, out);
      env.setOutputEncoding(OUTPUT_CHARSET);
      env.process();
    } catch (TemplateException e) {
      throw new IOException("Internal error in property FreeMarker template.", e);
    }
  }

  private File generateCode(PropertiesClass pl) {
    File outputFile = PropcHelper.computeGeneratedPropertiesJsFile(config, pl.getResourceBundle().getFullClassName(), pl.getLocale());
    generateCode(pl, outputFile, false);
    if (pl.getLocale() == null && config.isGenerateApi()) {
      // generate API from default locale bundle
      generateApi(pl);
    }
    return outputFile;
  }

  private File generateApi(PropertiesClass pl) {
    File apiOutputFile = PropcHelper.computeGeneratedPropertiesAS3File(config, pl.getResourceBundle().getFullClassName());
    return generateCode(pl, apiOutputFile, true);
  }

  private File generateCode(PropertiesClass pl, File outputFile, boolean api) {
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(outputFile), OUTPUT_CHARSET);
      generatePropertiesClass(pl, writer, api);
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
    for (File srcFile : config.getSourceFiles()) {
      File outputFile = generate(srcFile);
      outputFileMap.put(srcFile, Collections.singleton(outputFile));
    }
    return outputFileMap;
  }

  public Map<File,File> generateApi() {
    Map<File,File> outputFileMap = new LinkedHashMap<File, File>();
    for (File srcFile : config.getSourceFiles()) {
      File outputFile = generateApi(parse(srcFile));
      outputFileMap.put(srcFile, outputFile);
    }
    return outputFileMap;
  }

  public PropertiesClass parse(String propertiesClassName, InputStream in) throws IOException {
    PropertiesConfiguration p = new PropertiesConfiguration();
    p.setDelimiterParsingDisabled(true);
    Reader r = null;
    try {
      r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
      p.load(r);
    } catch (ConfigurationException e) {
      throw new PropcException("Internal error while parsing properties file.", e);
    } finally {
      try {
        if (r != null) {
          r.close();
        }
      } catch (IOException e) {
        //not really
      }
    }
    ResourceBundleClass bundle = new ResourceBundleClass(PropcHelper.computeBaseClassName(propertiesClassName));

    return new PropertiesClass(bundle, PropcHelper.computeLocale(propertiesClassName), p);
  }

  @Override
  public File generate(File propertiesFile) {
    PropertiesClass propertiesClass = parse(propertiesFile);
    // Create properties class, which registers itself with the bundle.
    return generateCode(propertiesClass);
  }

  public PropertiesClass parse(File propertiesFile) {
    String className;
    try {
      className = CompilerUtils.qNameFromFile(config.findSourceDir(propertiesFile), propertiesFile);
    } catch (IOException e1) {
      throw new PropcException(e1);
    }
    String propertiesClassName = className;
    PropertiesClass propertiesClass;
    try {
      propertiesClass = parse(propertiesClassName, new FileInputStream(propertiesFile));
    } catch (IOException e) {
      throw new PropcException("Error while parsing properties file", propertiesFile, e);
    }
    return propertiesClass;
  }

}
