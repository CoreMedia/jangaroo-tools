package net.jangaroo.extxml;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.File;

/**
 * Ext JS Component JavaScript to XML Schema (xsd) transformer.
 * <p>Usage: src2xsd <i>targetNamespace</i> <i>xsd-file-name</i> <i>js-src-directory</i>
 */
public class Src2Xsd {
  public static void main(String[] args) throws IOException, TemplateException {
    SrcFileScanner fileScanner = new SrcFileScanner(new ComponentSuite(args[0], new File(args[1]), new File(args[2])));
    fileScanner.scan();
    System.out.println(fileScanner.getComponentSuite());
    new XsdGenerator(fileScanner.getComponentSuite()).generateXsd();
  }
}
