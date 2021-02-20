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
import java.util.List;

public class Propc {
  public static final String PROPERTIES_CLASS_FTL = "properties_class.ftl";
  public static final String PROPERTIES_SUBCLASS_FTL = "properties_subclass.ftl";

  @SuppressWarnings("deprecation")
  private static Configuration cfg = new Configuration();

  private static final String OUTPUT_CHARSET = "UTF-8";

  static {
    /* Create and adjust freemarker configuration */
    cfg.setClassForTemplateLoading(Propc.class, "/net/jangaroo/properties/templates");
    //noinspection deprecation
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
  }

  public Propc() {
  }

  public void generateApi(String propertiesClassName, InputStream sourceInputStream, OutputStreamWriter writer) throws IOException {
    PropertiesClass propertiesClass = parse(propertiesClassName, sourceInputStream);
    generatePropertiesClass(propertiesClass, writer, propertiesClass.getLocale() == null ? PROPERTIES_CLASS_FTL : PROPERTIES_SUBCLASS_FTL);
  }

  private void generatePropertiesClass(PropertiesClass propertiesClass, Writer out, String templateFile) throws IOException {
    Template template = cfg.getTemplate(templateFile);
    try {
      Environment env = template.createProcessingEnvironment(propertiesClass, out);
      env.setOutputEncoding(OUTPUT_CHARSET);
      env.process();
    } catch (TemplateException e) {
      throw new IOException("Internal error in property FreeMarker template.", e);
    }
  }

  private File generateCode(PropertiesClass pl, File outputFile, String templateFile) {
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(outputFile), OUTPUT_CHARSET);
      generatePropertiesClass(pl, writer, templateFile);
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

  private File generateApi(PropertiesClass pl, File outputDirectory) {
    File apiOutputFile = PropcHelper.computeGeneratedPropertiesAS3File(outputDirectory, pl.getResourceBundle().getFullClassName(), pl.getLocale());
    return generateCode(pl, apiOutputFile, PROPERTIES_CLASS_FTL);
  }

  /**
   * Generate AS3 native classes representing the ActionScript API to the JS classes generated from the properties files.
   * The files within the given sourcePath should already be canonicalized using {@link File#getCanonicalFile()}.
   */
  public void generateApi(List<File> sourceFiles, List<File> sourcePath, File outputDirectory) {
    for (File srcFile : sourceFiles) {
      if (!srcFile.getName().contains("_")) {
        PropertiesClass propertiesClass = parse(srcFile, sourcePath);
        generateApi(propertiesClass, outputDirectory);
      }
    }
  }

  private PropertiesClass parse(String propertiesClassName, InputStream in) throws IOException {
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

  private PropertiesClass parse(File propertiesFile, List<File> sourcePath) {
    String className;
    try {
      className = CompilerUtils.qNameFromFile(sourcePath, propertiesFile);
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
