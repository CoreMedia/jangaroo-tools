/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

/**
 *
 */
public class StandardOutErrorHandler implements ErrorHandler{

  public void error(String message, int lineNumber, int columnNumber) {
    System.err.println(String.format("ERROR in line %s, column %s: %s", lineNumber, columnNumber, message));
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
   System.err.println(String.format("WARNING in line %s, column %s: %s", lineNumber, columnNumber, message));
  }
}
