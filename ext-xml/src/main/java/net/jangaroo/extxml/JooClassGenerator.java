package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.shared.model.fileset.mappers.FileNameMapper;
import org.apache.maven.shared.model.fileset.mappers.GlobPatternMapper;
import org.codehaus.plexus.util.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Generates ActionScriptClasses out of xml components.
 */
public class JooClassGenerator {

  private static final FileNameMapper XML_TO_JS_MAPPER = new GlobPatternMapper();

  static {
    XML_TO_JS_MAPPER.setFrom("*." + ComponentType.EXML.getExtension());
    XML_TO_JS_MAPPER.setTo("*." + ComponentType.ActionScript.getExtension());
  }

  private ErrorHandler errorHandler;
  private ComponentSuite componentSuite;

  public JooClassGenerator(ComponentSuite componentSuite, ErrorHandler handler) {
    this.errorHandler = handler;
    this.componentSuite = componentSuite;
  }

  public void generateJangarooClass(ComponentClass jooClass, Writer output) throws IOException, TemplateException {
    if (validateComponentClass(jooClass)) {
      Configuration cfg = new Configuration();
      cfg.setClassForTemplateLoading(ComponentClass.class, "/");
      cfg.setObjectWrapper(new DefaultObjectWrapper());

      Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/jangaroo_class.ftl");

      template.process(jooClass, output);
    }
  }

  private boolean validateComponentClass(ComponentClass jooClass) {
    boolean isValid = true;
    if (jooClass.getPackageName() == null) {
      errorHandler.error(String.format("Package name of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    if (StringUtils.isEmpty(jooClass.getXtype())) {
      errorHandler.error(String.format("Xtype of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    if (StringUtils.isEmpty(jooClass.getClassName())) {
      errorHandler.error(String.format("Class name of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    if (StringUtils.isEmpty(jooClass.getSuperClassName())) {
      errorHandler.error(String.format("Super class of component '%s' is undefined!", jooClass.getFullClassName()));
      isValid = false;
    }

    for (String importStr : jooClass.getImports()) {
      if(StringUtils.isEmpty(importStr)) {
        errorHandler.error(String.format("An empty import found. Something is wrong in your class %s", jooClass.getFullClassName()));
        isValid = false;
      }
    }

    return isValid;
  }

  private XmlToJsonHandler createHandlerFromClass(ComponentClass cc) {
    FileInputStream inputStream = null;
    XmlToJsonHandler handler = null;
    errorHandler.setCurrentFile(cc.getSrcFile());
    try {
      XMLReader xr = XMLReaderFactory.createXMLReader();
      handler = new XmlToJsonHandler(componentSuite, errorHandler);
      xr.setContentHandler(handler);
      inputStream = new FileInputStream(cc.getSrcFile());
      xr.parse(new InputSource(inputStream));
    } catch (FileNotFoundException e) {
      errorHandler.error("Exception while parsing", e);
    } catch (IOException e) {
      errorHandler.error("Exception while parsing", e);
    } catch (SAXException e) {
      errorHandler.error("Exception while parsing", e);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        //never happend
      }
    }
    return handler;
  }

  public void generateClasses() {
    for (ComponentClass cc : componentSuite.getComponentClassesByType(ComponentType.EXML)) {
      XmlToJsonHandler handler= createHandlerFromClass(cc);

      if (handler != null) {
        cc.setSuperClassName(handler.getSuperClassName());
        cc.setDescription(handler.getComponentDescription());
        cc.setImports(handler.getImports());
        cc.setJson(handler.getJsonAsString());
        cc.setCfgs(handler.getCfgs());
        File outputFile = new File(componentSuite.getAs3OutputDir(), XML_TO_JS_MAPPER.mapFileName(cc.getRelativeSrcFilePath()));
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();
        FileWriter writer = null;
        try {
          writer = new FileWriter(outputFile);
          generateJangarooClass(cc, writer);
        } catch (IOException e) {
          errorHandler.error("Exception while creating class", e);
        } catch (TemplateException e) {
          errorHandler.error("Exception while creating class", e);
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
  }
}
