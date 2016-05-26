package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class FileHelper {

  private FileHelper() {
    // hiding constructor of utility class
  }

  public static void writeBuildProperties(File propertyFile, Map<String, String> addProperties) throws MojoExecutionException {
    Properties buildProperties = new Properties();

    try (InputStream inputStream = new FileInputStream(propertyFile)) {
      buildProperties.load(inputStream);
    } catch (IOException ioe) {
      throw new MojoExecutionException("Failed to read property file " + propertyFile, ioe);
    }

    buildProperties.putAll(addProperties);

    try (OutputStream outputStream = new FileOutputStream(propertyFile)) {
      buildProperties.store(outputStream, "Added dynamic properties by jangaroo-maven-plugin.");
    } catch (IOException ioe) {
      throw new MojoExecutionException("Failed to write to property file " + propertyFile, ioe);
    }

  }
  public static void copyFiles(File srcDir, File targetDir) throws MojoExecutionException {
    if (srcDir.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, targetDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Copying sencha sources from %s to %s failed.", srcDir, targetDir ), e);
      }
    }
  }

  public static void addToConfigFile(File file, List<String> properties) throws MojoExecutionException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true)) ) {
      for (String property: properties) {
        pw.println(property);
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Could not append skip.sass and skip.slice to sencha.cfg of package", e);
    }
  }


  public static void ensureDirectory(File dir) throws MojoExecutionException {
    if (!dir.exists() && !dir.mkdirs()) {
      throw new MojoExecutionException("could not create folder for directory " + dir);
    }
  }
}
