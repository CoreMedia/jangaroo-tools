package net.jangaroo.extxml.generation;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.extxml.xml.ContentHandlerUtils;
import net.jangaroo.extxml.xml.ExmlToJsonHandler;
import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import net.jangaroo.extxml.log.Log;
import org.apache.maven.shared.model.fileset.mappers.FileNameMapper;
import org.apache.maven.shared.model.fileset.mappers.GlobPatternMapper;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Generates ActionScriptClasses out of xml components.
 */
public final class JooClassGenerator {

  private static final FileNameMapper XML_TO_JS_MAPPER = new GlobPatternMapper();

  static {
    XML_TO_JS_MAPPER.setFrom("*." + ComponentType.EXML.getExtension());
    XML_TO_JS_MAPPER.setTo("*." + ComponentType.ActionScript.getExtension());
  }

  private ComponentSuite componentSuite;

  public JooClassGenerator(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public void generateJangarooClass(ComponentClass jooClass, Writer output) throws IOException, TemplateException {
    if (validateComponentClass(jooClass)) {
      Configuration cfg = new Configuration();
      cfg.setClassForTemplateLoading(ComponentClass.class, "/");
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      if (jooClass.getJson() != null) {
        cfg.setSharedVariable("jsonForTemplate", jooClass.getJson().toString(2, 4));
      }
      Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/jangaroo_class.ftl");

      template.process(jooClass, output);
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

    if (StringUtils.isEmpty(jooClass.getSuperClassName())) {
      Log.e(String.format("Super class of component '%s' is undefined!", jooClass.getFullClassName()));
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
    for (ComponentClass cc : componentSuite.getComponentClassesByType(ComponentType.EXML)) {
      generateClass(cc);
    }
  }

  public File generateClass(ComponentClass componentClass) {
    ExmlToJsonHandler handler = new ExmlToJsonHandler(componentSuite);
    if (ContentHandlerUtils.parseExmlWithHandler(componentClass, handler)) {
      componentClass.setImports(handler.getImports());
      componentClass.setJson(handler.getJson());
      File outputFile = new File(componentSuite.getAs3OutputDir(), XML_TO_JS_MAPPER.mapFileName(componentClass.getRelativeSrcFilePath()));

      if(!outputFile.getParentFile().exists()) {
        if (outputFile.getParentFile().mkdirs()) {
          Log.d("Created parent output folder for " + outputFile.getAbsolutePath());
        }
      }

      FileWriter writer = null;
      try {
        writer = new FileWriter(outputFile);
        generateJangarooClass(componentClass, writer);
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
    return null;
  }
}
