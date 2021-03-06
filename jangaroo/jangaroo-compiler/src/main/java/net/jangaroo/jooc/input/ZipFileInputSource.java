package net.jangaroo.jooc.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
  private Multimap<String, ZipEntryInputSource> entriesByParent = HashMultimap.create();

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
        ZipEntryInputSource zipEntryInputSource = new ZipEntryInputSource(this, entry, relativePath);
        this.entries.put(relativePath, zipEntryInputSource);

        int slashPos = relativePath.lastIndexOf('/');
        String parent = relativePath.substring(0, slashPos + 1);
        entriesByParent.put(removeTrailingSlash(parent), zipEntryInputSource);
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
    return removeTrailingSlash(strippedName);
  }

  private String removeTrailingSlash(String strippedName) {
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
    Collection<ZipEntryInputSource> zipEntryInputSources = entriesByParent.get(removeTrailingSlash(relativePath));
    return zipEntryInputSources == null ? Collections.<InputSource>emptyList() : new ArrayList<InputSource>(zipEntryInputSources);
  }

  @Override
  public String toString() {
    return zipFile.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ZipFileInputSource that = (ZipFileInputSource) o;

    if (!file.equals(that.file)) {
      return false;
    }
    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    return Arrays.equals(rootDirs, that.rootDirs);

  }

  @Override
  public int hashCode() {
    int result = file.hashCode();
    result = 31 * result + Arrays.hashCode(rootDirs);
    return result;
  }
}