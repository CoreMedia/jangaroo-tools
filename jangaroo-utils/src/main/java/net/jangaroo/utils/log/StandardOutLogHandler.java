/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.utils.log;

/**
 *
 */
public class StandardOutLogHandler extends AbstractLogHandler {

  public void error(String message, Exception exception) {
    error(message);
    exception.printStackTrace();
  }

  public void error(String message) {
    System.err.println(message);
  }

  public void warning(String message) {
    System.err.println(message);
  }

  public void info(String message) {
    System.out.println(message);
  }

  public void debug(String message) {
    System.out.println(message);
  }
}
