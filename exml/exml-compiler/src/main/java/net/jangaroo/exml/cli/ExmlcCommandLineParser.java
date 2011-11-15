package net.jangaroo.exml.cli;


import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.utils.AbstractCommandLineParser;
import org.kohsuke.args4j.CmdLineParser;

/**
 *
 */
public class ExmlcCommandLineParser extends AbstractCommandLineParser<ExmlConfiguration> {

  @Override
  public String getShellScriptName() {
    return "exmlc";
  }

  @Override
  public ExmlConfiguration newT() {
    return new ExmlConfiguration();
  }

  @Override
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
}
