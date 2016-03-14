/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PackagesConfigurer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "extract-remote-packages",
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class SenchaRemotePackagesMojo extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "com.coremedia.sencha:ext:zip", readonly = true)
  private String extFrameworkArtifact;

  @Inject
  private ArchiverManager archiverManager;

  public void execute() throws MojoExecutionException, MojoFailureException {

    String remotePackagesTargetDir = getRemotePackagesDirectory(project);

    File extTargetDirectory = new File( getExtFrameworkDirectory(project) );
    createTargetDir(extTargetDirectory);

    Set<Artifact> dependencyArtifacts = project.getArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      if (SenchaUtils.PACKAGE_EXTENSION.equals(artifact.getType())
              || "zip".equals(artifact.getType())) {

        try {

          UnArchiver unArchiver = archiverManager.getUnArchiver("zip");
          unArchiver.setSourceFile(artifact.getFile());
          if (extFrameworkArtifact.equals(artifact.getDependencyConflictId())) {
            unArchiver.setDestDirectory(extTargetDirectory);
          } else {
            File packagesTargetDirectory = new File( remotePackagesTargetDir +  SenchaUtils.getSenchaPackageNameForArtifact(artifact) );
            createTargetDir(packagesTargetDirectory);
            unArchiver.setDestDirectory(packagesTargetDirectory);
          }
          unArchiver.extract();

        } catch (NoSuchArchiverException e) {
          throw new MojoExecutionException("Could not find zipUnArchiver.", e);
        } catch ( ArchiverException e ) {
          throw new MojoExecutionException( "Could not extract: " + artifact, e );
        }
      }
    }
  }

  static String getRemotePackagesDirectory(MavenProject remotePackagesProject) {
    return remotePackagesProject.getBuild().getDirectory() + "/packages/";
  }

  static String getExtFrameworkDirectory(MavenProject remotePackagesProject) {
    return remotePackagesProject.getBuild().getDirectory() + "/ext/";
  }


  private void createTargetDir(File targetDirectory) throws MojoExecutionException {
    if (!targetDirectory.exists() && !targetDirectory.mkdirs()) {
      throw new MojoExecutionException("could not create folder for remote package " + targetDirectory);
    }
  }

}
