package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

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

    this.componentSuite = componentSuite;
  }

  public void generateXsd() throws IOException, TemplateException {
    /* Get or create a template */
    Template template = cfg.getTemplate("component-suite-xsd.ftl");

    /* Merge data-model with template */
    Writer out = new FileWriter(componentSuite.getXsd());
    System.out.println(MessageFormat.format("Writing XML Schema {0} to file {1}", componentSuite.getNamespace(), componentSuite.getXsd().getAbsolutePath()));
    template.process(componentSuite, out);
    out.close();
  }

  private ComponentSuite componentSuite;
}
