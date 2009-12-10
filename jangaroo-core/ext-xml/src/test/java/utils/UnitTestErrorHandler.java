/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package utils;

import net.jangaroo.extxml.log.ErrorHandler;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.File;

/**
 *
 */
public class UnitTestErrorHandler implements ErrorHandler {

  public String expectedErrorMessage;
  private boolean expectedErrorOccured;

  public void setCurrentFile(File file) {

  }


  public void error(String message, int lineNumber, int columnNumber) {
    fail(message);
  }


  public void error(String message, Exception exception) {
    fail(message);
  }


  public void error(String message) {
    if(expectedErrorMessage != null) {
      assertEquals(expectedErrorMessage, message);
      expectedErrorOccured = true;
    } else {
      fail(message);
    }
  }


  public void warning(String message) {

  }


  public void warning(String message, int lineNumber, int columnNumber) {

  }

  public void info(String message) {

  }

  public void checkExpectedErrors() {
    if(!expectedErrorOccured && expectedErrorMessage != null) {
      fail(String.format("expected error %s not occured", expectedErrorMessage));
    }
  }
}
