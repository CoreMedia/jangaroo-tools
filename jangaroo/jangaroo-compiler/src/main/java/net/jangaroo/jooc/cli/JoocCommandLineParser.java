package net.jangaroo.jooc.cli;

import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.utils.AbstractCommandLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Parses the jooc command line to produce a {@link JoocConfiguration}.
 */
public class JoocCommandLineParser extends AbstractCommandLineParser {


  private void printVersion() {
    String pkgName = "net.jangaroo.jooc";
    Package pkg = Package.getPackage(pkgName);
    String specTitle = pkg.getSpecificationTitle();
    if (specTitle == null) {
      System.out.println("cannot retrieve package version information for " + pkgName); // NOSONAR this is a commandline tool
      return;
    }
    String specVendor = pkg.getSpecificationVendor();
    String specVersion = pkg.getSpecificationVersion();
    String implTitle = pkg.getImplementationTitle();
    String implVersion = pkg.getImplementationVersion();
    System.out.println(specTitle + " version " + specVersion); // NOSONAR this is a cmd line tool
    System.out.println(implTitle + " (build " + implVersion + ")"); // NOSONAR this is a cmd line tool
    System.out.println(specVendor); // NOSONAR this is a cmd line tool
  }

  @Override
  public String getShellScriptName() {
    return "jooc";
  }

  public JoocConfiguration parseConfig(CmdLineParser parser, JoocConfiguration config) {
    if (config.isHelp()) {
      System.out.println(extendedUsage(parser, null)); // NOSONAR this is a cmd line tool
      return null;
    }

    if (config.isVersion()) {
      printVersion();
      return null;
    }

    if (config.getOutputDirectory() == null) {
      System.out.println(extendedUsage(parser, null));  // NOSONAR this is a cmd line tool
      return null;
    }

    if (!config.getOutputDirectory().exists()) {
      throw new IllegalArgumentException("destination directory does not exist: " + config.getOutputDirectory().getAbsolutePath());
    }

    if (config.getApiOutputDirectory() != null && !config.getApiOutputDirectory().exists()) {
      throw new IllegalArgumentException("destination directory for API stubs does not exist: " + config.getApiOutputDirectory().getAbsolutePath());
    }

    if (config.isVerbose()) {
      /*
      System.out.println("enableassertions=" +  enableAssertions);
      */
      System.out.println("-genarateapi: " + config.isGenerateApi()); // NOSONAR this is a cmd line tool
      System.out.println("-g option values:"); // NOSONAR this is a cmd line tool
      System.out.println("debugMode=" + config.getDebugMode()); // NOSONAR this is a cmd line tool
    }

    return config;
  }

  public JoocConfiguration parse(String[] args) throws CommandLineParseException {
    JoocConfiguration config = new JoocConfiguration();

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
