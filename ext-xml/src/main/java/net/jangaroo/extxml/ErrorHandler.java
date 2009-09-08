/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

/**
 *
 */
public interface ErrorHandler {

  void error(String message, int lineNumber, int columnNumber);

  void error(String message, Exception exception);

  void error(String message);

  void warning(String message);

  void warning(String message, int lineNumber, int columnNumber);
}
