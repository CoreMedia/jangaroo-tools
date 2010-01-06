/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class PropertyClassGenerator {
  private static Configuration cfg = new Configuration();

  static {
    /* Create and adjust freemarker configuration */
    cfg.setClassForTemplateLoading(PropertyClassGenerator.class, "/net/jangaroo/properties/templates");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
  }

  private LocalizationSuite suite;


  public PropertyClassGenerator(LocalizationSuite suite) {
    this.suite = suite;
  }

  public void generatePropertiesClass(PropertiesClass propertiesClass, Writer out) throws IOException, TemplateException {
    Template template = cfg.getTemplate("properties_class.ftl");
    template.process(propertiesClass, out);
  }

  public void generateJangarooClasses(ResourceBundleClass rbc) throws IOException, TemplateException {

    for (PropertiesClass pl : rbc.getPropertiesClasses()) {
      String rel = pl.getSrcFile().getPath().substring(suite.getRootDir().getPath().length());

      String convertedName = FileUtils.dirname(rel) + "/" + rbc.getClassName() + "_properties";
      if (!pl.getLocale().getLanguage().equals("en")) {
        convertedName += "_" + pl.getLocale();
      }
      convertedName += ".as";

      File outputFile = new File(suite.getOutputDir(), convertedName);

      if (!outputFile.getParentFile().exists()) {
        if (outputFile.getParentFile().mkdirs()) {
          //TODO:Errror
        }
      }

      FileWriter writer = null;
      try {
        writer = new FileWriter(outputFile);
        generatePropertiesClass(pl, writer);
      } catch (IOException e) {
        //Todo:Log.getErrorHandler().error("Exception while creating class", e);
      } catch (TemplateException e) {
        //Todo:Log.getErrorHandler().error("Exception while creating class", e);
      } finally {
        try {
          if (writer != null) {
            writer.close();
          }
        } catch (IOException e) {
          //never happen
        }
      }
    }

  }

  public void generate() throws IOException, TemplateException {
    for (ResourceBundleClass rbc : suite.getResourceBundles()) {
      generateJangarooClasses(rbc);
    }
  }
}
