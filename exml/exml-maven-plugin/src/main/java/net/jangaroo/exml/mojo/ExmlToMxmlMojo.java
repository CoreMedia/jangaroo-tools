/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.mojo.pom.PomConverter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
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
@SuppressWarnings("unused")
public class ExmlToMxmlMojo extends AbstractExmlMojo {

  private static final DefaultArtifactVersion EXT_AS_FIRST_SWC_ARTIFACT_VERSION = new DefaultArtifactVersion("6.2.0-12");
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
   * The target version of the Ext AS API artifact (net.jangaroo:ext-as), needed for converting EXML into MXML.
   * @parameter default-value="${extAsVersion}"
   */
  private String extAsVersion;

  /**
   * @component
   */
  private ArtifactResolver artifactResolver;

  /**
   * @component
   */
  private ArtifactFactory artifactFactory;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  private List remoteRepositories;

  @Override
  public void execute() throws MojoExecutionException {
    boolean useNewPackagingType = extAsVersion != null &&
            new DefaultArtifactVersion(extAsVersion).compareTo(EXT_AS_FIRST_SWC_ARTIFACT_VERSION) >= 0;
    if (!renameOnly && (hasExmlConfiguration() || "jangaroo".equals(getProject().getPackaging()))) {
      getLog().info("reading POM from " + getProject().getBasedir().getPath());
      PomConverter pomConverter = new PomConverter(getProject().getBasedir());

      getLog().info("removing exml-maven-plugin from POM");
      pomConverter.removeExmlPlugin();
      String newPackaging = useNewPackagingType ? "swc" : "jangaroo-pkg";
      getLog().info("changing packaging from 'jangaroo' to '" + newPackaging + "'");
      pomConverter.changePackaging(newPackaging);
      if (useNewPackagingType) {
        getLog().info("adding dependency type 'swc' to all compile dependencies");
        pomConverter.addDependencyType(newPackaging);
      }

      getLog().info("updating POM at " + getProject().getBasedir().getPath());
      pomConverter.writePom();
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

    if (extAsVersion == null) {
      throw new MojoExecutionException("exml-to-mxml needs an extAsVersion.");
    }
    Artifact extAsArtifact = resolveExtAsArtifact(extAsVersion, useNewPackagingType ? "swc" : "jar");
    File extAsJar = extAsArtifact.getFile();

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

  private Artifact resolveExtAsArtifact(String version, String packaging) throws MojoExecutionException {
    Artifact toDownload = artifactFactory.createBuildArtifact("net.jangaroo", "ext-as", version, packaging);
    try {
      getLog().info("Resolving " + toDownload + "...");
      artifactResolver.resolve(toDownload, remoteRepositories, localRepository);
      return toDownload;
    } catch (AbstractArtifactResolutionException e) {
      throw new MojoExecutionException("Couldn't download artifact: " + e.getMessage(), e);
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
