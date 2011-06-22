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
public class JooSymbol extends java_cup.runtime.Symbol {

  private int line;
  private int column;
  private String whitespace;
  private String text;
  private Object jooValue;
  private String fileName;
  private boolean isVirtual;
  private boolean semicolonInsertedBefore;

  public JooSymbol(String text) {
    this(net.jangaroo.jooc.sym.IDE,  text);
  }

  public JooSymbol(int sym, String text) {
    this(sym,  "", -1, -1, "", text);
  }

  public JooSymbol(int sym, String fileName, int line, int column, String whitespace, String text) {
    this(sym, fileName, line, column, whitespace, text, null);
  }

  public JooSymbol(int sym, String fileName, int line, int column, String whitespace, String text, Object jooValue) {
    super(sym, -1, -1, null);
    this.fileName = fileName;
    this.value = this;
    this.line = line;
    this.column = column;
    this.whitespace = whitespace;
    this.text = text;
    this.jooValue = jooValue;
  }

  public String toString() {
    return "line "+line+", column "+column+": '" + whitespace + "', '" + text + "'" + " file " + fileName;
  }

  public String getText() {
    return text;
  }

  public String getWhitespace() {
    return whitespace;
  }

  public Object getJooValue() {
    return jooValue;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public boolean isVirtual() {
    return isVirtual;
  }

  public void setVirtual(final boolean virtual) {
    isVirtual = virtual;
  }

  public boolean isSemicolonInsertedBefore() {
    return semicolonInsertedBefore;
  }

  public void setSemicolonInsertedBefore(final boolean semicolonInsertedBefore) {
    this.semicolonInsertedBefore = semicolonInsertedBefore;
  }
}
