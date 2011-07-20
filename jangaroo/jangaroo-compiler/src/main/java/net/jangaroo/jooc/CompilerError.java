package net.jangaroo.jooc;

/**
 * An error that occurred during the compilation of Jangaroo sources.
 * If appropriate, a parser symbol is provided to indicate where the error was detected.
 */
public class CompilerError extends RuntimeException {
  private JooSymbol symbol = null;

  public CompilerError(String msg) {
    super(msg);
  }

  public CompilerError(String msg, Throwable rootCause) {
    super(msg, rootCause);
  }

  public CompilerError(JooSymbol symbol, String msg) {
    super(msg);
    this.symbol = symbol;
  }

  public CompilerError(JooSymbol symbol, String msg, Throwable rootCause) {
    super(msg, rootCause);
    this.symbol = symbol;
  }

  public JooSymbol getSymbol() {
    return symbol;
  }
}
