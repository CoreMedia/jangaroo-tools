package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.exml.model.ConfigClass;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 *
 */
public class ExmlConfigPackageXsdGenerator {

  public void generateXsdFile(final Collection<ConfigClass> configClasses, String configClassPackage,
                              final Writer output) throws IOException, TemplateException {
    ExmlConfigPackage suite = new ExmlConfigPackage(configClasses, configClassPackage);
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ExmlConfigPackage.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_config_package_xsd.ftl");
    Environment env = template.createProcessingEnvironment(suite, output);
    env.setOutputEncoding(Exmlc.OUTPUT_CHARSET);
    env.process();
  }

}
