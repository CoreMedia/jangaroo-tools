/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.utils.log;

import java.io.File;

/**
 *
 */
public class StandardOutLogHandler implements LogHandler {

  private File currentFile;

  public void setCurrentFile(File file) {
    this.currentFile = file;
  }

  public void error(String message, int lineNumber, int columnNumber) {
    System.err.println(String.format("ERROR in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
  }

  public void error(String message, Exception exception) {
    System.err.println(message);
    System.err.println(exception);
  }

  public void error(String message) {
    System.err.println(message);
  }

  public void warning(String message) {
    System.err.println(message);
  }

  public void warning(String message, int lineNumber, int columnNumber) {
   System.err.println(String.format("WARNING in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
  }

  public void info(String message) {
    System.out.println(message);
  }

  public void debug(String message) {
    System.out.println(message);
  }
}
