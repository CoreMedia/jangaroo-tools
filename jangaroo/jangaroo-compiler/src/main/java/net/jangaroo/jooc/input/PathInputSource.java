package net.jangaroo.jooc.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathInputSource extends DirectoryInputSource {

  private String name;
  private List<InputSource> entries;

  public static PathInputSource fromFiles(List<File> files, String[] rootDirs, boolean inSourcePath) throws IOException {
    return fromFiles(files, rootDirs, inSourcePath, null);
  }

  public static PathInputSource fromFiles(List<File> files, String[] rootDirs, boolean inSourcePath, String as3PackagePrefixToRemoveInTypeScript) throws IOException {
    List<InputSource> entries = new ArrayList<InputSource>();
    StringBuilder name = new StringBuilder();
    for (File file : files) {
      if (file.isDirectory()) {
        entries.add(new FileInputSource(file, file, inSourcePath, as3PackagePrefixToRemoveInTypeScript));
      } else if (file.getName().endsWith(".swc") || file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
        entries.add(new ZipFileInputSource(file, rootDirs, inSourcePath));
      }
      if (!(name.length() == 0)) {
        name.append(File.pathSeparatorChar);
      }
      name.append(file.getAbsolutePath());
    }
    return new PathInputSource(name.toString(), entries, inSourcePath);
  }

  public PathInputSource(final String name, final List<InputSource> entries, boolean inSourcePath) {
    super(inSourcePath);
    this.name = name;
    this.entries = entries;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return getName();
  }

  @Override
  public String getRelativePath() {
    return "";
  }

  @Override
  public List<InputSource> list() {
    List<InputSource> result = new ArrayList<InputSource>();
    for (InputSource entry : entries) {
      result.addAll(entry.list());
    }
    return result;
  }

  @Override
  public InputSource getChild(final String path) {
    List<InputSource> result = null;
    for (InputSource entry : entries) {
      final InputSource child = entry.getChild(path);
      if (child != null) {
        if (!child.isDirectory()) {
          return child;
        }
        if (result == null) {
          result = new ArrayList<InputSource>();
        }
        result.add(child);
      }
    }
    return result == null ? null : new PathInputSource("(" + getName() + ")" + path, result, isInSourcePath());
  }

  @Override
  public List<InputSource> getChildren(String path) {
    List<InputSource> result = new ArrayList<InputSource>();
    for (InputSource entry : entries) {
      List<InputSource> children = entry.getChildren(path);
      result.addAll(children);
    }
    return result;
  }

  @Override
  public char getFileSeparatorChar() {
    return '/';
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PathInputSource that = (PathInputSource) o;

    return entries.equals(that.entries);

  }

  @Override
  public int hashCode() {
    return entries.hashCode();
  }
}
