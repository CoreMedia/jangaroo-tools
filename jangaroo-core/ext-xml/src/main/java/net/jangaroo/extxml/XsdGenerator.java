package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

/**
 * An XsdGenerator takes a {@link ComponentSuite} and (re)generates its XML Schema (.xsd) file.
 */
public final class XsdGenerator {

  private static Configuration cfg = new Configuration();
  static {
    /* Create and adjust freemarker configuration */
    cfg.setClassForTemplateLoading(XsdGenerator.class, "/net/jangaroo/extxml/templates");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
  }

  private ComponentSuite componentSuite;

  /**
   * 
   * @param componentSuite
   */
  public XsdGenerator(ComponentSuite componentSuite) {    
    this.componentSuite = componentSuite;
  }

  public void generateXsd(Writer out) throws IOException {
    //do nothing if suite is empty
    if (!componentSuite.getComponentClasses().isEmpty()) {
      /* Get or create a template */
      Template template = null;
      try {
        template = cfg.getTemplate("component-suite-xsd.ftl");
      } catch (IOException e) {
        Log.getErrorHandler().error("Could not read xsd template", e);
      }

      /* Merge data-model with template */
      if (template != null) {
        Log.getErrorHandler().info(String.format("Writing XML Schema %s ", componentSuite.getNamespace()));
        try {
          template.process(componentSuite, out);
        } catch (TemplateException e) {
          Log.getErrorHandler().error("Error while generating xsd", e);
        }
      }
    }
  }
}
