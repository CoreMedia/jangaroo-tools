package net.jangaroo.jooc;

import net.jangaroo.jooc.api.FilePosition;

/**
 * An error that occurred during the compilation of Jangaroo sources.
 * If appropriate, a parser symbol is provided to indicate where the error was detected.
 */
public class CompilerError extends RuntimeException {
  private FilePosition symbol = null;

  public CompilerError(String msg) {
    super(msg);
  }

  public CompilerError(String msg, Throwable rootCause) {
    super(msg, rootCause);
  }

  public CompilerError(FilePosition symbol, String msg) {
    super(msg);
    this.symbol = symbol;
  }

  public CompilerError(FilePosition symbol, String msg, Throwable rootCause) {
    super(msg, rootCause);
    this.symbol = symbol;
  }

  public FilePosition getSymbol() {
    return symbol;
  }
}
