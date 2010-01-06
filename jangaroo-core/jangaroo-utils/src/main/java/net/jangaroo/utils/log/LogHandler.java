/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.utils.log;

import java.io.File;

/**
 *
 */
public interface LogHandler {

  void setCurrentFile(File file);

  void error(String message, int lineNumber, int columnNumber);

  void error(String message, Exception exception);

  void error(String message);

  void warning(String message);

  void warning(String message, int lineNumber, int columnNumber);

  void info(String message);

  void debug(String message);
}
