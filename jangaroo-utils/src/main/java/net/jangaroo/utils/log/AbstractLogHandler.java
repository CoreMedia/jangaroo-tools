package net.jangaroo.utils.log;

import java.io.File;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 08.07.11 Time: 08:52 To change this template use File | Settings |
 * File Templates.
 */
public abstract class AbstractLogHandler implements LogHandler {
  private File currentFile;

  public void setCurrentFile(File file) {
    this.currentFile = file;
  }

  protected String format(String logLevel, String message, int lineNumber, int columnNumber) {
    return String.format("%s in %s, line %s, column %s: %s", logLevel, currentFile, lineNumber, columnNumber, message);
  }

  @Override
  public void error(String message, int lineNumber, int columnNumber) {
    error(formatError(message, lineNumber, columnNumber));
  }

  protected String formatError(String message, int lineNumber, int columnNumber) {
    return format("ERROR", message, lineNumber, columnNumber);
  }

  @Override
  public void warning(String message, int lineNumber, int columnNumber) {
   warning(formatWarning(message, lineNumber, columnNumber));
  }

  protected String formatWarning(String message, int lineNumber, int columnNumber) {
    return format("WARNING", message, lineNumber, columnNumber);
  }
}
