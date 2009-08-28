package net.jangaroo.extxml;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Ext JS Component JavaScript to XML Schema (xsd) transformer.
 * <p>Usage: src2xsd <i>targetNamespace</i> <i>xsd-file-name</i> <i>js-src-directory</i>
 */
public class Src2Xsd {
  public static void main(String[] args) throws IOException, TemplateException {
    List<File> importedXsds = new ArrayList<File>(args.length-3);
    for (int i = 3; i < args.length; i++) {
      importedXsds.add(new File(args[i]));
    }
    SrcFileScanner fileScanner = new SrcFileScanner(new ComponentSuite(args[0], new File(args[1]), new File(args[2]), importedXsds));
    fileScanner.scan();
    System.out.println(fileScanner.getComponentSuite());
    new XsdGenerator(fileScanner.getComponentSuite()).generateXsd();
  }
}
