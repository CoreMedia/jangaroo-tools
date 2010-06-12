package net.jangaroo.jooc;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileInputSource implements InputSource {

  private ZipFile zipFile;
  private ZipEntry zipEntry;
  private String relativePath;

  public ZipFileInputSource(final ZipFile zipFile, final ZipEntry zipEntry, final String relativePath) {
    this.zipFile = zipFile;
    this.zipEntry = zipEntry;
    this.relativePath = relativePath;
  }

  @Override
  public String getName() {
    return zipFile.getName() + "!" + zipEntry.getName();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return zipFile.getInputStream(zipEntry);
  }

  @Override
  public String getRelativePath() {
    return relativePath;
  }

  @Override
  public void close() throws IOException {
    zipFile.close();
  }

  @Override
  public char getFileSeparatorChar() {
    return '/';
  }
}