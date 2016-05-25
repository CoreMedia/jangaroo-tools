/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.properties.api.PropertiesCompilerConfiguration;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.api.PropcException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "properties",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class PropertiesMojo extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * Source directory to scan for files to compile.
   */
  @Parameter(defaultValue = "${basedir}/src/main/joo")
  private File resourceDirectory;

  /**
   * Fileset for properties. default is:
   * <pre>
   * &lt;properties>
   *   &lt;directory>${basedir}/src/main/joo&lt;/directory>
   *   &lt;includes>
   *     &lt;include>**\/*.properties&lt;/include>
   *   &lt;/includes>
   * &lt;/properties>
   * </pre>
   */
  @Parameter
  private FileSet properties;

  /**
   * Output directory into which compiled property file classes are generated.
   * By default, for packaging type <code>jangaroo-app</code>, the directory
   * <code>${project.build.directory}/app/locale</code>
   * is used, for packaging type <code>jangaroo-pkg</code>, it is
   * <code>${project.build.directory}/packages/local/package/locale</code>.
   */
  @Parameter
  private File outputDirectory;

  /**
   * Output directory for generated AS3 API classes.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/joo")
  private File apiOutputDirectory;

  @Component
  private MavenProjectHelper projectHelper;

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  public File getOutputDirectory() {
    if (outputDirectory == null) {
      String modulePath = Type.JANGAROO_PKG_PACKAGING.equals(project.getPackaging()) ? "packages/local/package/locale" : "app/locale";
      return new File(project.getBuild().getDirectory(), modulePath);
    }
    return outputDirectory;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File currentOutputDirectory = getOutputDirectory();
    if (!currentOutputDirectory.exists()) {
      getLog().info("generating localized JavaScript into: " + currentOutputDirectory.getPath());
      getLog().debug("created " + currentOutputDirectory.mkdirs());
    }

    if (!apiOutputDirectory.exists()) {
      getLog().info("generating AS3 localization api stubs into: " + apiOutputDirectory.getPath());
      getLog().debug("created " + apiOutputDirectory.mkdirs());
    }

    if (properties == null) {
      properties = new FileSet();
      properties.setDirectory(resourceDirectory.getAbsolutePath());
      properties.addInclude("**/*.properties");
    }

    PropertiesCompilerConfiguration config = new PropertiesCompilerConfiguration();
    config.setOutputDirectory(currentOutputDirectory);
    config.setApiOutputDirectory(apiOutputDirectory);

    for (String srcFileRelativePath : new FileSetManager().getIncludedFiles(properties)) {
      config.addSourceFile(new File(resourceDirectory,srcFileRelativePath));
    }

    try {
      config.setSourcePath(Collections.singletonList(resourceDirectory));
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
