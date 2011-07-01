package net.jangaroo.jooc;

/**
* Created by IntelliJ IDEA.
* User: okummer
* Date: 01.07.11
* Time: 15:07
* To change this template use File | Settings | File Templates.
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

  public JooSymbol getSymbol() {
    return symbol;
  }
}
