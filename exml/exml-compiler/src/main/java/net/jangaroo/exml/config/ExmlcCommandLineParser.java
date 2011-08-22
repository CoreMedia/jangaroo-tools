package net.jangaroo.exml.config;


import net.jangaroo.jooc.config.CommandLineParseException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.StringWriter;

import static org.kohsuke.args4j.ExampleMode.REQUIRED;

/**
 *
 */
public class ExmlcCommandLineParser {
  
  public ExmlConfiguration parse(String[] args) throws CommandLineParseException {
    ExmlConfiguration config = new ExmlConfiguration();

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
      msg.append("java Exmlc [options...] source files...\n");
      // print the list of available options
      StringWriter writer = new StringWriter();
      parser.printUsage(writer, null);
      msg.append(writer.getBuffer());
      msg.append("\n");
      // print option sample. This is useful some time
      msg.append("  Example: java Exmlc").append(parser.printExample(REQUIRED));
      msg.append("\n");
      throw new CommandLineParseException(msg.toString(), -1);
    }

    if (!config.getOutputDirectory().exists()) {
      throw new IllegalArgumentException("destination directory does not exist: " + config.getOutputDirectory().getAbsolutePath());
    }

    if(config.getResourceOutputDirectory() == null) {
      config.setResourceOutputDirectory(config.getOutputDirectory());
    }

    return config;
  }
}
