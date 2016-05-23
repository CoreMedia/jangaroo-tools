package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SenchaCmdExecutor {

  static final long MAX_EXECUTION_TIME = ExecuteWatchdog.INFINITE_TIMEOUT;

  private File workingDirectory;
  private String arguments;
  private final Log log;

  public SenchaCmdExecutor(File workingDirectory, String arguments, Log log) {
    this.workingDirectory = workingDirectory;
    this.arguments = arguments;
    this.log = log;
  }

  public void execute() throws MojoExecutionException {
    String line = "sencha";
    if (!StringUtils.isEmpty(arguments)) {
      line += " " + arguments;
    }

    try {
      CommandLine cmdLine = getCommandLine(line);
      log.debug(String.format("Executing Sencha Cmd '%s'", line));
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
    executor.setExitValue(0);
    executor.execute(cmdLine);

    // ExecuteStreamHandler streamHandler = executor.getStreamHandler();
    // streamHandler.setProcessOutputStream(new BufferedInoutStream()

    if (watchdog.killedProcess()) {
      throw new ExecuteException(String.format("Watchdog killed Sencha Cmd process after %s ms.", MAX_EXECUTION_TIME), 0);
    }
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
