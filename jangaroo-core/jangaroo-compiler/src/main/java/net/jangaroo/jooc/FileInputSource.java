package net.jangaroo.jooc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

public class FileInputSource implements InputSource {

  private File sourceDir;
  private File file;

  public FileInputSource(final File file) {
    this.file = file;
  }

  public FileInputSource(final File file, final File sourceDir) {
    this.sourceDir = sourceDir;
    this.file = file;
  }

  @Override
  public String getName() {
    return file.getAbsolutePath();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }

  @Override
  public String getRelativePath() {
    try {
      return sourceDir == null ? null :
        file.getCanonicalPath().substring(sourceDir.getCanonicalPath().length()+1);
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public void close() {
  }

  public File getFile() {
    return file;
  }

  @Override
  public char getFileSeparatorChar() {
    return File.separatorChar;
  }
}
