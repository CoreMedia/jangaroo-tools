package net.jangaroo.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
  public static boolean isParent(File dir, File file) throws IOException {
    File parent = file.getParentFile();
    while (parent != null) {
      if (parent.equals(dir)) {
        return true;
      }
      parent = parent.getParentFile();
    }
    return false;
  }
}
