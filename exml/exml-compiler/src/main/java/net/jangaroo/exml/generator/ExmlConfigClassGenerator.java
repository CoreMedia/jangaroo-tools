package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.parser.ExmlToConfigClassParser;
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
  private ExmlToConfigClassParser exmlToConfigClassParser;

  public ExmlConfigClassGenerator(ExmlConfiguration config) {
    this.config = config;

    exmlToConfigClassParser = new ExmlToConfigClassParser(config);
  }

  public ConfigClass generateConfigClass(File source) throws IOException {
    ConfigClass configClass = exmlToConfigClassParser.parseExmlToConfigClass(source);

    File targetFile = computeConfigClassTargetPath(config, configClass);

    // only recreate file if result file is older than the source file
    if(mustGenerateConfigClass(source, targetFile)) {
      // generate the new config class ActionScript file
      generateClass(configClass, targetFile);
    }

    return configClass;
  }

  private static void generateClass(final ConfigClass configClass, File result) {
    // Maybe even the directory does not exist.
    File targetPackageFolder = result.getAbsoluteFile().getParentFile();
    if(!targetPackageFolder.exists()) {
      targetPackageFolder.mkdirs();
    }

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), OUTPUT_CHARSET);
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

  private static void generateClass(final ConfigClass configClass, final Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ConfigClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_type_class.ftl");
    Environment env = template.createProcessingEnvironment(configClass, output);
    env.setOutputEncoding(OUTPUT_CHARSET);
    env.process();
  }

  private static boolean mustGenerateConfigClass(File source, File targetFile) {
    return !targetFile.exists() || targetFile.lastModified() < source.lastModified();
  }

  private static File computeConfigClassTargetPath(ExmlConfiguration config, ConfigClass configClass) {
    File targetPackageFolder = new File(config.getOutputDirectory(), config.getConfigClassPackage().replaceAll("\\.", File.separator));
    return new File(targetPackageFolder, configClass.getName() + ".as");
  }
}
