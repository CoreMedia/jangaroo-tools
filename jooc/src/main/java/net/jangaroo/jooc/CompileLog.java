/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
public class CompileLog {

  protected boolean errors = false;
  protected boolean warnings = false;

  public void error(JooSymbol sym, String msg) {
    error(formatError(sym.getFileName(), sym.getLine(), sym.getColumn(), "Error", msg));
  }

  public void error(String msg) {
    System.out.println(msg);
    errors = true;
  }

  public void warning(JooSymbol sym, String msg) {
    warning(formatError(sym.getFileName(), sym.getLine(), sym.getColumn(), "Warning", msg));
  }

  public void warning(String msg) {
    System.out.println(msg);
    warnings = true;
  }

  public boolean hasErrors() {
    return errors;
  }

  String formatError(String fileName, int line, int column, String debugLevel, String message) {
    StringBuffer m = new StringBuffer();
    m.append(fileName + "(" + line + "): ");
    m.append(debugLevel);
    m.append(": ");
    m.append("in column " + column + ": ");
    m.append(message);
    return m.toString();
  }


}
