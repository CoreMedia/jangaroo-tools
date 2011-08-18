package net.jangaroo.extxml;

import net.jangaroo.extxml.file.SrcFileScanner;
import net.jangaroo.extxml.generation.ConfigClassGenerator;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.xml.XsdScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
 * <p>All ActionScript files have to comply with the following structure:
 * <ul>
 * <li>extend an ExtXml Component, i.e. ext.Panel</li>
 * <li>define a public static const xtype, i.e. <i>public static const xtype:String = "myComponent";</i>
 * <li>register the component in a static code block as Ext component, i.e
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
 * <p>The tool generates ActionScript config class files for all AS files.
 */
public final class ExtXml {

  private ExtXml () {

  }

  /**
   * Command line arguments:
   * <ol>
   *   <li>source root dir</li>
   *   <li>target root dir</li>
   *   <li>config package name</li>
   *   <li>any number of XSD files for import</li>
   * </ol>
   * @param args command line arguments
   * @throws IOException
   */
  public static void main(String ... args) throws IOException {
    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    ComponentSuiteRegistry componentSuiteRegistry = new ComponentSuiteRegistry();

    XsdScanner scanner = new XsdScanner();

    for (int i = 3; i < args.length;) {
      ComponentSuite componentSuite = scanner.scan(new FileInputStream(new File(args[i++])));
      componentSuite.setConfigClassPackage(args[i++]);

      componentSuiteRegistry.add(componentSuite);
    }

    ComponentSuite suite = new ComponentSuite(componentSuiteRegistry, "ignored", "ignored", new File(args[0]), new File(args[1]), args[2]);

    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();

    //Generate config classes out of the AS components
    ConfigClassGenerator generator = new ConfigClassGenerator(suite);
    generator.generateClasses();
  }
}