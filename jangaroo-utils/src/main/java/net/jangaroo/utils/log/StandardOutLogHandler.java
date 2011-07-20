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
    exception.printStackTrace(System.err); // NOSONAR dear Sonar, this *is* a log, we did not forget to use a log
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
