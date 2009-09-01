package net.jangaroo.extxml;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import net.sf.saxon.s9api.SaxonApiException;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;

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
  public static void main(String[] args) throws IOException, TemplateException, SaxonApiException, SAXException, XPathExpressionException, ParserConfigurationException {
    List<File> importedXsds = new ArrayList<File>(args.length - 3);
    for (int i = 3; i < args.length; i++) {
      importedXsds.add(new File(args[i]));
    }
    ComponentSuite suite = new ComponentSuite(args[0], new File(args[1]), new File(args[2]), importedXsds, new File(args[4]));
    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();
    JooClassGenerator generator = new JooClassGenerator(suite);
    generator.generateClasses();
    System.out.println(suite);
    new XsdGenerator(suite).generateXsd();
  }
}