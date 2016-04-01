package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.maven.plugin.logging.Log;

public class SenchaCmdLogOutputStream extends LogOutputStream {

  private static final String ERROR_MESSAGE_BIND_FAIL = "java.net.BindException: Address already in use: bind";
  private static final String ERROR_MESSAGE_PACKAGE_NOT_FOUND = "Failed to resolve package";

  private final Log log;
  private final ExecuteWatchdog watchdog;
  private ExecuteException executeException;

  public SenchaCmdLogOutputStream(ExecuteWatchdog watchdog, Log log) {
    super();
    this.watchdog = watchdog;
    this.log = log;
  }

  @Override protected void processLine(String line, int level) {
    log.info(line);
    // IMPORTANT: this is just a workaround!
    // Although Sencha CMD supports multiple instances and picks different ports for the internal phantomjs
    // instance, it does not seem to be robust to support parallel builds.
    // (e.g. if startet at exactly the same time)
    if (line.contains(ERROR_MESSAGE_BIND_FAIL)) {
      executeException = new RecoverableExecuteException("Detected bind fail", -1);
      // on some systems Sencha cmd hangs after bind fail...
      watchdog.destroyProcess();
    }
    // we cannot use --strict because not all warnings can be avoided, so we need to check for warnings
    // about failed resolving of packages (why isn't this an error in Sencha cmd?)
    if (line.contains(ERROR_MESSAGE_PACKAGE_NOT_FOUND)) {
      executeException = new ExecuteException("Detected failed to resolve package", -1);
    }
  }

  public ExecuteException getExecuteException() {
    return executeException;
  }
}

