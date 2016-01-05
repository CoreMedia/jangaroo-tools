package net.jangaroo.exml.mojo;

import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.jooc.mvnplugin.JangarooMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The most abstract EXML Mojo you could imagine.
 */
public abstract class AbstractExmlMojo extends JangarooMojo {
  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter default-value="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * The Maven project object
   *
   * @parameter expression="${project}"
   */
  private MavenProject project;
  /**
   * Source directory to scan for files to compile.
   *
   * @parameter default-value="${basedir}/src/main/joo"
   */
  private File sourceDirectory;
  /**
   * The package into which config classes of EXML components are generated.
   *
   * @parameter
   */
  private String configClassPackage;

  @Override
  protected MavenProject getProject() {
    return project;
  }

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  protected ExmlConfiguration createExmlConfiguration(List<File> classPath, List<File> sourcePath, File outputDirectory) throws MojoExecutionException {
    if (configClassPackage == null) {
      throw new MojoExecutionException("parameter 'configClassPackage' is missing");
    }

    ExmlConfiguration exmlConfiguration = new ExmlConfiguration();
    exmlConfiguration.setConfigClassPackage(configClassPackage);
    exmlConfiguration.setClassPath(classPath);
    exmlConfiguration.setOutputDirectory(outputDirectory);
    try {
      exmlConfiguration.setSourcePath(sourcePath);
    } catch (IOException e) {
      throw new MojoExecutionException("could not determine source directory", e);
    }
    return exmlConfiguration;
  }

}
