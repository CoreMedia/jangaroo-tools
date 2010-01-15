/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import net.jangaroo.utils.log.Log;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

public class PropertyClassGenerator {
  private static Configuration cfg = new Configuration();

  private final static String outputCharset = "UTF-8";

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
    Environment env = template.createProcessingEnvironment(propertiesClass, out);
    env.setOutputEncoding(outputCharset);
    env.process();
  }

  public void generateJangarooClasses(ResourceBundleClass rbc) throws IOException, TemplateException {

    for (PropertiesClass pl : rbc.getPropertiesClasses()) {
      String rel = pl.getSrcFile().getPath().substring(suite.getRootDir().getPath().length());

      String convertedName = FileUtils.dirname(rel) + "/" + rbc.getClassName() + "_properties";

      //"en" is the default thats why we don't add the language here
      if (!pl.getLocale().equals(Locale.ENGLISH)) {
        convertedName += "_" + pl.getLocale();
      }
      convertedName += ".as";

      File outputFile = new File(suite.getOutputDir(), convertedName);

      if (!outputFile.getParentFile().exists()) {
        if (outputFile.getParentFile().mkdirs()) {
          Log.e("Could not create output folder");
        }
      }

      Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(outputFile), outputCharset);
        generatePropertiesClass(pl, writer);
      } catch (IOException e) {
        Log.e("error while generating class", e);
      } catch (TemplateException e) {
        Log.e("error while generating class", e);
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
