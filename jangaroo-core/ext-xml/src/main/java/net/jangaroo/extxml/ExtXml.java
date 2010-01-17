package net.jangaroo.extxml;

import net.jangaroo.extxml.file.SrcFileScanner;
import net.jangaroo.extxml.generation.JooClassGenerator;
import net.jangaroo.extxml.generation.XsdGenerator;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.xml.XsdScanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * A tool to define Ext JS Component suites in JavaScript, ActionScript, or XML (files with exml ending).
 * <p>Usage: extxml <i>target-namespace namespace-prefix xsd-output-file src-dir output-dir imported-xsd-file*</i>
 * <p>Scans the given source directory (src-dir) for Ext JS component class definitions in any of the following three formats:
 * <ul>
 * <li>as a JavaScript file (*.js), using Ext's class and inheritance helpers,
 * <li>as an ActionScript file (*.as), or
 * <li>as an XML file (*.exml), which is first transformed to an ActionScript file.
 * </ul>
 * <p>All used or extended components from other component suites must be imported. This is done
 * by enumeration their XML Schema file locations.
 * <p>All ActionScipt files have to comply with the following structure:
 * <ul>
 * <li>extend an ExtXml Component, i.e. ext.Panel</li>
 * <li>define a public static const xtype, i.e. <i>public static const xtype:String = "myComponent";</i>
 * <li>register the compontent in a static code block as Ext component, i.e
 * <code>
 * { ext.ComponentMgr.registerType(xtype, MyComponent); }
 * </code>
 * </li>
 * </ul>
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
 * <li>an XML Schema (*.xsd, given as xsd-output-file) describing the whole component suite, i.e.
 * an element, type and super type for each component, and an attribute for each configuration parameter.
 * </ul>
 */
public final class ExtXml {

  private ExtXml () {

  }

  public static void main(String[] args) throws IOException {

    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();

    XsdScanner scanner = new XsdScanner();

    for (int i = 5; i < args.length; i++) {
      componentSuiteRegistry.add(scanner.scan(new FileInputStream(new File(args[i]))));
    }

    ComponentSuite suite = new ComponentSuite(args[0], args[1], new File(args[3]), new File(args[4]));
    
    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();

    //Generate JSON out of the xml components, complete the data in those ComponentClasses
    JooClassGenerator generator = new JooClassGenerator(suite);
    generator.generateClasses();

    System.out.println(suite);

    //generate the XSD for that
    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(args[2])), "UTF-8"));

    new XsdGenerator(suite).generateXsd(out);
    out.close();
  }
}