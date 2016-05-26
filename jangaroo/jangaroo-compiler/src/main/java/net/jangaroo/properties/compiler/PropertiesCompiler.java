package net.jangaroo.properties.compiler;/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.api.PropertiesCompilerConfiguration;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.StringWriter;

import static org.kohsuke.args4j.ExampleMode.REQUIRED;

public final class PropertiesCompiler {
  private PropertiesCompiler() {

  }

  public static int run(String[] args){

    PropertiesCompilerConfiguration config = new PropertiesCompilerConfiguration();

    CmdLineParser parser = new CmdLineParser(config);
    try {
      // parse the arguments.
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      StringBuilder msg = new StringBuilder();
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      msg.append(e.getMessage());
      msg.append("\n");
      msg.append("java -jar properties-compiler.jar [options...] source files...\n");
      // print the list of available options
      StringWriter writer = new StringWriter();
      parser.printUsage(writer, null);
      msg.append(writer.getBuffer());
      msg.append("\n");
      // print option sample. This is useful some time
      msg.append("  Example: java -jar properties-compiler.jar").append(parser.printExample(REQUIRED));
      msg.append("\n");
      System.err.println(msg); // NOSONAR this is a commandline tool
      return -1;
    }

    if (!config.getOutputDirectory().exists()) {
      throw new IllegalArgumentException("destination directory does not exist: " + config.getOutputDirectory().getAbsolutePath());
    }

    PropertyClassGenerator generator = new PropertyClassGenerator(config);
    generator.generate();
    
    return 0;
  }

  public static void main(String[] argv)  {
    int result = run(argv);
    if (result != 0) {
      System.exit(result);
    }
  }
}
