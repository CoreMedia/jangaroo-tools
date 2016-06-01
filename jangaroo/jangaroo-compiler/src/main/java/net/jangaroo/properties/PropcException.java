package net.jangaroo.properties;

import java.io.File;

/**
 *
 */
public class PropcException extends RuntimeException {
  private File source;

  public PropcException(String message, Throwable cause) {
    super(message, cause);
  }

  public PropcException(Throwable cause) {
    super(cause);
  }

  public PropcException(String message, File source, Exception e) {
    this(message, e);
    setSource(source);
  }

  public File getSource() {
    return source;
  }

  public void setSource(File source) {
    this.source = source;
  }
}
