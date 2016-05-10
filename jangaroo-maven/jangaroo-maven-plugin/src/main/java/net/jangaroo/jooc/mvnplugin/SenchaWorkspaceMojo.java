/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaWorkspaceHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import net.jangaroo.jooc.mvnplugin.util.PomManipulator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mojo that generates a Sencha workspace in the current project's directory. If this is an aggregator pom then the
 * project's configuration will also contain the paths to local Sencha packages and apps.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-sencha-workspace",
        requiresDependencyCollection = ResolutionScope.TEST,
        threadSafe = true)
public class SenchaWorkspaceMojo extends AbstractSenchaMojo {

  @Parameter(defaultValue = "${session}")
  private MavenSession session;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Override
  public String getType() {
    return Type.WORKSPACE;
  }

  public void execute() throws MojoExecutionException, MojoFailureException {

    String remotePackagesFromConfiguration = getRemotePackagesArtifact();
    MavenProject remotePackagesProject = getRemotePackagesProject(remotePackagesFromConfiguration);

    if (isWorkspaceDir()) {

      // add location of dirs to Sencha configuration
      addDirectoryLocations(remotePackagesProject);

      // updates dependencies to remote packages in remote packages aggregator
      updateRemotePackages(remotePackagesProject);

      // add remote packaging module to all jangaroo modules that do not contain this dependency
      addRemotePackagesProject(remotePackagesProject);

      // we need to create the Sencha module
      createAndPrepareSenchaModule();

    } else {

      // add remote packaging module to all jangaroo modules that do not contain this dependency
      addRemotePackagesProject(remotePackagesProject);

    }
  }

  private void createAndPrepareSenchaModule() throws MojoExecutionException {
    // for now:
    SenchaWorkspaceHelper senchaHelper = new SenchaWorkspaceHelper(project, this, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
  }

  private void addDirectoryLocations(MavenProject remotePackagesProject) throws MojoExecutionException {
    String remotePackagesPath = getPathRelativeToCurrentProjectFrom(
            SenchaRemotePackagesMojo.getRemotePackagesDirectory(remotePackagesProject)
    );
    setPackagesDir(remotePackagesPath);

    String extPath = getPathRelativeToCurrentProjectFrom(
            SenchaRemotePackagesMojo.getExtFrameworkDirectory(remotePackagesProject)
    );
    setExtFrameworkDir(extPath);
  }

  private boolean isWorkspaceDir() {
    return project.equals(session.getTopLevelProject());
  }

  /**
   * Returns the configured remote packages project or the current project if none is configured.
   *
   * @param remotePackageArtifactId the identifier of the configured remote packages project
   * @return the configured remote packages project or the current project if none is configured
   * @throws MojoExecutionException
   */
  @Nonnull
  private MavenProject getRemotePackagesProject(@Nullable String remotePackageArtifactId)
          throws MojoExecutionException {

    if (StringUtils.isEmpty(remotePackageArtifactId)) {
      return session.getCurrentProject();
    }
    List<MavenProject> allReactorProjects = session.getProjects();
    for (MavenProject project : allReactorProjects) {
      String projectId = project.getId();
      if (isRemoteAggregator(project)) {
        return project;
      }

    }
    throw new MojoExecutionException("Could not find local remote-packages module with coordinates "
            + remotePackageArtifactId);
  }

  /**
   * Updates the dependencies to all remote packages of this project in the remote packages aggregator.
   *
   * @throws MojoExecutionException
   */
  public void updateRemotePackages(MavenProject remoteAggregatorProject) throws MojoExecutionException {
    long startTime = System.nanoTime();

    // we need to use projects in this set, because the class dependency does not have an equals method
    List<Dependency> remotePackagesDependencies = new ArrayList<>();
    List<MavenProject> localProjects = session.getProjects();

    // check all collected projects for packaging type jangaroo
    for (MavenProject localProject : localProjects) {
      if (Type.containsJangarooSources(localProject)) {
        // check all dependencies of this project, do they contain remote dependencies
        collectRemoteDependencies(remotePackagesDependencies, localProjects, localProject, remoteAggregatorProject);
      }
    }

    // remove those dependencies that are already in the remote aggregator
    for (Artifact artifact : remoteAggregatorProject.getDependencyArtifacts()) {
      final Dependency pkgDependency = convertToPkgDependency( MavenDependencyHelper.fromArtifact(artifact), remoteAggregatorProject );
      MavenDependencyHelper.remove(remotePackagesDependencies, pkgDependency);
    }

    // update the dependencies part of the project pom
    PomManipulator.addDependencies(remoteAggregatorProject, remotePackagesDependencies, getLog());

    getLog().debug(String.format("Needed %d ns to update remotes for project %s", System.nanoTime() - startTime, project));
  }

  private void collectRemoteDependencies(List<Dependency> remotePackages, List<MavenProject> localProjects,
                                         MavenProject currentProject, MavenProject remoteAggregator) {

    Dependency remotePackageDependency = MavenDependencyHelper.fromKey(getRemotePackagesArtifact());
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(getExtFrameworkArtifact());

    for (Artifact artifact : currentProject.getArtifacts()) {
      Dependency dependency = MavenDependencyHelper.fromArtifact(artifact);

      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)) {
        // JAR remote dependencies from jangaroo-apps and jangaroo-pkgs will be added as PKG dependencies
        // to the aggregator POM so they need to be converted
        Dependency pkgDependency = convertToPkgDependency(dependency, remoteAggregator);
        MavenProject projectFromArtifact = createProjectFromArtifact(artifact);

        if (!MavenDependencyHelper.contains(remotePackages, pkgDependency)
                && !localProjects.contains(projectFromArtifact)) {
          // add dependency to this project for remote packaging
          getLog().info(String.format("Using remote dependency \"%s\" from project \"%s\"", artifact.getId(), currentProject.getId()));
          remotePackages.add(pkgDependency);
        }
      }

    }

  }

  /**
   * Adds a dependency to the remote packages aggregator to all jangaroo modules that do not have it
   *
   * @param remotesProject the remote packages aggregator to use
   */
  private void addRemotePackagesProject(@Nonnull MavenProject remotesProject) throws MojoExecutionException {

    // if the project does not contain the dependency to the remote packages aggregator, add it
    if (!remotesProject.equals(project)
            && Type.containsJangarooSources(project)
            && !containsProject(project.getDependencies(), remotesProject)) {

      Dependency remotesDependency;
      if (!Objects.equals(remotesProject.getVersion(), project.getVersion())) {
        remotesDependency = MavenDependencyHelper.createDependency(
                remotesProject.getGroupId(), remotesProject.getArtifactId(), "pom", remotesProject.getVersion());
      } else {
        remotesDependency = MavenDependencyHelper.createDependency(
                remotesProject.getGroupId(), remotesProject.getArtifactId(), "pom", "${project.version}");
      }
      remotesDependency.setScope(Artifact.SCOPE_PROVIDED);

      PomManipulator.addDependency(project, remotesDependency, getLog());

      getLog().info(String.format("Add dependency %s as remote packaging module to the module %s",
              remotesDependency, project));
    }

  }

  /**
   * Dependency does not implement an equals method, so we need to check it the hard way.
   * Only considers group id and artifact id
   *
   * @param dependencies a list of dependencies that might contain the project
   * @param project      the project to test
   * @return whether the given dependency is contained in the given dependencies list
   */
  private boolean containsProject(@Nonnull List<Dependency> dependencies, @Nonnull MavenProject project) {
    for (Dependency dependency : dependencies) {
      if (Objects.equals(dependency.getArtifactId(), project.getArtifactId())
              && Objects.equals(dependency.getGroupId(), project.getGroupId())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the given dependency as {@link MavenProject}
   *
   * @param artifact the dependency to convert to a {@link MavenProject}
   * @return the given dependency as {@link MavenProject}
   */
  @Nonnull
  private MavenProject createProjectFromArtifact(@Nonnull Artifact artifact) {
    MavenProject mavenProject = new MavenProject();
    mavenProject.setArtifactId(artifact.getArtifactId());
    mavenProject.setGroupId(artifact.getGroupId());
    mavenProject.setVersion(artifact.getVersion());

    return mavenProject;
  }

  private Dependency convertToPkgDependency(@Nullable Dependency dependency, @Nonnull MavenProject project) {
    if (dependency == null) {
      return null;
    }

    if (isDependencyManaged(project, dependency)) {
      dependency.setVersion(null);
    }
    dependency.setScope(null);
    dependency.setType(Type.PACKAGE_EXTENSION);

    return dependency;
  }

  private boolean isDependencyManaged(@Nonnull MavenProject project, @Nonnull final Dependency dependency) {
    return project.getDependencyManagement() != null
            && Iterables.tryFind(project.getDependencyManagement().getDependencies(), new Predicate<Dependency>() {
      @Override
      public boolean apply(@Nullable Dependency input) {
        return input != null
                && Objects.equals(input.getArtifactId(), dependency.getArtifactId())
                && Objects.equals(input.getGroupId(), dependency.getGroupId());
      }
    }).isPresent();
  }

  private String getPathRelativeToCurrentProjectFrom(@Nonnull String remotePackagePath)
          throws MojoExecutionException {
    Path absolutePathToCurrentProject = project.getBasedir().toPath().normalize();
    Path absolutePathFromProperty = Paths.get(remotePackagePath).normalize();
    return FilenameUtils.separatorsToUnix(
            absolutePathToCurrentProject.relativize(absolutePathFromProperty).toString()
    );
  }

  private boolean isRemoteAggregator(@Nonnull MavenProject project) {
    Dependency dependency = MavenDependencyHelper.fromProject(project);
    Dependency remotePackagesDependency = MavenDependencyHelper.fromKey(getRemotePackagesArtifact());
    return MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,remotePackagesDependency);
  }

}
