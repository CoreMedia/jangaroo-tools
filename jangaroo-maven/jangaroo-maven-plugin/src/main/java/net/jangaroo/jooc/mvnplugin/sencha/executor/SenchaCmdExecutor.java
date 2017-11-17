package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SenchaCmdExecutor {

  private static final long MAX_EXECUTION_TIME = ExecuteWatchdog.INFINITE_TIMEOUT;
  private static final Pattern SENCHA_CMD_VERSION_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+){2,3}");

  @Nonnull
  public static int[] queryVersion() throws IOException {
    SenchaVersionParser senchaVersionParser = new SenchaVersionParser();
    internalExecute(CommandLine.parse("sencha switch -l"), senchaVersionParser, null);
    if (senchaVersionParser.versions == null) {
      throw new IOException("No 'Current version' found in output of 'sencha switch -l'.");
    }
    return senchaVersionParser.versions;
  }

  /**
   * Parse first line or preferably, if a line containing "current version" appears, the line after that.
   */
  private static class SenchaVersionParser extends LogOutputStream {

    private int[] versions;
    private boolean parseNext = true;

    @Override
    protected void processLine(String line, int level) {
      if (parseNext) {
        int[] parsedVersions = parseVersion(line);
        if (parsedVersions != null) {
          versions = parsedVersions;
          parseNext = false;
        }
      }
      if (!parseNext && line.toLowerCase().contains("current version")) {
        parseNext = true;
      }
    }
  }

  static int[] parseVersion(String versionString) {
    Matcher matcher = SENCHA_CMD_VERSION_PATTERN.matcher(versionString);
    if (matcher.find()) {
      try {
        return Arrays.stream(StringUtils.split(matcher.group(), "."))
                .mapToInt(Integer::parseInt)
                .toArray();
      } catch (NumberFormatException e) {
        // handle below
      }
    }
    return null;
  }

  private File workingDirectory;
  private String arguments;
  private final Log log;
  private String senchaLogLevel;

  public SenchaCmdExecutor(File workingDirectory, String arguments, Log log, String senchaLogLevel) {
    this.workingDirectory = workingDirectory;
    this.arguments = arguments;
    this.log = log;
    this.senchaLogLevel = senchaLogLevel;
  }

  public void execute() throws MojoExecutionException {
    String line = "sencha --time " + getSenchaLogLevelCmd();

    if (!StringUtils.isEmpty(arguments)) {
      line += " " + arguments;
    }

    try {
      CommandLine cmdLine = getCommandLine(line);
      log.info(String.format("Executing Sencha Cmd '%s' in directory '%s'", line, workingDirectory));
      internalExecute(cmdLine, new SenchaCmdLogOutputStream(log), workingDirectory);
      log.debug("Executed Sencha Cmd successfully");
    } catch (IOException e) {
      throw new MojoExecutionException("Execution of Sencha Cmd failed.", e);
    }
  }

  private static void internalExecute(CommandLine cmdLine, OutputStream outputStream, File workingDirectory) throws IOException {
    Executor executor = getExecutor();
    ExecuteWatchdog watchdog = getExecuteWatchdog();
    executor.setWatchdog(watchdog);
    if (workingDirectory != null) {
      executor.setWorkingDirectory(workingDirectory);
    }
    // set allowed exit values (0 is actually the default)
    executor.setExitValue(0);
    PumpStreamHandler psh = new PumpStreamHandler(outputStream);
    executor.setStreamHandler(psh);
    executor.execute(cmdLine);

    if (watchdog.killedProcess()) {
      throw new ExecuteException(String.format("Watchdog killed Sencha Cmd process after %s ms.", MAX_EXECUTION_TIME), 0);
    }
  }

  private String getSenchaLogLevelCmd() {

    String logLevelToUse = senchaLogLevel != null ? senchaLogLevel : getMavenLogLevel();

    String result = "--info";
    // add logging
    if ("debug".equalsIgnoreCase(logLevelToUse)) {
      result = "--debug";
    } else if ("trace".equalsIgnoreCase(logLevelToUse)) {
      result = "--trace";
    }   else if ("warn".equalsIgnoreCase(logLevelToUse)) {
      result = "--quiet";
    }

    return result;
  }

  private  String getMavenLogLevel() {
    String result = "error";
    if (log.isDebugEnabled()) {
      result = "debug";
    } else if (log.isInfoEnabled()) {
      result = "info";
    } else if (log.isWarnEnabled()) {
      result = "warn";
    }
    return result;
  }

  /**
   * Extracted for testing...
   */
  private static CommandLine getCommandLine(String line) {
    return CommandLine.parse(line);
  }

  private static Executor getExecutor() {
    return new DefaultExecutor();
  }

  private static ExecuteWatchdog getExecuteWatchdog() {
    return new ExecuteWatchdog(MAX_EXECUTION_TIME);
  }

}
