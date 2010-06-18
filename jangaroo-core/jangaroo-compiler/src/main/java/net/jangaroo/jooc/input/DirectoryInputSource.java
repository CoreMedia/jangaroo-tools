package net.jangaroo.jooc.input;

import java.io.IOException;
import java.io.InputStream;

public abstract class DirectoryInputSource implements InputSource {

  private String fileExtension;
  private String fileExtensionWithDot;

  protected DirectoryInputSource(final String fileExtension) {
    this.fileExtension = fileExtension;
    fileExtensionWithDot = fileExtension.startsWith(".") ? fileExtension : "." + fileExtension;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public String getFileExtensionWithDot() {
    return fileExtensionWithDot;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    throw new UnsupportedOperationException("getInputStream() is not supported for directory input sources");
  }

  @Override
  public void close() throws IOException {
    throw new UnsupportedOperationException("close() is not supported for directory input sources");
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public InputSource getParent() {
    throw new UnsupportedOperationException("getParent() not supported for " + getClass().getSimpleName());
  }

}