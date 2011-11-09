package net.jangaroo.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 05.07.11 Time: 09:24 To change this template use File | Settings |
 * File Templates.
 */
public final class CompilerUtils {
  // utility class, do not instantiate
  private CompilerUtils() {
  }

  public static String qName(String packageName, String className) {
    return packageName.length() == 0 ? className : packageName + "." + className;
  }

  public static String packageName(String qName) {
    int lastDotPos = qName.lastIndexOf('.');
    return lastDotPos == -1 ? "" : qName.substring(0, lastDotPos);
  }

  public static String uncapitalize(String name) {
    if (name == null) {
      return name;
    }
    int capitalCount = 0;
    while (capitalCount < name.length() && Character.isUpperCase(name.charAt(capitalCount))) {
      capitalCount++;
    }
    int toLowerCount = capitalCount <= 1 || capitalCount == name.length() ?
            capitalCount :
            capitalCount - 1;
    return new StringBuilder(name.length())
            .append(name.substring(0, toLowerCount).toLowerCase(Locale.ROOT))
            .append(name.substring(toLowerCount))
            .toString();
  }

  public static String className(String qName) {
    int lastDotPos = qName.lastIndexOf('.');
    return lastDotPos == -1 ? qName : qName.substring(lastDotPos + 1);
  }

  public static File fileFromQName(String packageName, String className, File baseDirectory, String extension) {
    return fileFromQName(qName(packageName, className), baseDirectory, extension);
  }

  public static File fileFromQName(String qName, File baseDirectory, String extension) {
    char separatorChar = File.separatorChar;
    return new File(baseDirectory, fileNameFromQName(qName, separatorChar, extension));
  }

  public static String fileNameFromQName(String qName, char separatorChar, String extension) {
    return qName.replace('.', separatorChar) + extension;
  }

  public static String qNameFromFile(File baseDirectory, File file) {
    String relativePath = getRelativePath(baseDirectory, file);
    if (relativePath != null) {
      int lastDotPos = relativePath.lastIndexOf('.');
      if (lastDotPos != -1 && lastDotPos > relativePath.lastIndexOf(File.separatorChar)) {
        return relativePath.substring(0, lastDotPos).replace(File.separatorChar, '.');
      }
    }
    return null;
  }

  public static String getRelativePath(File baseDirectory, File file) {
    String relativePath = null;
    try {
      String canonicalBasePath = baseDirectory.getCanonicalPath() + File.separator;
      String canonicalPath = file.getCanonicalPath();
      if (canonicalPath.length() > canonicalBasePath.length() &&
              canonicalPath.startsWith(canonicalBasePath)) {
        relativePath = canonicalPath.substring(canonicalBasePath.length());
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("could not determine qualified name from file; the strange file is called " + file + " in " + baseDirectory, e);
    }
    return relativePath;
  }

  /**
   * Returns the directory path portion of a file specification string.
   * Matches the equally named unix command.
   *
   * @param filename the file path
   * @return The directory portion excluding the ending file separator.
   */
  public static String dirname(String filename) {
    int i = filename.lastIndexOf(File.separator);
    return (i >= 0 ? filename.substring(0, i) : "");
  }

  /**
   * Remove extension from filename.
   * ie
   * <pre>
   * foo.txt --> foo
   * a\b\c.jpg --> a\b\c
   * a\b\c --> a\b\c
   * </pre>
   *
   * @param filename the path of the file
   * @return the filename minus extension
   */
  public static String removeExtension(final String filename) {
    String ext = extension(filename);

    if ("".equals(ext)) {
      return filename;
    }

    final int index = filename.lastIndexOf(ext) - 1;
    return filename.substring(0, index);
  }

  /**
   * Returns the extension portion of a file specification string.
   * This everything after the last dot '.' in the filename (NOT including
   * the dot).
   *
   * @param filename the file path
   * @return the extension of the file
   */
  public static String extension(String filename) {
    // Ensure the last dot is after the last file separator
    int lastSep = filename.lastIndexOf(File.separatorChar);
    int lastDot;
    if (lastSep < 0) {
      lastDot = filename.lastIndexOf('.');
    } else {
      lastDot = filename.substring(lastSep + 1).lastIndexOf('.');
      if (lastDot >= 0) {
        lastDot += lastSep + 1;
      }
    }

    if (lastDot >= 0 && lastDot > lastSep) {
      return filename.substring(lastDot + 1);
    }

    return "";
  }
}
