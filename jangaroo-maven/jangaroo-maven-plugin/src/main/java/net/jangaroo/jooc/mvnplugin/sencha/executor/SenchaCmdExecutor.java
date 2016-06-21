package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class SenchaCmdExecutor {

  static final long MAX_EXECUTION_TIME = ExecuteWatchdog.INFINITE_TIMEOUT;

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
      log.info(String.format("Executing Sencha Cmd '%s'", line));
      internalExecute(cmdLine);
      log.debug("Executed Sencha Cmd successfully");
    } catch (IOException e) {
      throw new MojoExecutionException("Execution of Sencha Cmd failed.", e);
    }
  }

  private void internalExecute(CommandLine cmdLine) throws IOException {
    Executor executor = getExecutor();
    ExecuteWatchdog watchdog = getExecuteWatchdog();
    executor.setWatchdog(watchdog);
    executor.setWorkingDirectory(workingDirectory);
    // set allowed exit values (0 is actually the default)
    executor.setExitValue(0);

    SenchaCmdLogOutputStream logOutputStream = new SenchaCmdLogOutputStream(log);
    PumpStreamHandler psh = new PumpStreamHandler(logOutputStream);
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
  protected CommandLine getCommandLine(String line) {
    return CommandLine.parse(line);
  }

  protected Executor getExecutor() {
    return new DefaultExecutor();
  }

  protected ExecuteWatchdog getExecuteWatchdog() {
    return new ExecuteWatchdog(MAX_EXECUTION_TIME);
  }

}
