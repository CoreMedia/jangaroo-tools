/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.mojo.pom.PomConverter;
import org.apache.maven.plugin.MojoExecutionException;

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
   * Set this to 'true' to rename EXML files to MXML files only and to skip the actual conversion. This allows to give
   * a hint to SCM systems like Git about the renaming and then run the actual conversion in a second step.
   *
   * @parameter expression="${renameOnly}"
   */
  private boolean renameOnly;

  /**
   * The JAR containing the target ExtAS API for converting EXML into MXML.
   *
   * @parameter expression="${extAsJar}"
   */
  private File extAsJar;

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
        renameFiles(getTestSourceDirectory());
      } catch (IOException e) {
        throw new MojoExecutionException("error while renaming EXML files", e);
      }
      return;
    }

    // Convert main EXML sources to MXML:
    ExmlConfiguration config = createExmlConfiguration(getActionScriptClassPath(),
            Collections.singletonList(getSourceDirectory()), getSourceDirectory());
    config.setExtAsJar(extAsJar);
    new Exmlc(config).convertAllExmlToMxml();
    // Also convert test EXML sources to MXML:
    if (getTestSourceDirectory() != null && getTestSourceDirectory().exists()) {
      ExmlConfiguration testConfig = createExmlConfiguration(getActionScriptTestClassPath(),
              Collections.singletonList(getTestSourceDirectory()), getTestSourceDirectory());
      testConfig.setExtAsJar(extAsJar);
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

  private List<File> getActionScriptTestClassPath() {
    final List<File> classPath = new ArrayList<File>(getMavenPluginHelper().getActionScriptClassPath(true));
    classPath.add(0, getSourceDirectory());
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }
}
