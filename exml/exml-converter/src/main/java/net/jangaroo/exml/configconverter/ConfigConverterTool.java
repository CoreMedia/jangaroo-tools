package net.jangaroo.exml.configconverter;

import net.jangaroo.exml.configconverter.file.SrcFileScanner;
import net.jangaroo.exml.configconverter.generation.ConfigClassGenerator;
import net.jangaroo.exml.configconverter.model.ComponentSuite;
import net.jangaroo.exml.configconverter.xml.XsdScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class ConfigConverterTool {

  private File sourceRoot;
  private File outputDir;
  private String newConfigPackage;
  private ComponentSuiteRegistry componentSuiteRegistry;
  private XsdScanner scanner;

  public ConfigConverterTool(File sourceRoot, File outputDir, String newConfigPackage) {
    this.sourceRoot = sourceRoot;
    this.outputDir = outputDir;
    this.newConfigPackage = newConfigPackage;

    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    componentSuiteRegistry = new ComponentSuiteRegistry();
    scanner = new XsdScanner(componentSuiteRegistry);
  }

  public void addModule(File xsd, String packageName) throws IOException {
    ComponentSuite componentSuite = scanner.scan(new FileInputStream(xsd));
    componentSuite.setConfigClassPackage(packageName);
    componentSuiteRegistry.add(componentSuite);
  }


  public void convertAll() throws IOException {
    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    ComponentSuite suite = new ComponentSuite(componentSuiteRegistry, "ignored", "ignored", sourceRoot, outputDir, newConfigPackage);

    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();

    //Generate config classes out of the AS components
    ConfigClassGenerator generator = new ConfigClassGenerator(suite);
    generator.generateClasses();
  }
}
