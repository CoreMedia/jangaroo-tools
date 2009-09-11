package net.jangaroo.extxml;

import freemarker.template.TemplateException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * A tool to define Ext JS Component suites in JavaScript, ActionScript, or XML.
 * <p>Usage: extxml <i>target-namespace xsd-output-file src-dir output-dir imported-xsd-file*</i>
 * <p>Scans the given source directory (src-dir) for Ext JS component class definitions in any of the following three formats:
 * <ul>
 * <li>as a JavaScript file (*.js), using Ext's class and inheritance helpers,
 * <li>as an ActionScript file (*.as), which is then translated to JavaScript by the Jangaroo compiler (jooc), or
 * <li>as an XML file (*.xml), which is first transformed to an ActionScript file and then compiled by jooc.
 * </ul>
 * <p>All used or extended components from other component suites must be imported. This is done
 * by enumeration their XML Schema file locations.
 * <p>All JavaScript files must contain the following documentation annotations in order to be
 * interpreted as an Ext JS component class:
 * <ul>
 * <li><code>&#064;class</code> - the fully qualified name of the JavaScript component class.
 * <li><code>&#064;extends</code> - the fully qualified name of the super component class.
 * <li><code>&#064;xtype</code> - the xtype under which to register the component class with Ext.
 * </ul>
 * <p>Furthermore, both JavaScript and ActionScript source files may define an arbitrary number
 * of <em>configuration parameters</em>, using the following documentation annotation syntax:
 * <p><code>&#064;cfg {<i>type</i>} <i>name description</i></code>
 * <p>The tool generates
 * <ul>
 * <li>ActionScript files for all XML files,
 * <li>TODO: JavaScript files for all ActionScript files (by invoking jooc),
 * <li>an XML Schema (*.xsd, given as xsd-output-file) describing the whole component suite, i.e.
 * an element, type and super type for each component, and an attribute for each configuration parameter.
 * </ul>
 */
public class ExtXml {
  public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TemplateException {

    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    ComponentSuite suite = new ComponentSuite(args[0], args[1], new File(args[3]), new File(args[4]));
    for (int i = 5; i < args.length; i++) {
      InputStream in = null;
      try {
        in = new FileInputStream(new File(args[i]));
        suite.addImportedComponentSuite(XsdScanner.scan(in));
      } finally {
        if(in != null)
          in.close();
      }
    }
    suite.addImportedComponentSuite(XsdScanner.getExt3ComponentSuite());
    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();

    //Generate JSON out of the xml compontents, complete the data in those ComponentClasses
    JooClassGenerator generator = new JooClassGenerator(suite, new StandardOutErrorHandler());
    generator.generateClasses();

    System.out.println(suite);

    //generate the XSD for that
    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(args[2])), "UTF-8"));

    new XsdGenerator(suite).generateXsd(out);
    out.close();
  }
}