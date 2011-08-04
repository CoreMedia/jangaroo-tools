package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

/**
 *
 */
public class ExmlConfigPackageXsdGenerator {

  private ExmlConfiguration config;

  public ExmlConfigPackageXsdGenerator(ExmlConfiguration config) {
    this.config = config;
  }

  public void generateXsdFile(final ConfigClassRegistry registry, File result) throws IOException, TemplateException {
    // Maybe even the directory does not exist.
    File targetPackageFolder = result.getAbsoluteFile().getParentFile();
    if(!targetPackageFolder.exists()) {
      targetPackageFolder.mkdirs(); // NOSONAR
    }

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), ExmlConstants.OUTPUT_CHARSET);
      generateXsdFile(registry, writer);
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

  public void generateXsdFile(final ConfigClassRegistry registry, final Writer output) throws IOException, TemplateException {
    registry.scanAllAsFiles();
    Collection<ConfigClass> configClasses = registry.getRegisteredConfigClasses();
    ExmlConfigPackage suite = new ExmlConfigPackage(configClasses, config.getConfigClassPackage());

    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ExmlConfigPackage.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_config_package_xsd.ftl");
    Environment env = template.createProcessingEnvironment(suite, output);
    env.setOutputEncoding(ExmlConstants.OUTPUT_CHARSET);
    env.process();
  }

  

}
