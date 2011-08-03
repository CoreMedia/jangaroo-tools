package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 */
public final class ExmlComponentClassGenerator {

  private ExmlConfiguration config;

  public ExmlComponentClassGenerator(ExmlConfiguration config) {
    this.config = config;
  }

  public File computeComponentClassTarget(ExmlModel model) {
    return CompilerUtils.fileFromQName(model.getPackageName(), model.getClassName(), config.getOutputDirectory(), JangarooParser.AS_SUFFIX);
  }

  public void generateClass(final ExmlModel model, final Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ExmlComponentClassModel.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_component_class.ftl");
    ExmlComponentClassModel exmlComponentClassModel = new ExmlComponentClassModel(model, config.getConfigClassPackage());
    Environment env = template.createProcessingEnvironment(exmlComponentClassModel, output);
    env.setOutputEncoding(ExmlConstants.OUTPUT_CHARSET);
    env.process();
  }

  public File generateClass(final ExmlModel model) throws IOException, TemplateException {
    File result = computeComponentClassTarget(model);
    Writer writer = null;
    try {
      result.getParentFile().mkdirs();  // NOSONAR
      writer = new OutputStreamWriter(new FileOutputStream(result), ExmlConstants.OUTPUT_CHARSET);
      generateClass(model, writer);
      return result;
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
