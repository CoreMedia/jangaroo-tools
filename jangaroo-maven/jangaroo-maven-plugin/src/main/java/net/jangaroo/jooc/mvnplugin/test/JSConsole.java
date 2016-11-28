package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.logging.Log;

import java.util.Arrays;
import java.util.Objects;

public class JSConsole {

  private final Log log;

  JSConsole(Log log) {
    this.log = log;
  }

  private static String formatMessage(Object... args) {
    if (args == null || args.length == 0) {
      return null;
    }

    if (args.length == 1) {
      return Objects.toString(args[0]);
    }

    Object firstArg = args[0];
    if (firstArg instanceof String && ((String) firstArg).contains("%")) {
      return String.format((String)firstArg, Arrays.asList(args).subList(1, args.length).toArray());
    }
    return Arrays.toString(args);
  }

  public void debug(Object... args) {
    log.debug(formatMessage(args));
  }

  public void log(Object... args) {
    log.info(formatMessage(args));
  }

  public void info(Object... args) {
    log.info(formatMessage(args));
  }

  public void warn(Object... args) {
    log.warn(formatMessage(args));
  }

  public void error(Object... args) {
    log.error(formatMessage(args));
  }
}
