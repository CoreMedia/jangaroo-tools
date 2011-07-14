package net.jangaroo.jooc;

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

  public void error(JooSymbol sym, String msg) {
    error(formatError(sym.getFileName(), sym.getLine(), sym.getColumn(), "Error", msg));
  }

  public void warning(JooSymbol sym, String msg) {
    warning(formatError(sym.getFileName(), sym.getLine(), sym.getColumn(), "Warning", msg));
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
