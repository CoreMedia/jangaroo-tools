package net.jangaroo.exml;

import java.io.File;

/**
 * An exception that occurs while parsing EXML files.
 */
public class ExmlcException extends RuntimeException {
  private File source;
  private int line;

  public ExmlcException(String message) {
    super(message);
  }

  public ExmlcException(String message, int line) {
    super(message);
    setLine(line);
  }

  public ExmlcException(String message, Throwable t) {
    super(message, t);
  }

  public ExmlcException(String message, File source, Exception e) {
    this(message, e);
    setSource(source);
  }

  public void setSource(File source) {
    this.source = source;
  }

  public void setLine(int line) {
    this.line = line;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (source != null) {
      builder.append('[');
      builder.append(source.toString());
      if (line > 0) {
        // Only show line number if the source is given. (They would be pretty useless otherwise.)
        builder.append(":");
        builder.append(line);
      }
      builder.append("] ");
    }
    builder.append(super.toString());
    return builder.toString();
  }
}
