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
public class XsdGenerator {

  private static Configuration cfg;

  public XsdGenerator(ComponentSuite componentSuite) throws IOException {
    /* Create and adjust freemarker configuration */
    cfg = new Configuration();
    cfg.setClassForTemplateLoading(XsdGenerator.class, "/net/jangaroo/extxml/templates");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setOutputEncoding("UTF-8");
    this.componentSuite = componentSuite;
  }

  public void generateXsd(Writer out) throws IOException, TemplateException {
    /* Get or create a template */
    Template template = cfg.getTemplate("component-suite-xsd.ftl");

    /* Merge data-model with template */
    System.out.println(String.format("Writing XML Schema %s ", componentSuite.getNamespace()));
    template.process(componentSuite, out);
  }

  private ComponentSuite componentSuite;
}
