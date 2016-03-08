/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.PomManipulator;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-sencha-workspace",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true, aggregator = true)
public class SenchaWorkspaceMojo extends JangarooMojo {

  @Parameter(defaultValue = "${session}")
  private MavenSession session;

  public void execute() throws MojoExecutionException, MojoFailureException {

    MavenSenchaConfiguration.ArtifactItem remotePackages = getSenchaConfiguration().getRemotePackagesArtifact();
    if (remotePackages != null) {
      MavenProject remotePackagesProject = getRemotePackagesProject(session, remotePackages);

      String remotePackagesPath = getPathRelativeToCurrentProjectFrom("remote.packages.dir", remotePackagesProject);
      getSenchaConfiguration().setPackagesDir(remotePackagesPath);

      String extPath = getPathRelativeToCurrentProjectFrom("ext.dir", remotePackagesProject);
      getSenchaConfiguration().setExtFrameworkDir(extPath);

      // add remote packaging module to all jangaroo modules that do not contain this dependency
      addRemotePackagesProject(remotePackagesProject);
    }

    getSenchaConfiguration().setType(SenchaConfiguration.Type.WORKSPACE);

    // for now:
    SenchaHelper senchaHelper = new SenchaModuleHelper(getProject(), getSenchaConfiguration(), getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
  }

  @Nonnull
  static MavenProject getRemotePackagesProject(@Nonnull MavenSession session,
                                                       @Nonnull MavenSenchaConfiguration.ArtifactItem remotePackageArtifact)
          throws MojoExecutionException {
    if (remotePackageArtifact.getGroupId() == null || remotePackageArtifact.getArtifactId() == null) {
      return session.getCurrentProject();
    }
    List<MavenProject> allReactorProjects = session.getProjects();
    for (MavenProject project : allReactorProjects) {

      if (project.getGroupId().equals(remotePackageArtifact.getGroupId())
              && project.getArtifactId().equals(remotePackageArtifact.getArtifactId())) {
        return project;
      }

    }
    throw new MojoExecutionException("Could not find local remote-packages module with coordinates "
            + remotePackageArtifact.getGroupId() + ":" + remotePackageArtifact.getArtifactId());
  }

  private void addRemotePackagesProject(@Nonnull MavenProject remotePackagesProject)
          throws MojoExecutionException {
    Dependency remotePackagingProjectDependency = getRemotePackagingProjectAsDependency(remotePackagesProject);
    if (null != remotePackagingProjectDependency) {
      List<MavenProject> allReactorProjects = session.getProjects();
      for (MavenProject project : allReactorProjects) {
        if (Types.JANGAROO_TYPE.equals(project.getPackaging())) {
          List<Dependency> projectDependencies = project.getDependencies();
          if (!containsDependency(projectDependencies, remotePackagingProjectDependency)) {
            getLog().info(String.format("Add dependency %s as remote packaging module to the module %s",
                    remotePackagingProjectDependency, project));
            PomManipulator.addDependency(project.getFile(), remotePackagingProjectDependency, getLog());
          }
        }
      }
    }
  }

  private boolean containsDependency(List<Dependency> dependencies, Dependency dependencyToCheck) {
    for (Dependency dependency : dependencies) {
      if (dependency.getGroupId().equals(dependencyToCheck.getGroupId())
              && dependency.getArtifactId().equals(dependencyToCheck.getArtifactId())) {
        return true;
      }
    }
    return false;
  }

  private Dependency getRemotePackagingProjectAsDependency(@Nonnull MavenProject remotePackagesProject) {
    Dependency dependency = null;

    if (remotePackagesProject.getGroupId() != null || remotePackagesProject.getArtifactId() != null) {
      dependency = new Dependency();
      dependency.setArtifactId(remotePackagesProject.getArtifactId());
      dependency.setGroupId(remotePackagesProject.getGroupId());
      dependency.setVersion("${project.version}");
      dependency.setType("pom");
    }
    return dependency;
  }

  private String getPathRelativeToCurrentProjectFrom(String pathFromProperty, MavenProject remotePackages)
          throws MojoExecutionException {
    Path absolutePathToCurrentProject = getProject().getBasedir().toPath().normalize();
    String remotePackagesDir = (String) remotePackages.getProperties().get(pathFromProperty);
    if (remotePackagesDir == null) {
      remotePackagesDir = getProject().getBuild().getDirectory() + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_PACKAGES; // TODO is this a good constant?
    }
    Path absolutePathFromProperty = Paths.get(remotePackagesDir).normalize();
    return absolutePathToCurrentProject.relativize(absolutePathFromProperty).toString();
  }

  private Path normalizePath(Path path) throws MojoExecutionException {
    try {
      if (!Files.exists(path)) {
        Files.createDirectories(path);
      }
      return path.toRealPath();
    } catch (IOException e) {
      throw new MojoExecutionException("path could not be normalized: " + path, e);
    }
  }


}
