package net.jangaroo.jooc.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileInputSource extends DirectoryInputSource {

  private final File file;
  private final ZipFile zipFile;
  private final String[] rootDirs;
  private final Map<String, ZipEntryInputSource> entries = new LinkedHashMap<String, ZipEntryInputSource>();
  private final Multimap<String, ZipEntryInputSource> entriesByParent = HashMultimap.create();
  private String senchaPackageName;

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
    this(file, rootDirs, inSourcePath, false);
  }

  /**
   * Create an InputSource directory from the given zip or jar file, providing a "union view" over the zip file
   * with all entries with paths relative to the given root directories
   *
   * @param file     a zip or jar file
   * @param rootDirs a list of directories to accept as roots (e.g. ["", "META-INF/joo-api"], in lookup order
   * @param inSourcePath whether this is part of the source path
   * @param inCompilePath whether this is part of the compile path
   * @throws IOException if an IO error occurs
   */
  public ZipFileInputSource(final File file, String[] rootDirs, boolean inSourcePath, boolean inCompilePath) throws IOException {
    super(inSourcePath, inCompilePath);
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
    readSenchaPackageJson();
  }

  private static final Pattern JSON_PROPERTY_STRING_VALUE_PATTERN = Pattern.compile("\"([a-z]+)\"\\s*:\\s*\"(.*)\"");

  private void readSenchaPackageJson() throws IOException {
    InputSource packageJsonInputSource = getChild("META-INF/pkg/package.json");
    if (packageJsonInputSource == null) {
      return;
    }
    try (BufferedReader br = new BufferedReader(new InputStreamReader(packageJsonInputSource.getInputStream(), StandardCharsets.UTF_8))) {
      for (String line; (line = br.readLine()) != null; ) {
        Matcher matcher = JSON_PROPERTY_STRING_VALUE_PATTERN.matcher(line);
        if (matcher.find()) {
          String propertyName = matcher.group(1);
          if (senchaPackageName == null && "name".equals(propertyName)) {
            senchaPackageName = matcher.group(2);
          }
          if (extNamespace == null && "namespace".equals(propertyName)) {
            extNamespace = matcher.group(2);
          }
          if (senchaPackageName != null && extNamespace != null) {
            break;
          }
        }
      }
    }
  }

  public String getSenchaPackageName() {
    return senchaPackageName;
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