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
 * @goal convert-to-mxml
 * @phase generate-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
public class ExmlToMxmlMojo extends AbstractExmlMojo {

  /**
   * Set this to 'true' to rename EXML files to MXML files only and to skip the actual conversion. This allows to give
   * a hint to SCM systems like Git about the renaming and then run the actual conversion in a second step.
   * @parameter default-value="${renameOnly}"
   */
  private boolean renameOnly;

  /**
   * Set this to 'true' when EXML files have been renamed to MXML files already but the files still need to be
   * converted.
   * @parameter default-value="${alreadyRenamed}"
   */
  private boolean alreadyRenamed;

  /**
   * The JAR containing the target ExtAS API for converting EXML into MXML.
   * @parameter default-value="${extAsJar}"
   */
  private File extAsJar;

  @Override
  public void execute() throws MojoExecutionException {
    if (!renameOnly && hasExmlConfiguration()) {
      getLog().info("removing exml-maven-plugin from POM");
      PomConverter.removeExmlPlugin(getProject().getBasedir());
    }

    if (!renameOnly && "jangaroo".equals(getProject().getPackaging())) {
      getLog().info("changing packaging from jangaroo to jangaroo-pkg");
      PomConverter.changePackaging(getProject().getBasedir());
    }

    if (!isExmlProject()) {
      getLog().info("not an EXML project, skipping MXML conversion");
      return;
    }

    if (renameOnly) {
      getLog().info("Renaming EXML files to MXML files");
      try {
        renameFiles(getSourceDirectory(), "exml", "mxml");
        renameFiles(getTestSourceDirectory(), "exml", "mxml");
      } catch (IOException e) {
        throw new MojoExecutionException("error while renaming EXML files", e);
      }
      return;
    }

    if (alreadyRenamed) {
      getLog().info("Renaming MXML files back to EXML files before running the conversion");
      try {
        renameFiles(getSourceDirectory(), "mxml", "exml");
        renameFiles(getTestSourceDirectory(), "mxml", "exml");
      } catch (IOException e) {
        throw new MojoExecutionException("error while renaming files", e);
      }
    }

    // Convert main EXML sources to MXML:
    ExmlConfiguration config = createExmlConfiguration(getActionScriptClassPath(),
            Collections.singletonList(getSourceDirectory()), getGeneratedSourcesDirectory());
    config.setExtAsJar(extAsJar);
    new Exmlc(config).convertAllExmlToMxml();
    // Also convert test EXML sources to MXML:
    if (getTestSourceDirectory() != null && getTestSourceDirectory().exists()) {
      ExmlConfiguration testConfig = createExmlConfiguration(getActionScriptTestClassPath(),
              Collections.singletonList(getTestSourceDirectory()), getGeneratedTestSourcesDirectory());
      testConfig.setExtAsJar(extAsJar);
      new Exmlc(testConfig).convertAllExmlToMxml();
    }
  }

  private List<File> getActionScriptClassPath() {
    List<File> classPath = getMavenPluginHelper().getActionScriptClassPath(false);
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }

  private void renameFiles(File directory, String sourceExtension, String targetExtension) throws IOException {
    if (directory != null && directory.exists()) {
      for (File exmlFile : listFiles(directory, new String[]{sourceExtension}, true)) {
        File mxmlFile = new File(exmlFile.getParent(), getBaseName(exmlFile.getName()) + "." + targetExtension);
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
