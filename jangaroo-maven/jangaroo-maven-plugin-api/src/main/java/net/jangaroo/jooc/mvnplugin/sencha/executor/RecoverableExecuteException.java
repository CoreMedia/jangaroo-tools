package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.ExecuteException;

public class RecoverableExecuteException extends ExecuteException {

  public RecoverableExecuteException(String message, int exitValue) {
    super(message, exitValue);
  }

}
