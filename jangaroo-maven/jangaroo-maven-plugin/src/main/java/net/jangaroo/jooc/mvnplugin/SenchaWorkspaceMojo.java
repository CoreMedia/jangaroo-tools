/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaWorkspaceHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependency;
import net.jangaroo.jooc.mvnplugin.util.PomManipulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-sencha-workspace",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyCollection = ResolutionScope.COMPILE, // TEST needed when using pkgs for testing. see SenchaUtils#isRequireSenchaDependency
        threadSafe = true, aggregator = true)
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

    // add location of dirs to Sencha configuration
    addDirectoryLocations(remotePackagesProject);

    // only update POMs if a separate remote-packages module exists and is configured
    if (remotePackagesFromConfiguration != null) {

      // updates dependencies to remote packages in remote packages aggregator
      updateRemotePackages(remotePackagesProject);

      // add remote packaging module to all jangaroo modules that do not contain this dependency
      addRemotePackagesProject(remotePackagesProject);

    }

    // we need to create the Sencha module
    createAndPrepareSenchaModule();

  }

  private void createAndPrepareSenchaModule() throws MojoExecutionException {
    // for now:
    SenchaHelper senchaHelper = new SenchaWorkspaceHelper(project, this, getLog());
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
    Set<MavenProject> remotePackagesProjects = new TreeSet<>(new MavenProjectComparator());
    for (Artifact artifact : remoteAggregatorProject.getDependencyArtifacts()) {
      MavenProject project = createProjectFromArtifact(artifact);
    }

    List<MavenProject> localProjects = session.getProjects();

    // check all collected projects for packaging type jangaroo
    for (MavenProject localProject : localProjects) {
      if (Type.containsJangarooSources(localProject)) {
        // check all dependencies of this project, do they contain remote dependencies
        collectRemoteDependencies(remotePackagesProjects, localProjects, localProject);
      }
    }

    // remove those dependencies that are already in the remote aggregator
    for (Artifact artifact : remoteAggregatorProject.getDependencyArtifacts()) {
      remotePackagesProjects.remove(createProjectFromArtifact(artifact));
    }

    // we need this as list of dependencies
    List<Dependency> dependencies = Lists.newArrayList(
            createDependencies(ImmutableList.copyOf(remotePackagesProjects), remoteAggregatorProject));

    // update the dependencies part of the project pom
    PomManipulator.updateDependencies(remoteAggregatorProject, dependencies, getLog());

    getLog().debug(String.format("Needed %d ns to update remotes for project %s", System.nanoTime() - startTime, project));
  }

  private void collectRemoteDependencies(Set<MavenProject> remotePackages, List<MavenProject> localProjects, MavenProject currentProject) {
    MavenDependency remotePackageDependency = MavenDependency.fromKey(getRemotePackagesArtifact());
    MavenDependency extFrameworkDependency = MavenDependency.fromKey(getExtFrameworkArtifact());
    for (Artifact artifact : currentProject.getArtifacts()) {

      MavenDependency dependency = MavenDependency.fromArtifact(artifact);
      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)) {
        MavenProject projectFromDependency = createProjectFromArtifact(artifact);

        if (!remotePackages.contains(projectFromDependency) && !localProjects.contains(projectFromDependency)) {
          // add dependency to this project for remote packaging
          getLog().info(String.format("Added remote dependency \"%s\" for project \"%s\"", artifact.getId(), currentProject.getId()));
          remotePackages.add(projectFromDependency);
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

    // check all known projects if they have the jangaroo type
    for (MavenProject project : session.getProjects()) {

      // if the project does not contain the dependency to the remote packages aggregator, add it
      if (!remotesProject.equals(project)
              && Type.containsJangarooSources(project)
              && !containsProject(project.getDependencies(), remotesProject)) {

        Dependency remotesDependency;
        if (!Objects.equals(remotesProject.getVersion(), project.getVersion())) {
          remotesDependency = createDependency(
                  remotesProject.getGroupId(), remotesProject.getArtifactId(), "pom", remotesProject.getVersion());
        } else {
          remotesDependency = createDependency(
                  remotesProject.getGroupId(), remotesProject.getArtifactId(), "pom", "${project.version}");
        }
        remotesDependency.setScope(Artifact.SCOPE_PROVIDED);

        PomManipulator.addDependency(project, remotesDependency, getLog());

        getLog().info(String.format("Add dependency %s as remote packaging module to the module %s",
                remotesDependency, project));
      }

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

  private List<Dependency> createDependencies(@Nonnull List<MavenProject> projects, @Nonnull final MavenProject remoteAggregator) {
    return Lists.transform(projects, new Function<MavenProject, Dependency>() {
      @Nullable
      @Override
      public Dependency apply(@Nullable MavenProject mavenProject) {
        return createDependencyFromProject(mavenProject, remoteAggregator);
      }
    });
  }

  private Dependency createDependencyFromProject(@Nullable MavenProject mavenProject, @Nonnull MavenProject remoteAggregator) {
    if (mavenProject == null) {
      return null;
    }

    Dependency dependency = createDependency(mavenProject);
    if (isDependencyManaged(remoteAggregator, dependency)) {
      dependency.setVersion(null);
    }
    dependency.setType(Type.PACKAGE_EXTENSION);

    return dependency;
  }

  private boolean isDependencyManaged(@Nonnull MavenProject project, @Nonnull final Dependency dependency) {
    if (project.getDependencyManagement() == null) {
      return false;
    }
    final String dependencyVersion = dependency.getVersion();
    return Iterables.tryFind(project.getDependencyManagement().getDependencies(), new Predicate<Dependency>() {
      @Override
      public boolean apply(@Nullable Dependency input) {
        return input != null
                && Objects.equals(input.getArtifactId(), dependency.getArtifactId())
                && Objects.equals(input.getGroupId(), dependency.getGroupId())
                && Objects.equals(input.getVersion(), dependencyVersion);
      }
    }).isPresent();
  }

  private static Dependency createDependency(@Nonnull MavenProject mavenProject) {
    return createDependency(mavenProject.getGroupId(), mavenProject.getArtifactId(), null, mavenProject.getVersion());
  }

  private static Dependency createDependency(String groupId, String artifactId, String type, String version) {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(artifactId);
    dependency.setGroupId(groupId);
    dependency.setType(type);
    dependency.setVersion(version);
    return dependency;
  }

  private String getPathRelativeToCurrentProjectFrom(@Nonnull String remotePackagePath)
          throws MojoExecutionException {
    Path absolutePathToCurrentProject = project.getBasedir().toPath().normalize();
    Path absolutePathFromProperty = Paths.get(remotePackagePath).normalize();
    return absolutePathToCurrentProject.relativize(absolutePathFromProperty).toString();
  }

  private boolean isRemoteAggregator(@Nonnull MavenProject project) {
    MavenDependency dependency = MavenDependency.fromProject(project);
    MavenDependency remotePackagesDependency = MavenDependency.fromKey(getRemotePackagesArtifact());
    return dependency.equalsGroupIdAndArtifactId(remotePackagesDependency);
  }

  /**
   * Used to sort projects by group id, artifact id and version.
   */
  private static class MavenProjectComparator implements Comparator<MavenProject> {

    @Override
    public int compare(MavenProject p1, MavenProject p2) {
      if (!p1.getGroupId().equals(p2.getGroupId())) {
        return p1.getGroupId().compareTo(p2.getGroupId());
      } else if (!p1.getArtifactId().equals(p2.getArtifactId())) {
        return p1.getArtifactId().compareTo(p2.getArtifactId());
      }
      return p1.getVersion().compareTo(p2.getVersion());
    }
  }
}
