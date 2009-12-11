/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.log;

public final class Log {

  private static ErrorHandler errorHandler = new StandardOutErrorHandler();

  private Log() {
    
  }

  public static void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }

  public static ErrorHandler getErrorHandler() {
    return errorHandler;
  }
}
