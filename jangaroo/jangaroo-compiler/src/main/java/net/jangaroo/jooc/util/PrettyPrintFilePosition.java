package net.jangaroo.jooc.util;

import com.google.debugging.sourcemap.FilePosition;

/**
 * A Google source maps FilePosition with a meaningful toString() method.
 */
public class PrettyPrintFilePosition extends FilePosition {

  public PrettyPrintFilePosition(FilePosition filePosition) {
    this(filePosition.getLine(), filePosition.getColumn());
  }

  public PrettyPrintFilePosition(int line, int column) {
    super(line, column);
  }

  @Override
  public String toString() {
    return String.format("%d:%d", getLine() + 1, getColumn() + 1);
  }
}
