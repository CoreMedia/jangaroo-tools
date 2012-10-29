package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class ExmlConfigPackageXsdGenerator {

  private ExmlConfiguration config;

  public ExmlConfigPackageXsdGenerator(ExmlConfiguration config) {
    this.config = config;
  }

  public File generateXsdFile(final ConfigClassRegistry registry) throws IOException, TemplateException {
    registry.scanAllAsFiles();
    Map<String,Collection<ConfigClass>> configClassesByTargetClassPackage =
      registry.getRegisteredConfigClassesByTargetClassPackage();
    String packageName = config.getConfigClassPackage();
    for (Map.Entry<String, Collection<ConfigClass>> entry : configClassesByTargetClassPackage.entrySet()) {
      Collection<ConfigClass> configClasses = entry.getValue();
      // the map contains *all* target classes, so filter by those having their config class in the config's package:
      if (!configClasses.isEmpty() && packageName.equals(configClasses.iterator().next().getPackageName())) {
        ExmlConfigPackage suite = new ExmlConfigPackage(configClasses, entry.getKey(), true);
        generateXsdFile(suite);
      }
    }
    Collection<ConfigClass> configClasses = registry.getRegisteredConfigClasses();
    return generateXsdFile(new ExmlConfigPackage(configClasses, packageName, false));
  }

  private File generateXsdFile(ExmlConfigPackage suite) throws IOException, TemplateException {
    // Maybe even the directory does not exist.
    File targetPackageFolder = config.getResourceOutputDirectory();
    if(!targetPackageFolder.exists()) {
      //noinspection ResultOfMethodCallIgnored
      targetPackageFolder.mkdirs(); // NOSONAR
    }
    File result = new File(targetPackageFolder, suite.getPackageName() + ".xsd");

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), Exmlc.OUTPUT_CHARSET);
      generateXsdFile(suite, writer);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        //never happen
      }
    }
    return result;
  }

  public void generateXsdFile(final ConfigClassRegistry registry, final Writer output) throws IOException, TemplateException {
    registry.scanAllAsFiles();
    generateXsdFile(new ExmlConfigPackage(registry.getRegisteredConfigClasses(), config.getConfigClassPackage(), false),
      output);
  }

  private void generateXsdFile(ExmlConfigPackage suite, Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ExmlConfigPackage.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_config_package_xsd.ftl");
    Environment env = template.createProcessingEnvironment(suite, output);
    env.setOutputEncoding(Exmlc.OUTPUT_CHARSET);
    env.process();
  }

}
