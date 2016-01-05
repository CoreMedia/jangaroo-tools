/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.mojo.pom.PomConverter;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FileUtils.moveFile;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * A Mojo to compile EXML sources to AS3 sources into target/generated-sources/joo in phase generate-sources.
 *
 * @goal convert-to-mxml
 * @phase generate-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
public class ExmlToMxmlMojo extends AbstractExmlMojo {

  /**
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;

  /**
   * @parameter default-value="${plugin}"
   * @readonly
   */
  private PluginDescriptor pluginDescriptor;

  /**
   * Set this to 'true' to rename EXML files to MXML files only and to skip the actual conversion. This allows to give
   * a hint to SCM systems like Git about the renaming and then run the actual conversion in a second step.
   *
   * @parameter expression="${renameOnly}"
   */
  private boolean renameOnly;

  /**
   * Set the map for migrating ExtAS API versions when converting EXML into MXML.
   *
   * @parameter expression="${migrationMap}"
   */
  private File migrationMap;

  @Override
  public void execute() throws MojoExecutionException {
    if (!renameOnly && hasExmlConfiguration()) {
      getLog().info("removing exml-maven-plugin from POM");
      PomConverter.removeExmlPlugin(getProject().getBasedir());
    }

    if (!isExmlProject()) {
      getLog().info("not an EXML project, skipping MXML conversion");
      return;
    }

    if (renameOnly) {
      getLog().info("Renaming EXML files to MXML files");
      try {
        renameFiles(getSourceDirectory());
        renameFiles(testSourceDirectory);
      } catch (IOException e) {
        throw new MojoExecutionException("error while renaming EXML files", e);
      }
      return;
    }

    // Convert main EXML sources to MXML:
    ExmlConfiguration config = createExmlConfiguration(getActionScriptClassPath(),
            Collections.singletonList(getSourceDirectory()), getSourceDirectory());
    config.setMigrationMap(migrationMap);
    new Exmlc(config).convertAllExmlToMxml();
    // Also convert test EXML sources to MXML:
    if (testSourceDirectory != null && testSourceDirectory.exists()) {
      ExmlConfiguration testConfig = createExmlConfiguration(getActionScriptTestClassPath(),
              Collections.singletonList(testSourceDirectory), testSourceDirectory);
      testConfig.setMigrationMap(migrationMap);
      new Exmlc(testConfig).convertAllExmlToMxml();
    }
  }

  private List<File> getActionScriptClassPath() {
    List<File> classPath = getMavenPluginHelper().getActionScriptClassPath(false);
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }

  private void renameFiles(File directory) throws IOException {
    if (directory != null && directory.exists()) {
      for (File exmlFile : listFiles(directory, new String[]{"exml"}, true)) {
        File mxmlFile = new File(exmlFile.getParent(), getBaseName(exmlFile.getName()) + ".mxml");
        getLog().debug(format("Renaming %s to %s", exmlFile.getPath(), mxmlFile.getPath()));
        moveFile(exmlFile, mxmlFile);
      }
    }
  }

  private boolean isExmlProject() {
    for(Object o : getProject().getBuildPlugins()) {
      Plugin plugin = (Plugin) o;
      if (plugin.getGroupId().equals(pluginDescriptor.getGroupId()) &&
              plugin.getArtifactId().equals(pluginDescriptor.getArtifactId())) {
        return plugin.isExtensions();
      }
    }
    return false;
  }

  private boolean hasExmlConfiguration() {
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

  private List<File> getActionScriptTestClassPath() {
    final List<File> classPath = new ArrayList<File>(getMavenPluginHelper().getActionScriptClassPath(true));
    classPath.add(0, getSourceDirectory());
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }
}
