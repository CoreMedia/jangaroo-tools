package com.coremedia.studio.tools.exmlconverter;

/**
 * A file could not be parsed. This exception indicates that the generated output
 * is likely incorrect and should be discarded.
 */
public class ParseException extends Exception{
  public ParseException(String message) {
    super(message);
  }
}
