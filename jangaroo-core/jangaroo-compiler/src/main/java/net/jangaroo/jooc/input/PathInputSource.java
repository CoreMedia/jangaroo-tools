package net.jangaroo.jooc.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathInputSource extends DirectoryInputSource {

  private String name;
  private List<InputSource> entries;

  public static PathInputSource fromPath(String path) {
    throw new UnsupportedOperationException("not impl.");
  }

  public static PathInputSource fromFiles(List<File> files, String[] rootDirs)  throws IOException {
    List<InputSource> entries = new ArrayList<InputSource>();
    String name = "";
    for (File file :files) {
      if (file.isDirectory()) {
        entries.add(new FileInputSource(file, file));
      } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
        entries.add(new ZipFileInputSource(file, rootDirs));
      }
      if (!name.isEmpty()) {
        name += File.pathSeparatorChar;
      }
      name += file.getAbsolutePath();
    }
    return new PathInputSource(name, entries);
  }

  public PathInputSource(final String name, final List<InputSource> entries) {
    super();
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
  public List<InputSource> list()  {
    List<InputSource> result = new ArrayList<InputSource>();
    for (InputSource entry : entries) {
      result.addAll(entry.list());
    }
    return result;
  }

  @Override
  public InputSource getChild(final String path)  {
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
    return result == null ? null : new PathInputSource("(" + getName() + ")" + path, result);
  }

  @Override
  public char getFileSeparatorChar() {
    return '/';
  }
}
