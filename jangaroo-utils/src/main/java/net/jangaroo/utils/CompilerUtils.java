package net.jangaroo.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 05.07.11 Time: 09:24 To change this template use File | Settings |
 * File Templates.
 */
public class CompilerUtils {
  public static String qName(String packageName, String className) {
    return packageName.length() == 0 ? className : packageName + "." + className;
  }

  public static String packageName(String qName) {
    int lastDotPos = qName.lastIndexOf('.');
    return lastDotPos == -1 ? "" : qName.substring(0, lastDotPos);
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
    try {
      String canonicalBasePath = baseDirectory.getCanonicalPath() + File.separator;
      String canonicalPath = file.getCanonicalPath();
      if (canonicalPath.length() > canonicalBasePath.length() &&
        canonicalPath.startsWith(canonicalBasePath)) {
        String relativePath = canonicalPath.substring(canonicalBasePath.length());
        int lastDotPos = relativePath.lastIndexOf('.');
        if (lastDotPos != -1 && lastDotPos > relativePath.lastIndexOf(File.separatorChar)) {
          return relativePath.substring(0, lastDotPos).replace(File.separatorChar, '.');
        }
      }
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
