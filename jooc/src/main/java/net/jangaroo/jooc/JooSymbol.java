/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
public class JooSymbol extends java_cup.runtime.Symbol {
  protected int line;
  protected int column;
  protected String whitespace;
  protected String text;
  protected Object jooValue;
  protected String fileName;

  public JooSymbol(int type, String fileName, int line, int column, String whitespace, String text) {
    this(type, fileName, line, column, whitespace, text, null);
  }

  public JooSymbol(int type, String fileName, int line, int column, String whitespace, String text, Object jooValue) {
    super(type, -1, -1, null);
    this.fileName = fileName;
    this.value = this;
    this.line = line;
    this.column = column;
    this.whitespace = whitespace;
    this.text = text;
    this.jooValue = jooValue;
  }

  public String toString() {
    return "line "+line+", column "+column+": '" + whitespace + "', '" + text + "'";
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


}
