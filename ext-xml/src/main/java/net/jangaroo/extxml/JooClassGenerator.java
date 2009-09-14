package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.shared.model.fileset.mappers.FileNameMapper;
import org.apache.maven.shared.model.fileset.mappers.GlobPatternMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;


public class JooClassGenerator {

  private static FileNameMapper XML_TO_JS_MAPPER = new GlobPatternMapper();

  static {
    XML_TO_JS_MAPPER.setFrom("*." + ComponentType.EXML.extension);
    XML_TO_JS_MAPPER.setTo("*." + ComponentType.ActionScript.extension);
  }

  ErrorHandler errorHandler;
  ComponentSuite componentSuite;

  public JooClassGenerator(ComponentSuite componentSuite, ErrorHandler handler) {
    this.errorHandler = handler;
    this.componentSuite = componentSuite;
  }


  public void generateJangarooClass(ComponentClass jooClass, Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ComponentClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());

    Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/jangaroo_class.ftl");

    template.process(jooClass, output);
  }

  public void generateClasses() {
    for (ComponentClass cc : componentSuite.getComponentClassesByType(ComponentType.EXML)) {
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
          inputStream.close();
        } catch (IOException e) {
          //never happend
        }
      }

      if (handler != null) {
        cc.setSuperClassName(handler.getSuperClassName());
        cc.setImports(handler.getImports());
        cc.setJson(handler.getJsonAsString());
        cc.setCfgs(handler.getCfgs());
        File outputFile = new File(componentSuite.getAs3OutputDir(), XML_TO_JS_MAPPER.mapFileName(cc.getRelativeSrcFilePath()));
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
            writer.close();
          } catch (IOException e) {
            //never happen
          }
        }

      }
    }

  }
}
