package net.jangaroo.jooc.mvnplugin.sencha.executor;


import org.apache.commons.exec.LogOutputStream;
import org.apache.maven.plugin.logging.Log;

public class SenchaCmdLogOutputStream extends LogOutputStream {

  private static final String SENCHA_DEBUG_PREFIX = "[DBG] ";
  private static final String SENCHA_INFO_PREFIX = "[INF] ";
  private static final String SENCHA_WARN_PREFIX = "[WRN] ";
  private static final String SENCHA_ERROR_PREFIX = "[ERR] ";
  private static final int SENCHA_PREFIX_SIZE = SENCHA_DEBUG_PREFIX.length() - 1; // zero based

  private Log log;


  public SenchaCmdLogOutputStream(Log log) {
    this.log = log;
  }

  @Override
  protected void processLine(String line, int level) {

      if (line.startsWith(SENCHA_DEBUG_PREFIX)) {
      log.debug(line.substring(SENCHA_PREFIX_SIZE));
    } else if (line.startsWith(SENCHA_INFO_PREFIX)) {
      log.info(line.substring(SENCHA_PREFIX_SIZE));
    } else if (line.startsWith(SENCHA_WARN_PREFIX)) {
      log.warn(line.substring(SENCHA_PREFIX_SIZE));
    } else if (line.startsWith(SENCHA_ERROR_PREFIX)) {
      log.error(line.substring(SENCHA_PREFIX_SIZE));
    } else {
      log.info(line);
    }
  }
}
