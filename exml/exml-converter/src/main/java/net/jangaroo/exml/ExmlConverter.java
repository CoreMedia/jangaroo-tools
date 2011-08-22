package net.jangaroo.exml;

import net.jangaroo.exml.exmlconverter.ExmlConverterTool;
import net.jangaroo.exml.file.SrcFileScanner;
import net.jangaroo.exml.generation.ConfigClassGenerator;
import net.jangaroo.exml.model.ComponentSuite;
import net.jangaroo.exml.xml.XsdScanner;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.kohsuke.args4j.ExampleMode.REQUIRED;

/**
 * Converter tool for ActionScript component classes based on Jangaroo 0.8.4 that should be
 * converted to the new ConfigClass style of Jangaroo 0.8.5.
 * To run the tool, the module, you try to convert has to compile and fully work with Jangaroo 0.8.4. The tool does not rewrite
 * your classes but generates new one.
 */
public class ExmlConverter {

  @Option(name = "-m",
          aliases = {"-module"},
          usage = "Maven module root folder that should be converted",
          metaVar = "MODULE_DIRECTORY",
          required = true)
  private File moduleRoot = null;

  @Option(name = "-e", usage = "the optional character encoding of the EXML files; " +
          "defaults to UTF-8", metaVar = "ENCODING")
  private String encoding = "UTF-8";

  @Option(name = "-p",
          aliases = {"-mapping"},
          usage = "properties file with mapping of maven module name to config class package. " +
          "Every dependency that provides some Exml components should be added to this file. " +
          "Example: ext3=ext.config",
          metaVar = "MAPPING_FILE",
          required = true)
  private File mappingPropertiesFile = null;

  @Option(name = "-s", usage = "the source path, relative to MODULE_DIRECTORY; defaults to src/main/joo")
  private String sourcePath = "src" + File.separator + "main" + File.separator + "joo" + File.separator;

  @Option(name = "-o",
          usage = "the output directory for the generated config classes. " +
                  "OUTPUT_DIRECTORY defaults to the source folder of the Maven module (src/main/joo)",
          metaVar = "OUTPUT_DIRECTORY")
  private File outputDir;

  @Option(name="-t", aliases = "-test-output", metaVar = "JANGAROO_TEST_OUTPUT_DIR", usage = "the Jangaroo testoutput directory. " +
          "This is typically <module-root>/target/jangaroo-output-test or <module-root>/target/test-classes which will also " +
          "be used if not specified. You have to provide this parameter if you have changed the value of <testOutputDirectory>target/jangaroo-output-test</testOutputDirectory> " +
          "in your module pom.xml.")
  private File moduleJangarooTestOutputDir;


  void exit(int status) {
    System.exit(status);
  }

  public static void main(String[] args) throws IOException {
    new ExmlConverter().run(args);
  }

  void run(String[] args) throws IOException {
    CmdLineParser parser = new CmdLineParser(this);
    try {
      // parse the arguments.
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println();
      printUsage(parser);
      exit(-3);
    }

    if (!moduleRoot.exists()) {
      System.err.println("The maven module root directory '" + args[0] + "' does not exist.");
      System.err.println("Please specify an existing path.");
      exit(-2);
    }

    String moduleName = moduleRoot.getName();


    if (!mappingPropertiesFile.exists()) {
      System.err.println("The mapping file '" + args[1] + "' does not exist.");
      System.err.println("Please specify an existing path.");
      exit(-2);
    }

    FileInputStream stream = null;
    Properties mappings;
    try {
      stream = new FileInputStream(mappingPropertiesFile);
      mappings = new Properties();
      mappings.load(stream);
      mappings.put("ext3", "ext.config");
    } finally {
      if(stream != null) {
        stream.close();
      }
    }

    String configClassPackage = getConfigPackageName(moduleName, mappings);
    
    File moduleSourceRoot = new File(moduleRoot, sourcePath);
    if (!moduleSourceRoot.exists()) {
      System.err.println("Source folder '" + moduleSourceRoot.getAbsolutePath() + "' does not exist.");
      System.err.println("Is this really a Maven module?");
      exit(-2);
    }

    if (outputDir == null) {
      outputDir = moduleSourceRoot;
    }
    if(moduleJangarooTestOutputDir == null ){
      moduleJangarooTestOutputDir = new File(moduleRoot, "target" + File.separator + "jangaroo-output-test" + File.separator);
      if (!moduleJangarooTestOutputDir.exists()) {
        //try the default maven testoutput
        moduleJangarooTestOutputDir = new File(moduleRoot, "target" + File.separator + "test-classes" + File.separator);
        if (!moduleJangarooTestOutputDir.exists()) {
          System.err.println("JANGAROO_TEST_OUTPUT_DIR '" + moduleJangarooTestOutputDir.getAbsolutePath() + "' does not exist.");
          System.err.println("Is this really a Maven module or have you not called mvn install yet?");
          exit(-2);
        }
      }
    }

    if (!moduleJangarooTestOutputDir.exists()) {
      System.err.println("JANGAROO_TEST_OUTPUT_DIR '" + moduleJangarooTestOutputDir.getAbsolutePath() + "' does not exist.");
      System.err.println("Is this really a Maven module or have you not called mvn install yet?");
      exit(-2);
    }

    System.out.println("Converting Maven module: " + moduleName);

    //Scan the directory for xml, as or javascript components and collect the data in ComponentClass, import all provided XSDs
    ComponentSuiteRegistry componentSuiteRegistry = new ComponentSuiteRegistry();
    XsdScanner scanner = new XsdScanner();

    for (String module : mappings.stringPropertyNames()) {
      File xsd = new File(moduleJangarooTestOutputDir, module + ".xsd");
      if (xsd.exists()) {
        ComponentSuite componentSuite = scanner.scan(new FileInputStream(xsd));
        String property = getConfigPackageName(module, mappings);
        componentSuite.setConfigClassPackage(property);
        componentSuiteRegistry.add(componentSuite);
      }
    }

    ComponentSuite suite = new ComponentSuite(componentSuiteRegistry, "ignored", "ignored", moduleSourceRoot, outputDir, configClassPackage);

    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    fileScanner.scan();

    //Generate config classes out of the AS components
    ConfigClassGenerator generator = new ConfigClassGenerator(suite);
    generator.generateClasses();

    System.out.println("\n******************");
    System.out.println("Warning:");
    System.out.println("If you have any actions or plugins that also have been converted, ");
    System.out.println("you have to change the annotation value 'xtype' manually!");
    System.out.println("******************");

    ExmlConverterTool exmlConverter = new ExmlConverterTool(encoding, moduleSourceRoot);
    boolean anyErrors = exmlConverter.convertAll();
    if(anyErrors) {
      System.err.println("Some files could not be processed due to errors.");
      exit(-3);
    }
  }

  private String getConfigPackageName(String moduleName, Properties mappings) {
    String configClassPackage = mappings.getProperty(moduleName);
    if(configClassPackage == null || configClassPackage.length() == 0) {
      System.err.println("No config class package for module '"+moduleName+"' defined!");
      System.err.println("Please add some package name for the module '"+moduleName+"' to the mapping file " + mappingPropertiesFile);
      exit(-2);
    }
    return configClassPackage;
  }

  private void printUsage(CmdLineParser parser) {
    System.err.println("Usage: java -jar exml-converter.jar [options...]");
    System.err.println("Exml Converter that converts Exml Maven modules to the new Exml compiler.");
    System.err.println("Generates config classes from ExtJS components written in old style AS and converts all exml files.");
    System.err.println("Details can be found here: https://github.com/CoreMedia/jangaroo-tools/wiki/Ext-AS-to-config-class-converter");
    System.err.println();
    // print the list of available options
    parser.printUsage(System.err);
    System.err.println();
    // print option sample. This is useful some time
    System.err.println("  Example: java -jar exml-converter.jar" + parser.printExample(REQUIRED));
  }
}