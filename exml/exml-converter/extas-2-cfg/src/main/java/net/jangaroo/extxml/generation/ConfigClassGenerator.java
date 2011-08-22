package net.jangaroo.extxml.generation;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import net.jangaroo.utils.log.Log;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Generates new style config classes out of old style annotated AS components.
 */
public final class ConfigClassGenerator {

  private final static String outputCharset = "UTF-8";

  private ComponentSuite componentSuite;

  public ConfigClassGenerator(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public void generateJangarooClass(ComponentClass jooClass, String configClassName, Writer output) throws IOException, TemplateException {
    if (validateComponentClass(jooClass)) {
      Configuration cfg = new Configuration();
      cfg.setClassForTemplateLoading(ComponentClass.class, "/");
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/config_class.ftl");
      ConfigClassModel configClassModel = new ConfigClassModel(jooClass, componentSuite, configClassName);
      Log.i("Generate config class '" + configClassModel.getComponentSuite().getConfigClassPackage()+"."+ configClassModel.getClassName() + "' for component class '" + jooClass.getFullClassName()+"'");
      Environment env = template.createProcessingEnvironment(configClassModel, output);
      env.setOutputEncoding(outputCharset);
      env.process();
    }
  }

  private boolean validateComponentClass(ComponentClass jooClass) {
    boolean isValid = true;

    if (StringUtils.isEmpty(jooClass.getXtype())) {
      Log.e(String.format("Xtype of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    if (StringUtils.isEmpty(jooClass.getClassName())) {
      Log.e(String.format("Class name of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    for (String importStr : jooClass.getImports()) {
      if (StringUtils.isEmpty(importStr)) {
        Log.e(String.format("An empty import found. Something is wrong in your class %s", jooClass.getFullClassName()));
        isValid = false;
      }
    }

    return isValid;
  }

  public void generateClasses() {
    for (ComponentClass cc : componentSuite.getComponentClassesByType(ComponentType.ActionScript)) {
      generateClass(cc);
    }
  }

  public File generateClass(ComponentClass componentClass) {
    File configClassDir = new File(componentSuite.getAs3OutputDir(), componentSuite.getConfigClassPackage().replace('.', File.separatorChar));
    String configClassName = StringUtils.uncapitalise(componentClass.getLastXtypeComponent());
    File outputFile = new File(configClassDir, configClassName + ".as");

    if(!outputFile.getParentFile().exists()) {
      if (outputFile.getParentFile().mkdirs()) {
        Log.d("Folder '"+outputFile.getParentFile().getAbsolutePath() + "' created.");
      }
    }

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(outputFile), outputCharset);
      generateJangarooClass(componentClass, configClassName, writer);
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
    return outputFile;
  }
}
