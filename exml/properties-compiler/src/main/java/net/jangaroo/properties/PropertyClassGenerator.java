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
import java.util.Locale;

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

  public void generateJangarooClasses(ResourceBundleClass rbc) {

    for (PropertiesClass pl : rbc.getPropertiesClasses()) {

      File sourceDir = null;
      try {
        sourceDir = locations.findSourceDir(pl.getSrcFile());
      } catch (IOException e) {
        throw new PropcException(e);
      }

      String rel = pl.getSrcFile().getPath().substring(sourceDir.getPath().length());

      String convertedName = CompilerUtils.dirname(rel) + "/" + rbc.getClassName() + "_properties";

      if (pl.getLocale() != null) {
        convertedName += "_" + pl.getLocale();
      }
      convertedName += ".as";

      File outputFile = new File(locations.getOutputDirectory(), convertedName);
      outputFile.getParentFile().mkdirs(); // NOSONAR

      Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(outputFile), OUTPUT_CHARSET);
        generatePropertiesClass(pl, writer);
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

  }

  public void generate() {
    for (File srcFile : locations.getSourceFiles()) {

      String className = null;
      try {
        className = CompilerUtils.qNameFromFile(locations.findSourceDir(srcFile), srcFile);
      } catch (IOException e) {
        throw new PropcException(e);
      }
      String packageName = CompilerUtils.packageName(className);

      Locale locale;
      if (className.indexOf('_') != -1) {
        String localeString = className.substring(className.indexOf('_') + 1, className.length());
        if (localeString.indexOf('_') != -1) {
          String lang = localeString.substring(0, localeString.indexOf('_'));
          String countr = localeString.substring(lang.length() + 1, localeString.length());
          if (countr.indexOf('_') != -1) {
            String var = countr.substring(countr.indexOf('_') + 1, countr.length());
            countr = countr.substring(0, countr.indexOf('_'));
            locale = new Locale(lang, countr, var);
          } else {
            locale = new Locale(lang, countr);
          }
        } else {
          locale = new Locale(localeString);
        }
        className = className.substring(0, className.indexOf('_'));
      } else {
        locale = null;
      }

      ResourceBundleClass bundle = new ResourceBundleClass(className);

      PropertiesConfiguration p = new PropertiesConfiguration();
      p.setDelimiterParsingDisabled(true);
      Reader r = null;
      try {
        r = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
        p.load(r);
      } catch (Exception e) {
        throw new PropcException("Error while parsing properties file", srcFile, e);
      } finally {
        try {
          if (r != null) {
            r.close();
          }
        } catch (IOException e) {
          //not really
        }
      }
      // Create properties class, which registers itself with the bundle.
      new PropertiesClass(bundle, locale, p, srcFile);
      generateJangarooClasses(bundle);
    }
  }
}
