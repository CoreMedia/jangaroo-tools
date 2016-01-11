package net.jangaroo.exml.mojo;

import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.jooc.mvnplugin.JangarooMojo;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
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
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;
  /**
   * Output directory for all test ActionScript3 files generated out of exml components
   *
   * @parameter default-value="${project.build.directory}/generated-test-sources/joo"
   */
  private File generatedTestSourcesDirectory;
  /**
   * The package into which config classes of EXML components are generated.
   *
   * @parameter
   */
  private String configClassPackage;

  /**
   * @parameter default-value="${plugin}"
   * @readonly
   */
  private PluginDescriptor pluginDescriptor;

  @Override
  protected MavenProject getProject() {
    return project;
  }

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public File getTestSourceDirectory() {
    return testSourceDirectory;
  }

  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  public File getGeneratedTestSourcesDirectory() {
    return generatedTestSourcesDirectory;
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

  protected boolean hasExmlConfiguration() {
    for (Object o : getProject().getBuildPlugins()) {
      Plugin plugin = (Plugin) o;
      if (plugin.getGroupId().equals(pluginDescriptor.getGroupId()) &&
              plugin.getArtifactId().equals(pluginDescriptor.getArtifactId())) {
        return true;
      }
    }
    Build build = getProject().getOriginalModel().getBuild();
    if (build != null) {
      PluginManagement pluginManagement = build.getPluginManagement();
      if (pluginManagement != null) {
        for (Plugin plugin : pluginManagement.getPlugins()) {
          if (plugin.getGroupId().equals(pluginDescriptor.getGroupId()) &&
                  plugin.getArtifactId().equals(pluginDescriptor.getArtifactId())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  protected boolean isExmlProject() {
    for(Object o : getProject().getBuildPlugins()) {
      Plugin plugin = (Plugin) o;
      if (plugin.getGroupId().equals(pluginDescriptor.getGroupId()) &&
              plugin.getArtifactId().equals(pluginDescriptor.getArtifactId())) {
        return plugin.isExtensions();
      }
    }
    return false;
  }
}
