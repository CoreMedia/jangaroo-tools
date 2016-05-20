package net.jangaroo.jooc.mvnplugin.sencha;


import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {

  private FileUtils() {
    // hide constructor
  }

  public static void copyFiles(String sourcePath, String destPath) throws MojoExecutionException {
    // getProject().getBasedir() + SENCHA_SRC_PATH
    File srcDir = new File(sourcePath);
    File targetDir = new File(destPath);
    if (srcDir.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, targetDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Copying sencha sources from %s to %s failed.", srcDir, targetDir ), e);
      }
    }
  }
}
