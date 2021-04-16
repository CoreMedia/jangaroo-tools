package net.jangaroo.jooc.input;

import net.jangaroo.utils.BOMStripperInputStream;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileInputSource extends DirectoryInputSource {

  private File sourceDir;
  private File file;
  private List<InputSource> children;

  public FileInputSource(final File sourceDir, final File file, boolean inSourcePath, boolean inCompilePath, String extNamespace) {
    super(inSourcePath, inCompilePath);
    this.extNamespace = extNamespace;
    this.sourceDir = sourceDir;
    this.file = file;
  }

  public FileInputSource(final File sourceDir, final File file, boolean inSourcePath, String extNamespace) {
    super(inSourcePath);
    this.extNamespace = extNamespace;
    this.sourceDir = sourceDir;
    this.file = file;
  }

  public FileInputSource(final File file, boolean inSourcePath) {
    this(file, file, inSourcePath, null);
  }

  public FileInputSource(final File file, boolean inSourcePath, boolean inCompilePath) {
    this(file, file, inSourcePath, inCompilePath, null);
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
    return isDirectory() ? super.getInputStream() : new BOMStripperInputStream(new FileInputStream(file));
  }

  @Override
  public String getRelativePath() {
    try {
      return sourceDir == null ? file.getCanonicalPath() :
              sourceDir.equals(file) ? "" :
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

  public File getSourceDir() {
    return sourceDir;
  }

  @Override
  public char getFileSeparatorChar() {
    return File.separatorChar;
  }

  @Override
  public InputSource getParent() {
    return new FileInputSource(sourceDir, file.getParentFile(), isInSourcePath(), getExtNamespace());
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
      File[] childFiles = file.listFiles();
      assert childFiles != null; // since we made sure it is a directory!
      Arrays.sort(childFiles);   // create a predictable order!
      for (File childFile : childFiles) {
        children.add(new FileInputSource(sourceDir, childFile, isInSourcePath(), getExtNamespace()));
      }
    }
    return children;
  }

  @Override
  public FileInputSource getChild(String path) {
    if (path.length() == 0) {
      return this;
    }
    File sourceFile = new File(file, path);
    if (sourceFile.exists()) {
      String realPath = file == null ? sourceFile.getPath() : CompilerUtils.getRelativePath(file, sourceFile, false);
      if (File.separatorChar != '/') {
        path = path.replace(File.separatorChar, '/');
        realPath = realPath.replace(File.separatorChar, '/');
      }
      if (path.equals(realPath)) {
        return new FileInputSource(sourceDir, sourceFile, isInSourcePath(), isInCompilePath(), getExtNamespace());
      }
    }
    return null;
  }

  @Override
  public List<InputSource> getChildren(String path) {
    InputSource child = isDirectory() ? getChild(path) : null;
    return child == null ? Collections.<InputSource>emptyList() : Collections.singletonList(child);
  }

  @Override
  public String toString() {
    return file.getPath();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FileInputSource that = (FileInputSource) o;

    return file.equals(that.file);

  }

  @Override
  public int hashCode() {
    return file.hashCode();
  }
}
