package net.jangaroo.jooc.api;

/**
 * Position of a symbol (token) in a file, e.g. to specify where an error occurred.
 */
public interface FilePosition {
  String getFileName();

  int getLine();

  int getColumn();
}
