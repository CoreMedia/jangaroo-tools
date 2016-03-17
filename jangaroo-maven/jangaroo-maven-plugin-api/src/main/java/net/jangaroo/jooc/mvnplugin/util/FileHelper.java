package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

public final class FileHelper {

  private FileHelper() {
    // hiding constructor of utility class
  }

  public static void writeBuildProperties(File propertyFile, Map<String, String> addProperties) throws MojoExecutionException {
    Properties buildProperties = new Properties();

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(propertyFile);
      buildProperties.load(inputStream);
    } catch (IOException ioe) {
      throw new MojoExecutionException("Failed to read property file " + propertyFile, ioe);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }

    buildProperties.putAll(addProperties);

    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(propertyFile);
      buildProperties.store(outputStream, "Added dynamic properties by jangaroo-maven-plugin.");
    } catch (IOException ioe) {
      throw new MojoExecutionException("Failed to write to property file " + propertyFile, ioe);
    } finally {
      IOUtils.closeQuietly(outputStream);
    }

  }

}
