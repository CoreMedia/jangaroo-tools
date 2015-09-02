package net.jangaroo.jooc.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileInputSource extends DirectoryInputSource {

  private File file;
  private ZipFile zipFile;
  private String[] rootDirs;
  private Map<String, ZipEntryInputSource> entries = new LinkedHashMap<String, ZipEntryInputSource>();

  /**
   * Create an InputSource directory from the given zip or jar file, providing a "union view" over the zip file
   * with all entries with paths relative to the given root directories
   *
   * @param file     a zip or jar file
   * @param rootDirs a list of directories to accept as roots (e.g. ["", "META-INF/joo-api"], in lookup order
   * @param inSourcePath whether this is part of the source path
   * @throws IOException if an IO error occurs
   */
  public ZipFileInputSource(final File file, String[] rootDirs, boolean inSourcePath) throws IOException {
    super(inSourcePath);
    this.file = file;
    this.zipFile = new ZipFile(file);
    this.rootDirs = rootDirs.clone();
    final Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
    while (zipEntryEnum.hasMoreElements()) {
      ZipEntry entry = zipEntryEnum.nextElement();
      final String relativePath = getRelativePath(entry.getName());
      if (relativePath != null && !entries.containsKey(relativePath)) {
        this.entries.put(relativePath, new ZipEntryInputSource(this, entry, relativePath));
      }
    }
  }

  private String getRelativePath(final String name) {
    // find the root dir with maximal length
    String foundRoot = null;
    for (String rootDir : rootDirs) {
      if (name.startsWith(rootDir) && (foundRoot == null || foundRoot.length() < rootDir.length())) {
        foundRoot = rootDir;
      }
    }
    if (foundRoot == null) {
      return null;
    }
    String strippedName = name.substring(foundRoot.length());
    return strippedName.endsWith("/") ? strippedName.substring(0, strippedName.length() - 1) : strippedName;
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
  public String getRelativePath() {
    return "";
  }

  @Override
  public List<InputSource> list() {
    return list("");
  }

  @Override
  public void close() throws IOException {
    zipFile.close();
  }

  @Override
  public char getFileSeparatorChar() {
    return '/';
  }

  public ZipFile getZipFile() {
    return zipFile;
  }

  @Override
  public InputSource getChild(final String path) {
    return entries.get(path);
  }

  @Override
  public List<InputSource> getChildren(String path) {
    InputSource child = getChild(path);
    return child == null ? Collections.<InputSource>emptyList() : Collections.singletonList(child);
  }

  List<InputSource> list(final ZipEntryInputSource dir) {
    if (!dir.isDirectory()) {
      throw new UnsupportedOperationException("list() is not supported for non-directory input sources");
    }
    return list(dir.getRelativePath());
  }

  private List<InputSource> list(final String relativePath) {
    List<InputSource> result = new ArrayList<InputSource>();
    for (Map.Entry<String, ZipEntryInputSource> entry : entries.entrySet()) {
      String p2 = entry.getValue().getRelativePath();
      for (String root : rootDirs) {
        String p = root + relativePath;
        if (p2.startsWith(p) &&
                p2.length() > p.length() &&
                p2.lastIndexOf('/') == (p.isEmpty() ? -1 : p.length())) {
          result.add(entry.getValue());
          break;
        }
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return zipFile.getName();
  }
}