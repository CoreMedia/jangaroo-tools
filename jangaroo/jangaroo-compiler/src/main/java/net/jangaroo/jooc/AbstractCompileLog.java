package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;

public abstract class AbstractCompileLog implements CompileLog {
  protected boolean errors = false;

  String formatError(String fileName, int line, int column, String debugLevel, String message) {
    StringBuilder m = new StringBuilder();
    m.append(fileName);
    m.append("(");
    m.append(line);
    m.append("): ");
    m.append(debugLevel);
    m.append(": ");
    m.append("in column ");
    m.append(column);
    m.append(": ");
    m.append(message);
    return m.toString();
  }

  public void error(FilePosition position, String msg) {
    error(formatError(position.getFileName(), position.getLine(), position.getColumn(), "Error", msg));
  }

  public void warning(FilePosition position, String msg) {
    warning(formatError(position.getFileName(), position.getLine(), position.getColumn(), "Warning", msg));
  }

  public boolean hasErrors() {
    return errors;
  }

  public void error(String msg) {
    doLogError(msg);
    errors = true;
  }

  protected abstract void doLogError(String msg);

}
