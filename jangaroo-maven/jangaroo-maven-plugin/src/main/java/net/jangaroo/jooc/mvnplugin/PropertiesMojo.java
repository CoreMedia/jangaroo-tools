/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import freemarker.template.TemplateException;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.utils.log.Log;
import net.jangaroo.utils.log.LogHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Mojo to compile properties files to ActionScript3 files
 *
 * @goal properties
 * @phase generate-sources
 * @requiresDependencyResolution
 */
public class PropertiesMojo extends AbstractMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${basedir}/src/main/joo"
   */
  private File resourceDirectory;

  /**
   *
   * Fileset for properties. default is:
   * {@code
   * <properties>
   *   <directory>${basedir}/src/main/joo</directory>
   *   <includes>
   *     <include>**\/*.properties</include>
   *   </includes>
   * </properties>
   * }
   * @parameter
   */
  private FileSet properties;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * @component
   */
  MavenProjectHelper projectHelper;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!generatedSourcesDirectory.exists()) {
      getLog().info("generating sources into: " + generatedSourcesDirectory.getPath());
      getLog().debug("created " + generatedSourcesDirectory.mkdirs());
    }
    MavenLogHandler logHandler = new MavenLogHandler();
    Log.setLogHandler(logHandler);
    if (properties == null) {
      properties = new FileSet();
      properties.setDirectory(resourceDirectory.getAbsolutePath());
      properties.addInclude("**/*.properties");
    }
    
    LocalizationSuite suite = new LocalizationSuite(properties, generatedSourcesDirectory);

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    try {
      scanner.scan();
    } catch (IOException e) {
      throw new MojoExecutionException("Scan failure" ,e);
    }

    PropertyClassGenerator generator = new PropertyClassGenerator(suite);
    try {
      generator.generate();
    } catch (IOException e) {
      throw new MojoExecutionException("Generation failure" ,e);
    } catch (TemplateException e) {
      throw new MojoExecutionException("Generation failure" ,e);
    }

  }
  class MavenLogHandler implements LogHandler {
    ArrayList<String> errors = new ArrayList<String>();
    ArrayList<String> warnings = new ArrayList<String>();
    Exception lastException;
    String exceptionMsg;
    File currentFile;

    public void setCurrentFile(File file) {
      this.currentFile = file;
    }

    public void error(String message, int lineNumber, int columnNumber) {
      errors.add(String.format("ERROR in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
    }

    public void error(String message, Exception exception) {
      this.exceptionMsg = message;
      if (currentFile != null) {
        this.exceptionMsg += String.format(" in file: %s", currentFile);
      }
      this.lastException = exception;
    }

    public void error(String message) {
      errors.add(message);
    }

    public void warning(String message) {
      warnings.add(message);
    }

    public void warning(String message, int lineNumber, int columnNumber) {
      warnings.add(String.format("WARNING in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
    }

    public void info(String message) {
      getLog().info(message);
    }

    public void debug(String message) {
      getLog().debug(message);
    }
  }
}
