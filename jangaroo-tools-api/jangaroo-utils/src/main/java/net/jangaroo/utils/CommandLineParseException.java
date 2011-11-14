package net.jangaroo.utils;

/**
 *
 */
public class CommandLineParseException extends Exception {
    private int exitCode;

    public CommandLineParseException(String message, int exitCode) {
      super(message);
      this.exitCode = exitCode;
    }

    public CommandLineParseException(String message, int exitCode, Throwable cause) {
      super(message, cause);
      this.exitCode = exitCode;
    }

    public int getExitCode() {
      return exitCode;
    }
  }