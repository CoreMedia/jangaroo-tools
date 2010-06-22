package net.jangaroo.jooc.config;

import net.jangaroo.jooc.Jooc;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the jooc command line to produce a {@link JoocConfiguration}.
 */
public class JoocCommandLineParser {

  public static class CommandLineParseException extends Exception {
    private int exitCode;

    public CommandLineParseException(String message, int exitCode) {
      super(message);
      this.exitCode = exitCode;
    }

    public int getExitCode() {
      return exitCode;
    }
  }

  @SuppressWarnings({"AccessStaticViaInstance"})
  public JoocConfiguration parse(String[] argv) throws CommandLineParseException {
    JoocConfiguration config = new JoocConfiguration();

    Option help = new Option("help", "print this message");
    Option version = OptionBuilder
        .withDescription("print version information and exit")
        .create("version");
    Option verboseOption = OptionBuilder.withLongOpt("verbose")
        .withDescription("be extra verbose")
        .create("v");
    Option debugOption = OptionBuilder.withDescription("generate debugging information " +
        "(possible modes: source, lines, none)")
        .hasOptionalArgs()
        .withArgName("mode")
        .create("g");
    Option destinationDir = OptionBuilder.withArgName("dir")
        .hasArg()
        .withDescription("destination directory for generated JavaScript files")
        .create("d");
    Option sourcePath = OptionBuilder.withArgName("path")
        .hasArg()
        .withDescription("source root directories, separated by the system dependant path separator character (e.g. ':' on Unix systems, ';' on Windows")
        .create("sourcepath");
    Option classPath = OptionBuilder.withArgName("path")
        .hasArg()
        .withDescription("source root directories or jangaroo jars of dependent classes, separated by the system dependant path separator character (e.g. ':' on Unix systems, ';' on Windows")
        .create("classpath");
    Option enableAssertionsOption = OptionBuilder.withLongOpt("enableassertions")
        .withDescription("enable assertions")
        .create("ea");
    Option apiDestinationDir = OptionBuilder.withLongOpt("apiDir")
        .withDescription("destination directory where to generate ActionScript API stubs")
        .hasArg()
        .create("api");
    Option allowDuplicateLocalVariablesOption = OptionBuilder.withLongOpt("allowduplicatelocalvariables")
        .withDescription("allow multiple declarations of local variables")
        .create("ad");
    Option enableGuessingOption = OptionBuilder.withDescription(
        "Enable heuristic for guessing member access ('members'), classes in scope ('classes'), and type casts ('typecasts').")
        .withLongOpt("enableguessing")
        .hasOptionalArgs()
        .withArgName("mode")
        .create("eg");
    Options options = new Options();
    options.addOption(help);
    options.addOption(version);
    options.addOption(verboseOption);
    options.addOption(debugOption);
    options.addOption(destinationDir);
    options.addOption(sourcePath);
    options.addOption(classPath);
    options.addOption(enableAssertionsOption);
    options.addOption(apiDestinationDir);
    options.addOption(allowDuplicateLocalVariablesOption);
    options.addOption(enableGuessingOption);
    CommandLineParser parser = new GnuParser();
    CommandLine line = null;

    try {
      line = parser.parse(options, argv);
    } catch (UnrecognizedOptionException e) {
      throw new CommandLineParseException(e.getMessage(), Jooc.RESULT_CODE_UNRECOGNIZED_OPTION);
    } catch (MissingArgumentException e) {
      throw new CommandLineParseException(e.getMessage(), Jooc.RESULT_CODE_MISSING_OPTION_ARGUMENT);
    } catch (ParseException e) {
      throw new CommandLineParseException(e.getMessage(), Jooc.RESULT_CODE_UNRECOGNIZED_OPTION);
    }

    if (line.hasOption("help")) {
      printHelp(options);
      return null;
    }

    config.setVersion(line.hasOption("version"));
    config.setVerbose(line.hasOption(verboseOption.getOpt()));

    if (line.hasOption(destinationDir.getOpt())) {
      String destinationDirName = line.getOptionValue(destinationDir.getOpt()).trim();
      File destDir = new File(destinationDirName);
      if (!destDir.exists()) {
        throw new IllegalArgumentException("destination directory does not exist: " + destDir.getAbsolutePath());
      }
      config.setOutputDirectory(destDir);
    }

    List<File> sp = parsePath(line, sourcePath);
    List<File> cp = parsePath(line, classPath);

   if (sp != null) {
      config.setSourcePath(sp);
    }
    if (sp != null) {
      config.setClassPath(cp);
    }
    if (line.hasOption(enableAssertionsOption.getOpt())) {
      config.setEnableAssertions(true);
    }
    if (line.hasOption(apiDestinationDir.getOpt())) {
      String destinationDirName = line.getOptionValue(apiDestinationDir.getOpt()).trim();
      File destDir = new File(destinationDirName);
      if (!destDir.exists()) {
        throw new IllegalArgumentException("destination directory for API stubs does not exist: " + destDir.getAbsolutePath());
      }
      config.setApiOutputDirectory(destDir);
    }
    if (line.hasOption(allowDuplicateLocalVariablesOption.getOpt())) {
      config.setAllowDuplicateLocalVariables(true);
    }
    if (line.hasOption(debugOption.getOpt())) {
      String[] values = line.getOptionValues(debugOption.getOpt());
      config.setDebug(true);
      if (values == null || values.length == 0) {
        config.setDebugLines(true);
        config.setDebugSource(true);
      } else {
        for (String value : values) {
          if (value.equals("source")) {
            config.setDebugSource(true);
          } else if (value.equals("lines")) {
            config.setDebugLines(true);
          } else if (value.equals("none")) {
            config.setDebug(false);
            config.setDebugSource(false);
            config.setDebugLines(false);
          } else {
            throw new IllegalArgumentException("unknown -g argument: " + value);
          }
        }
      }
    } else {
      config.setDebugSource(false);
      config.setDebugLines(true);
    }
    if (config.isVerbose()) {
      /*
      System.out.println("enableassertions=" +  enableAssertions);
      */
      System.out.println("-genarateapi: " + config.isGenerateApi());
      System.out.println("-g option values:");
      System.out.println("source=" + config.isDebugSource());
      System.out.println("lines=" + config.isDebugLines());
    }

    String[] fileNames = line.getArgs();
    if (fileNames.length == 0) {
      printHelp(options);
      return null;
    }

    for (String fileName : fileNames) {
      config.addSourceFile(fileName);
    }

    return config;
  }

  private List<File> parsePath(final CommandLine line, final Option opt) {
    if (line.hasOption(opt.getOpt())) {
      String sourcePathString = line.getOptionValue(opt.getOpt()).trim();
      if (!sourcePathString.isEmpty()) {
        final String[] sourceDirs = sourcePathString.split("\\Q" + File.pathSeparatorChar + "\\E");
        final List<File> sourcePathFiles = new ArrayList<File>(sourceDirs.length);
        for (String sourceDirPath : sourceDirs) {
          // be tolerant, accept also '/' as file separator
          File sourceDir = new File(sourceDirPath.replace('/', File.separatorChar));
          if (!sourceDir.exists()) {
            throw new IllegalArgumentException("directory or file does not exist: " + sourceDir.getAbsolutePath());
          }
          sourcePathFiles.add(sourceDir);
        }
        return sourcePathFiles;
      }
    }
    return new ArrayList<File>();
  }


  protected void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("jooc [options] <file> ...", options);
  }

}
