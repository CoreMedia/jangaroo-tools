package net.jangaroo.exml.api;

import net.jangaroo.jooc.api.FilePosition;

import java.io.File;

/**
 * An exception that occurs while parsing EXML files.
 */
public final class ExmlcException extends RuntimeException implements FilePosition {
  private File file;
  private int line;
  private int column;

  public ExmlcException(String message) {
    super(message);
  }

  public ExmlcException(String message, int line) {
    this(message, line, -1);
  }

  public ExmlcException(String message, int line, int column) {
    super(message);
    setLine(line);
    setColumn(column);
  }

  public ExmlcException(String message, Throwable t) {
    super(message, t);
  }

  public ExmlcException(String message, File file, Exception e) {
    this(message, e);
    setFile(file);
  }

  public void setFile(File file) {
    this.file = file;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public File getFile() {
    return file;
  }

  @Override
  public String getFileName() {
    return file.getAbsolutePath();
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (file != null) {
      builder.append('[');
      builder.append(file.toString());
      if (line > 0) {
        // Only show line number if the source is given. (They would be pretty useless otherwise.)
        builder.append(":");
        builder.append(line);
        if(column != -1) {
          builder.append(";");
          builder.append(column);
        }
      }

      builder.append("] ");
    }
    builder.append(super.toString());
    return builder.toString();
  }
}
