package net.jangaroo.exml.cli;


import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.jooc.cli.CommandLineParseException;
import net.jangaroo.utils.AbstractCommandLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 *
 */
public class ExmlcCommandLineParser extends AbstractCommandLineParser {

  @Override
  public String getShellScriptName() {
    return "exmlc";
  }

  public ExmlConfiguration parseConfig(CmdLineParser parser, ExmlConfiguration config) {
    if (config.getOutputDirectory() == null) {
      System.out.println(extendedUsage(parser, null)); // NOSONAR this is a cmd line tool
      return null;
    }

    if (!config.getOutputDirectory().exists()) {
      throw new IllegalArgumentException("destination directory does not exist: " + config.getOutputDirectory().getAbsolutePath());
    }

    if (config.getResourceOutputDirectory() == null) {
      config.setResourceOutputDirectory(config.getOutputDirectory());
    }
    return config;
  }

  public ExmlConfiguration parse(String[] args) throws CommandLineParseException {
    ExmlConfiguration config = new ExmlConfiguration();

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
