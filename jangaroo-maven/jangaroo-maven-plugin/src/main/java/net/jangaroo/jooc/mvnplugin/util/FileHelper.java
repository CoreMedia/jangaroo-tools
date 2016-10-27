package net.jangaroo.jooc.mvnplugin.util;

import org.apache.maven.plugin.MojoExecutionException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

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

  public static void copyDirectory(@Nonnull File srcDir, @Nonnull File targetDir) throws MojoExecutionException {
    if (srcDir.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(srcDir, targetDir);
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
      throw new MojoExecutionException("Could not append properties to file " + file, e);
    }
  }


  public static void ensureDirectory(File dir) throws MojoExecutionException {
    if (!dir.exists() && !dir.mkdirs()) {
      throw new MojoExecutionException("could not create folder for directory " + dir);
    }
  }

  public static void copyFilesToDirectory(@Nonnull File source, @Nonnull File target, String matchPattern)
          throws MojoExecutionException {
    File[] files = source.listFiles();
    if (files == null || files.length == 0) {
      return;
    }
    Pattern pattern = null;
    if (matchPattern != null) {
      pattern = Pattern.compile(matchPattern);
    }

    for (File file: files) {
      if (file.isFile() && (pattern == null || pattern.matcher(file.getName()).matches())) {
        doCopyFile(file, target);
      }
    }
  }

  private static void doCopyFile(@Nonnull File source, @Nonnull File target) throws MojoExecutionException {
    try {
      Files.copy(source.toPath(),
              target.toPath().resolve(source.getName()),
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.COPY_ATTRIBUTES,
              LinkOption.NOFOLLOW_LINKS);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not copy files to janagaroo dir", e);
    }
  }

  public static void copyDirectories(@Nonnull File source, @Nonnull File target, Set<String> excludeDirectories)
          throws MojoExecutionException {
    File[] files = source.listFiles();
    if (files == null || files.length == 0) {
      return;
    }

    for (File file: files) {
      if (file.isDirectory() && (excludeDirectories == null || !excludeDirectories.contains(file.getName()))) {
        copyDirectory(file, target);
      }
    }
  }
}
