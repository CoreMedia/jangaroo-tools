package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.utils.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 */
public final class ExmlComponentClassGenerator {

  private final static String outputCharset = "UTF-8";

  public static void generateClass(final ExmlModel model, String packageName, final Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ExmlComponentClassModel.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_component_class.ftl");
    ExmlComponentClassModel exmlComponentClassModel = new ExmlComponentClassModel(packageName, model.getJsonObject().toString(2, 4).trim(), model);
    Environment env = template.createProcessingEnvironment(exmlComponentClassModel, output);
    env.setOutputEncoding(outputCharset);
    env.process();
  }

  public static void generateClass(final ExmlModel model, String packageName, File result) {
    Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(result), outputCharset);
        generateClass(model, packageName, writer);
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
