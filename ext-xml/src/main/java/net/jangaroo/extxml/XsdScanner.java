package net.jangaroo.extxml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

/**
 * An XsdScanner parses an Ext XML component declaration schema into a {@link ComponentSuite}.
 */
public class XsdScanner {

  public XsdScanner(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public void scan() {
    //DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    //builder.
    //componentSuite.getXsd()
  }

  private ComponentSuite componentSuite;
}
