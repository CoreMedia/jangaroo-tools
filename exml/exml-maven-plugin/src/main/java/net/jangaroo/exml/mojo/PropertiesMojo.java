/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.utils.FileLocations;
import net.jangaroo.properties.api.PropcException;
import net.jangaroo.properties.PropertyClassGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Mojo to compile properties files to ActionScript3 files
 *
 * @goal properties
 * @phase generate-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
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
   * Fileset for properties. default is:
   * {@code
   * <properties>
   * <directory>${basedir}/src/main/joo</directory>
   * <includes>
   * <include>**\/*.properties</include>
   * </includes>
   * </properties>
   * }
   *
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
  private MavenProjectHelper projectHelper;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!generatedSourcesDirectory.exists()) {
      getLog().info("generating sources into: " + generatedSourcesDirectory.getPath());
      getLog().debug("created " + generatedSourcesDirectory.mkdirs());
    }

    if (properties == null) {
      properties = new FileSet();
      properties.setDirectory(resourceDirectory.getAbsolutePath());
      properties.addInclude("**/*.properties");
    }

    FileLocations config = new FileLocations();
    config.setOutputDirectory(generatedSourcesDirectory);

    for (String srcFileRelativePath : new FileSetManager().getIncludedFiles(properties)) {
      config.addSourceFile(new File(resourceDirectory,srcFileRelativePath));
    }

    try {
      config.setSourcePath(Arrays.asList(resourceDirectory));
    } catch (IOException e) {
      throw new MojoExecutionException("configuration failure", e);
    }

    PropertyClassGenerator generator = new PropertyClassGenerator(config);
    try {
      generator.generate();
    } catch (PropcException e) {
      throw new MojoExecutionException("Generation failure", e);
    }

  }

}
