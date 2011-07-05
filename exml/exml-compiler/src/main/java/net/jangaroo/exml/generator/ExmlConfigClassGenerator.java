package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.utils.CompilerUtils;
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
  private final static String OUTPUT_CHARSET = "UTF-8";

  private ExmlConfiguration config;

  public ExmlConfigClassGenerator(ExmlConfiguration config) {
    this.config = config;
  }

  public void generateClass(final ConfigClass configClass, File result) throws IOException, TemplateException {
    // Maybe even the directory does not exist.
    File targetPackageFolder = result.getAbsoluteFile().getParentFile();
    if(!targetPackageFolder.exists()) {
      targetPackageFolder.mkdirs();
    }

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), OUTPUT_CHARSET);
      generateClass(configClass, writer);
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

  private static void generateClass(final ConfigClass configClass, final Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ConfigClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_type_class.ftl");
    Environment env = template.createProcessingEnvironment(configClass, output);
    env.setOutputEncoding(OUTPUT_CHARSET);
    env.process();
  }

  public boolean mustGenerateConfigClass(File source, File targetFile) {
    return !targetFile.exists() || targetFile.lastModified() < source.lastModified();
  }

  public File computeConfigClassTarget(String configClassName) {
    return CompilerUtils.fileFromQName(config.getConfigClassPackage(), configClassName, config.getOutputDirectory(), JangarooParser.AS_SUFFIX);
  }

}
