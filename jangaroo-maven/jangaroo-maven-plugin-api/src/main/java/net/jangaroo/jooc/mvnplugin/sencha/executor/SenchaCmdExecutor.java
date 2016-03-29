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
  static final int MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS = 5;

  private File workingDirectory;
  private String arguments;
  private final Log log;

  public SenchaCmdExecutor(File workingDirectory, String arguments, Log log) {
    this.workingDirectory = workingDirectory;
    this.arguments = arguments;
    this.log = log;
  }

  public void execute() throws MojoExecutionException {
    int currentAttempt = 1;
    String line = "sencha";
    if (!StringUtils.isEmpty(arguments)) {
      line += " " + arguments;
    }

    try {
      CommandLine cmdLine = getCommandLine(line);
      do {
        log.debug(String.format("Executing Sencha Cmd '%s'", line));
        try {
          internalExecute(cmdLine);
        } catch (RecoverableExecuteException e) {
          if (currentAttempt >= MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS) {
            log.error("Execution of Sencha Cmd failed.");
            throw e;
          }
          currentAttempt++;
          log.debug(String.format("Execution failed. Trying again - Attempt: %s of %s", currentAttempt, MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS));
          continue;
        }
        break;
      } while (currentAttempt <= MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS);
      log.debug("Executed Sencha Cmd successfully");
    } catch (IOException e) {
      throw new MojoExecutionException("Execution of Sencha Cmd failed.", e);
    }
  }

  private void internalExecute(CommandLine cmdLine) throws IOException {
    Executor executor = getExecutor();
    ExecuteWatchdog watchdog = getExecuteWatchdog();
    executor.setWatchdog(watchdog);
    SenchaCmdLogOutputStream outputStream = getSenchaCmdLogOutputStream(watchdog);
    PumpStreamHandler streamHandler = getPumpStreamHandler(outputStream);
    executor.setStreamHandler(streamHandler);
    executor.setWorkingDirectory(workingDirectory);
    executor.setExitValue(0);
    executor.execute(cmdLine);
    if (null != outputStream.getExecuteException()) {
      throw outputStream.getExecuteException();
    }
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

  protected SenchaCmdLogOutputStream getSenchaCmdLogOutputStream(ExecuteWatchdog watchdog) {
    return new SenchaCmdLogOutputStream(watchdog, log);
  }

  protected PumpStreamHandler getPumpStreamHandler(SenchaCmdLogOutputStream outputStream) {
    return new PumpStreamHandler(outputStream);
  }
}
