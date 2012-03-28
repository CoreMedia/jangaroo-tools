package net.jangaroo.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

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
    return getRelativePath(baseDirectory, file, true);
  }

  public static String getRelativePath(File baseDirectory, File file,
                                       boolean onlyBelowBaseDirectory) {
    try {
      return getRelativePath(baseDirectory.getCanonicalPath() + File.separator, file.getCanonicalPath(), File.separator, onlyBelowBaseDirectory);
    } catch (IOException e) {
      throw new IllegalArgumentException("could not determine qualified name from file; the strange file is called " + file + " in " + baseDirectory, e);
    }
  }

  static String getRelativePathBelow(String canonicalBasePath, String canonicalPath) {
    if (canonicalPath.length() > canonicalBasePath.length() &&
      canonicalPath.startsWith(canonicalBasePath)) {
      return canonicalPath.substring(canonicalBasePath.length());
    }
    return null;
  }

  static String getRelativePath(String canonicalBasePath, String canonicalPath, String fileSeparator,
                                  boolean onlyBelowBaseDirectory) {
    String relativePath = getRelativePathBelow(canonicalBasePath, canonicalPath);
    if (relativePath == null && !onlyBelowBaseDirectory) {
      // construct with "..":
      String fileSeparatorAsRegExp = Pattern.quote(fileSeparator);
      String[] basePathParts = canonicalBasePath.split(fileSeparatorAsRegExp);
      String[] pathParts = canonicalPath.split(fileSeparatorAsRegExp, -1);
      int samePartCount = basePathParts.length;
      for (int i = 0; i < basePathParts.length; i++) {
        if (!basePathParts[i].equals(pathParts[i])) {
          samePartCount = i;
          break;
        }
      }
      StringBuilder builder = new StringBuilder();
      for (int i = samePartCount; i < basePathParts.length; i++) {
        builder.append("..").append(fileSeparator);
      }
      for (int i = samePartCount; i < pathParts.length; i++) {
        builder.append(pathParts[i]);
        if (i < pathParts.length - 1) {
          builder.append(fileSeparator);
        }
      }
      relativePath = builder.toString();
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

  /**
   * Produce a string in double quotes with backslash sequences in all the
   * right places. A backslash will be inserted within </, allowing JSON
   * text to be delivered in HTML. In JSON text, a string cannot contain a
   * control character or an unescaped quote or backslash.
   *
   * @param string A String
   * @return A String correctly formatted for insertion in a JSON text.
   */
  public static String quote(String string) {
    if (string == null || string.length() == 0) {
      return "\"\"";
    }

    char b;
    char c = 0;
    int i;
    int len = string.length();
    StringBuilder sb = new StringBuilder(len + 4);
    String t;

    sb.append('"');
    for (i = 0; i < len; i += 1) {
      b = c;
      c = string.charAt(i);
      switch (c) {
        case '\\':
        case '"':
          sb.append('\\');
          sb.append(c);
          break;
        case '/':
          if (b == '<') {
            sb.append('\\');
          }
          sb.append(c);
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\r':
          sb.append("\\r");
          break;
        default:
          if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
              (c >= '\u2000' && c < '\u2100')) {
            t = "000" + Integer.toHexString(c);
            sb.append("\\u").append(t.substring(t.length() - 4));
          } else {
            sb.append(c);
          }
      }
    }
    sb.append('"');
    return sb.toString();
  }

  public static AS3Type guessType(String attributeValue) {
    try {
      long l = Long.parseLong(attributeValue);
      return l >= 0 ? AS3Type.UINT : AS3Type.INT;
    } catch (NumberFormatException e) {
      // Try again.
    }
    try {
      Double.parseDouble(attributeValue);
      return AS3Type.NUMBER;
    } catch (NumberFormatException e) {
      // Try again.
    }
    if ("false".equalsIgnoreCase(attributeValue) || "true".equalsIgnoreCase(attributeValue)) {
      return AS3Type.BOOLEAN;
    }
    // TODO: guess /.../ to be a RegExp? Guess date formats? Allow [a, b, ...] for Arrays?
    return null;
  }
}
