package net.jangaroo.jooc;

/**
 * Callback interface for compiler errors and warnings.
 */
public interface CompileLog {
  void error(JooSymbol sym, String msg);

  void error(String msg);

  void warning(JooSymbol sym, String msg);

  void warning(String msg);

  boolean hasErrors();
}
