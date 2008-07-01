/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
public class CompileLog {

  protected boolean errors = false;

  public void error(JscSymbol sym, String msg) {
    error(formatError(sym.getFileName(), sym.getLine(), sym.getColumn(), msg));
    errors = true;
  }

  public void error(String msg) {
    System.out.println(msg);
    errors = true;
  }

  public boolean hasErrors() {
    return errors;
  }

  String formatError(String fileName, int line, int column, String message) {
    StringBuffer m = new StringBuffer();
    m.append(fileName + "(" + line + "): ");
    m.append("Error: ");
    m.append("in column " + column + ": ");
    m.append(message);
    return m.toString();
  }


}
