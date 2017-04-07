package net.jangaroo.jooc.util;

public class FilePosition {
  private final int line;
  private final int column;

  public FilePosition(int line, int column) {
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return this.line;
  }

  public int getColumn() {
    return this.column;
  }
}
