/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.log;

import java.io.File;

public final class Log {

  private static LogHandler logHandler = new StandardOutLogHandler();

  private Log() {
    
  }

  public static void setLogHandler(LogHandler handler) {
    logHandler = handler;
  }

  public static void setCurrentFile(File currentFile) {
    getLogHandler().setCurrentFile(currentFile);
  }

  private static LogHandler getLogHandler() {
    return logHandler;
  }

  public static void e(String message, int lineNumber, int columnNumber){
    getLogHandler().error(message, lineNumber, columnNumber);
  }

  public static void e(String message, Exception exception){
    getLogHandler().error(message, exception);
  }

  public static void e(String message){
    getLogHandler().error(message);
  }

  public static void w(String message){
    getLogHandler().warning(message);
  }

  public static void w(String message, int lineNumber, int columnNumber){
    getLogHandler().warning(message, lineNumber, columnNumber);
  }

  public static void i(String message){
    getLogHandler().info(message);
  }

  public static void d(String message) {
    getLogHandler().debug(message);
  }

}
