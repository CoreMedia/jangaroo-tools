package net.jangaroo.jooc.input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileInputSource extends DirectoryInputSource {

  private File sourceDir;
  private File file;
  private List<InputSource> children;

  public FileInputSource(final File sourceDir, final File file, final String fileExtension) {
    super(fileExtension);
    this.sourceDir = sourceDir;
    this.file = file;
  }

  public FileInputSource(final File file, final String fileExtension) {
    this(file, null, fileExtension);
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public String getPath() {
    return file.getAbsolutePath();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return isDirectory() ? super.getInputStream() : new FileInputStream(file);
  }

  @Override
  public String getRelativePath() {
    try {
      return sourceDir == null ? file.getCanonicalPath() :
        file.getCanonicalPath().substring(sourceDir.getCanonicalPath().length() + 1);
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

  @Override
  public InputSource getParent() {
    return new FileInputSource(sourceDir, file.getParentFile(), getFileExtension());
  }

  @Override
  public boolean isDirectory() {
    return file.isDirectory();
  }

  @Override
  public List<InputSource> list() {
    if (!isDirectory()) {
      throw new UnsupportedOperationException("list() not supported for non-directory input sources");
    }
    if (children == null) {
      children = new ArrayList<InputSource>();
      File[] childFiles = file.listFiles(new FileFilter() {
        @Override
        public boolean accept(final File pathname) {
          return pathname.isDirectory() || pathname.getName().endsWith(getFileExtensionWithDot());
        }
      });
      for (File childFile : childFiles) {
        children.add(new FileInputSource(sourceDir, childFile, getFileExtensionWithDot()));
      }
    }
    return children;
  }

  @Override
  public InputSource getChild(final String path) {
    File sourceFile = new File(file, path);
    return sourceFile.exists() && (sourceFile.isDirectory() || sourceFile.getName().endsWith(getFileExtensionWithDot()))
      ? new FileInputSource(sourceDir, sourceFile, getFileExtension())
      : null;
  }

}
