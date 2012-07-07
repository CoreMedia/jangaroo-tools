package net.jangaroo.jooc.util;

import java.io.IOException;
import java.io.Writer;

/**
 * A delgating Writer that keeps track of the current line and column of the text being written.
 */
public class PositionTrackingWriter extends Writer {
  private Writer delegate;
  private int line = 1;
  private int column = 1;

  public PositionTrackingWriter(Writer delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    delegate.write(cbuf, off, len);
    for (int i = off; i < off + len; i++) {
      char c = cbuf[i];
      if (c == '\n') {
        ++line;
        column = 1;
      } else {
        ++column;
      }
    }
  }

  @Override
  public void flush() throws IOException {
    delegate.flush();
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }
}
