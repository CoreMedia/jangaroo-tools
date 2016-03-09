package net.jangaroo.jooc.mvnplugin.sencha;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class SenchaCmdExecutor {

  private static final int MAX_EXECUTION_TIME = 300000;
  private static final boolean USE_STRICT = true;
  private static final int MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS = 5;

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
    if (USE_STRICT) {
      line += " --strict";
    }
    if (!StringUtils.isEmpty(arguments)) {
      line += " " + arguments;
    }

    try {
      boolean success;
      CommandLine cmdLine = CommandLine.parse(line);
      do {
        if (currentAttempt > 1) {
          log.info("trying again...");
        }
        log.info("executing sencha cmd - attempt: " + currentAttempt + " of " + MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS);
        success = internalExecute(cmdLine);
        if (!success) {
          log.info("execution of sencha cmd was not successfully because of a bind fail for phantomjs");
          currentAttempt++;
        }
      } while (!success && currentAttempt <= MAX_ATTEMPTS_TO_HANDLE_BIND_FAILS);
      if (!success) {
        log.error("maximum number of attempts to execute sencha cmd was reached");
        throw new MojoExecutionException("execution of sencha cmd was not successful");
      }
      log.info("execution of sencha cmd was successful");
    } catch (IOException e) {
      throw new MojoExecutionException("execution of sencha cmd was not successful", e);
    }
  }

  private boolean internalExecute(CommandLine cmdLine) throws IOException, MojoExecutionException {
    DefaultExecutor executor = new DefaultExecutor();
    executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
    ExecuteWatchdog watchDog = new ExecuteWatchdog(MAX_EXECUTION_TIME);
    executor.setWatchdog(watchDog);
    CollectingLogOutputStream outputStream = new CollectingLogOutputStream(log);
    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
    executor.setStreamHandler(streamHandler);
    executor.setWorkingDirectory(workingDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (ExecuteException e) {
      // Although Sencha CMD supports multiple instances and picks different ports for the internal phantomjs
      // instance, it does not seem to be robust to support parallel builds.
      // (e.g. if startet at exactly the same time)

      // In that case we try to restart the sencha cmd execution for a defined amount of times.
      if (outputStream.foundError) {
        return false;
      }
      throw e;
    }
    return !watchDog.killedProcess();
  }

  private class CollectingLogOutputStream extends LogOutputStream {

    private static final String ERROR_MESSAGE_BIND_FAIL = "java.net.BindException: Address already in use: bind";

    private final Log log;
    private boolean foundError;

    public CollectingLogOutputStream(Log log) {
      super();
      this.log = log;
    }

    @Override protected void processLine(String line, int level) {
      log.info(line);
      if (line.contains(ERROR_MESSAGE_BIND_FAIL)) {
        foundError = true;
      }
    }
  }
}
