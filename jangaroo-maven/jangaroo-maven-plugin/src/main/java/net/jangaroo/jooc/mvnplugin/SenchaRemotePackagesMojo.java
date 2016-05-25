/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
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

import javax.inject.Inject;
import java.io.File;
import java.util.Set;

@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "extract-remote-packages",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class SenchaRemotePackagesMojo extends AbstractSenchaMojo {

  private static final String PACKAGES_DIRECTORY = "/packages/";
  private static final String EXT_FRAMEWORK_DIRECTORY = "/ext/";

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Inject
  private ArchiverManager archiverManager;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    String remotePackagesTargetDir = getRemotePackagesDirectory(project);

    Set<Artifact> dependencyArtifacts = project.getArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      if (Type.PACKAGE_EXTENSION.equals(artifact.getType())) {

        unpackArtifact(remotePackagesTargetDir, artifact);

      }
    }
  }

  private void unpackArtifact(String remotePackagesTargetDir, Artifact artifact) throws MojoExecutionException {
    try {

      UnArchiver unArchiver = archiverManager.getUnArchiver(Type.ZIP_EXTENSION);
      unArchiver.setSourceFile(artifact.getFile());

      File packageTargetDir;
      Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(getExtFrameworkArtifact());
      Dependency currentArtifactDependency = MavenDependencyHelper.fromArtifact(artifact);
      if (MavenDependencyHelper.equalsGroupIdAndArtifactId(currentArtifactDependency, extFrameworkDependency)) {
        packageTargetDir = new File( getExtFrameworkDirectory(project) );
      } else {
        packageTargetDir = new File(remotePackagesTargetDir
                        + SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId()));
      }

      FileHelper.ensureDirectory(packageTargetDir);
      unArchiver.setDestDirectory(packageTargetDir);

      unArchiver.extract();
      getLog().info(String.format("Extracted %s to %s", artifact, packageTargetDir));

    } catch (NoSuchArchiverException e) {
      throw new MojoExecutionException("Could not find zipUnArchiver.", e);
    } catch ( ArchiverException e ) {
      throw new MojoExecutionException( "Could not extract: " + artifact, e );
    }
  }

  static String getRemotePackagesDirectory(MavenProject remotePackagesProject) {
    return remotePackagesProject.getBuild().getDirectory() + PACKAGES_DIRECTORY;
  }

  static String getExtFrameworkDirectory(MavenProject remotePackagesProject) {
    return remotePackagesProject.getBuild().getDirectory() + EXT_FRAMEWORK_DIRECTORY;
  }
}
