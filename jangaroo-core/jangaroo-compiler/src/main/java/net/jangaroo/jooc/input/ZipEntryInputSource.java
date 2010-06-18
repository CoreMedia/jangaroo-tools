package net.jangaroo.jooc.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;

public class ZipEntryInputSource implements InputSource {

  private ZipFileInputSource zipFileInputSource;
  private ZipEntry zipEntry;
  private String name;
  private String relativePath;

  public ZipEntryInputSource(final ZipFileInputSource zipFileInputSource, final ZipEntry zipEntry, final String relativePath) {
    this.zipFileInputSource = zipFileInputSource;
    this.zipEntry = zipEntry;
    this.relativePath = relativePath;

    name = zipEntry.getName();
    int lastSlash = name.lastIndexOf('/');
    if (lastSlash >= 0) {
      name = name.substring(lastSlash+1);
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return zipFileInputSource.getPath() + "!" + zipEntry.getName();
  }

  @Override
  public InputSource getParent() {
    String path = getPath();
    int lastSlash = path.lastIndexOf('/');
    if (lastSlash < 0) {
      return null;
    }
    String parentPath = path.substring(0, lastSlash);
    return zipFileInputSource.getChild(parentPath);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return zipFileInputSource.getZipFile().getInputStream(zipEntry);
  }

  @Override
  public boolean isDirectory() {
    return zipEntry.isDirectory();
  }

  @Override
  public List<InputSource> list() {
    return zipFileInputSource.list(this);
  }

  @Override
  public InputSource getChild(final String path) {
    String p = getRelativePath();
    if (!p.isEmpty()) {
      p += '/';
    }
    p += path;
    return zipFileInputSource.getChild(p);
  }

  @Override
  public String getRelativePath() {
    return relativePath;
  }

  @Override
  public void close() throws IOException {
    zipFileInputSource.close();
  }

  @Override
  public char getFileSeparatorChar() {
    return '/';
  }
}