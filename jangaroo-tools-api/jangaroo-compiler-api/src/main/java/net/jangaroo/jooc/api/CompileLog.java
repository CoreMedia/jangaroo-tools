package net.jangaroo.jooc.api;

/**
 * Callback interface for compiler errors and warnings.
 */
public interface CompileLog {
  void error(FilePosition position, String msg);

  void error(String msg);

  void warning(FilePosition position, String msg);

  void warning(String msg);

  boolean hasErrors();
}
