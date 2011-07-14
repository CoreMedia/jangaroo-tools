package net.jangaroo.jooc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A LineRangeReader that reads a line subrange of a given Reader.
 * Line numbers start at 1. The given endLine is the first line
 * that is not included (like in {@link String#substring(int, int)}).
 */
public class LineRangeReader extends Reader {

  private BufferedReader delegate;
  private int currentLine, endLine;

  public LineRangeReader(Reader delegate, int startLine, int endLine) throws IOException {
    this.delegate = new BufferedReader(delegate);
    for (int i = 1; i < startLine; ++i) {
      this.delegate.readLine();
    }
    this.endLine = endLine;
    this.currentLine = startLine;
  }

  public int read(char[] cbuf, int off, int len) throws IOException {
    if (currentLine >= endLine) {
      return -1;
    }
    int read = delegate.read(cbuf, off, len);
    // scan for newlines:
    for (int i = 0; i < cbuf.length; i++) {
      char c = cbuf[i];
      /* A line is considered to be terminated by any one
       * of a line feed ('\n'), a carriage return ('\r'), or a carriage return
       * followed immediately by a linefeed.
       */
      if (c == '\n' || c == '\r') {
        ++currentLine;
        if (c == '\r' && i + 1 < cbuf.length && cbuf[i + 1] == '\n') {
          ++i;
        }
        if (currentLine >= endLine) {
          return i;
        }
      }
    }
    return read;
  }

  public void close() throws IOException {
    delegate.close();
  }
}
