package net.jangaroo.utils;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.StringWriter;

import static org.kohsuke.args4j.ExampleMode.ALL;

public abstract class AbstractCommandLineParser<T extends FileLocations> {

  public abstract String getMain();

  public abstract T newT();

  public abstract T parseConfig(CmdLineParser parser, T config);

  public StringBuilder extendedUsage(CmdLineParser parser, CmdLineException e) {
    StringBuilder msg = new StringBuilder();
    // if there's a problem in the command line,
    // you'll get this exception. this will report
    // an error message.
    if (e != null) {
      msg.append(e.getMessage());
      msg.append("\n");
    }
    msg.append("java ").append(getMain()).append(" [options...] source files...\n");
    // print the list of available options
    StringWriter writer = new StringWriter();
    parser.printUsage(writer, null);
    msg.append(writer.getBuffer());
    msg.append("\n");
    // print option sample. This is useful some time
    msg.append("  Example: java ").append(getMain()).append(parser.printExample(ALL));
    msg.append("\n");
    return msg;
  }

  public T parse(String[] args) throws CommandLineParseException {
    T config = newT();

    CmdLineParser parser = new CmdLineParser(config);
    try {
      // parse the arguments.
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      StringBuilder msg = extendedUsage(parser, e);
      throw new CommandLineParseException(msg.toString(), -1);
    }
    return parseConfig(parser, config);
  }

}
