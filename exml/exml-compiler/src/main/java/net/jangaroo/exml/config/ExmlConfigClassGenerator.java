package net.jangaroo.exml.config;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.config.model.ConfigClass;
import net.jangaroo.utils.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 */
public final class ExmlConfigClassGenerator {

  private final static String outputCharset = "UTF-8";

  public static void generateClass(final ConfigClass configClass, final Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ConfigClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/config/template/exml_type_class.ftl");
    Environment env = template.createProcessingEnvironment(configClass, output);
    env.setOutputEncoding(outputCharset);
    env.process();
  }

  public static void generateClass(final ConfigClass configClass, File result) {
    Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(result), outputCharset);
        generateClass(configClass, writer);
      } catch (IOException e) {
        Log.e("Exception while creating class", e);
      } catch (TemplateException e) {
        Log.e("Exception while creating class", e);
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
