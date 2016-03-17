/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaWorkspaceHelper;
import net.jangaroo.jooc.mvnplugin.util.PomManipulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
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
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-sencha-workspace",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true, aggregator = true)
public class SenchaWorkspaceMojo extends AbstractMojo {

  @Parameter(defaultValue = "${session}")
  private MavenSession session;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * The sencha configuration to use.
   */
  @Parameter(property = "senchaConfiguration")
  private MavenSenchaConfiguration senchaConfiguration;


  public void execute() throws MojoExecutionException, MojoFailureException {

    senchaConfiguration.setType(Type.WORKSPACE);
    senchaConfiguration.setProjectBuildDir(project.getBuild().getDirectory());

    String remotePackages = senchaConfiguration.getRemotePackagesArtifact();
    if (remotePackages != null) {

      MavenProject remotePackagesProject = getRemotePackagesProject(session, remotePackages);

      // add location of dirs to sencha configuration
      addDirectoryLocations(remotePackagesProject);

      // updates dependencies to remote packages in remote packages aggregator
      updateRemotePackages();

      // add remote packaging module to all jangaroo modules that do not contain this dependency
      addRemotePackagesProject(remotePackagesProject);
    }

    // we need to create the sencha module
    createAndPrepareSenchaModule();

  }

  private void createAndPrepareSenchaModule() throws MojoExecutionException {
    // for now:
    SenchaHelper senchaHelper = new SenchaWorkspaceHelper(project, senchaConfiguration, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
  }

  private void addDirectoryLocations(MavenProject remotePackagesProject) throws MojoExecutionException {
    String remotePackagesPath = getPathRelativeToCurrentProjectFrom(SenchaRemotePackagesMojo.getRemotePackagesDirectory(remotePackagesProject));
    senchaConfiguration.setPackagesDir(remotePackagesPath);

    String extPath = getPathRelativeToCurrentProjectFrom(SenchaRemotePackagesMojo.getExtFrameworkDirectory(remotePackagesProject));
    senchaConfiguration.setExtFrameworkDir(extPath);
  }

  /**
   * Returns the configured remote packages project or the current project if none is configured.
   *
   * @param session               the current session
   * @param remotePackageArtifactId the identifier of the configured remote packages project
   * @return the configured remote packages project or the current project if none is configured
   * @throws MojoExecutionException
   */
  @Nonnull
  private MavenProject getRemotePackagesProject(@Nonnull MavenSession session,
                                               @Nonnull String remotePackageArtifactId)
          throws MojoExecutionException {

    if (StringUtils.isEmpty(remotePackageArtifactId)) {
      return session.getCurrentProject();
    }
    List<MavenProject> allReactorProjects = session.getProjects();
    for (MavenProject project : allReactorProjects) {
      String projectId = project.getId();
      // the project id has the following format <groupid>:<artifactid>:<packaging>:<version> while
      // the remotePackageArtifactId usually looks as follows <groupid>:<artifactid>
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
  public void updateRemotePackages() throws MojoExecutionException {
    long startTime = System.nanoTime();
    MavenProject remoteAggregatorProject = null;
    // we need to use projects in this set, because the class dependency does not have an equals method
    Set<MavenProject> remotePackagesProjects = new TreeSet<>(new MavenProjectComparator());

    List<MavenProject> collectedProjects = project.getCollectedProjects();

    // check all collected projects for packaging type jangaroo
    for (MavenProject currentProject : collectedProjects) {
      if (Type.containsJangarooSources(currentProject)) {
        // check all dependencies of this project, do they contain remote dependencies
        for (Dependency dependency : currentProject.getDependencies()) {
          if (SenchaUtils.isActualSenchaDependency(dependency, senchaConfiguration)) {
            MavenProject projectFromDependency = createProjectFromDependency(dependency);
            if (!remotePackagesProjects.contains(projectFromDependency) && !collectedProjects.contains(projectFromDependency)) {
              // add dependency to this project for remote packaging
              remotePackagesProjects.add(projectFromDependency);
            }
          }
        }
      }

      if (isRemoteAggregator(currentProject)) {
        remoteAggregatorProject = currentProject;
      }
    }

    if (remoteAggregatorProject == null) {
      throw new MojoExecutionException("Could not find remote packages aggregator");
    }

    // we need this as list of dependencies
    List<Dependency> dependencies = Lists.newArrayList(
            createDependencies(ImmutableList.copyOf(remotePackagesProjects), remoteAggregatorProject));

    // update the dependencies part of the project pom
    PomManipulator.updateDependencies(remoteAggregatorProject, dependencies, getLog());

    getLog().debug(String.format("Needed %d ns to update remotes for project %s", System.nanoTime() - startTime, project));
  }

  /**
   * Adds a dependency to the remote packages aggregator to all jangaroo modules that do not have it
   *
   * @param remotePackagesProject the remote packages aggregator to use
   */
  private void addRemotePackagesProject(@Nonnull MavenProject remotePackagesProject) throws MojoExecutionException {

    Dependency remotePackagingProjectDependency = getRemotePackagingProjectAsDependency(remotePackagesProject);
    if (null != remotePackagingProjectDependency) {
      List<MavenProject> allReactorProjects = session.getProjects();
      for (MavenProject project : allReactorProjects) {
        if (Type.containsJangarooSources(project)) {
          List<Dependency> projectDependencies = project.getDependencies();
          if (!containsDependency(projectDependencies, remotePackagingProjectDependency)) {
            getLog().info(String.format("Add dependency %s as remote packaging module to the module %s",
                    remotePackagingProjectDependency, project));
            PomManipulator.addDependency(project, remotePackagingProjectDependency, getLog());
          }
        }
      }
    }
  }

  /**
   * Dependency does not implement an equals method, so we need to check it the hard way.
   * Only considers group id and artifact id
   *
   * @param dependencies      a list of depedencies
   * @param dependencyToCheck a dependency which we want to check
   * @return whether the given dependency is contained in the given dependencies list
   */
  private boolean containsDependency(@Nonnull List<Dependency> dependencies, @Nonnull Dependency dependencyToCheck) {
    for (Dependency dependency : dependencies) {
      if (dependency.getGroupId().equals(dependencyToCheck.getGroupId())
              && dependency.getArtifactId().equals(dependencyToCheck.getArtifactId())) {
        return true;
      }
    }
    return false;
  }

  @Nullable
  private Dependency getRemotePackagingProjectAsDependency(@Nonnull MavenProject remotePackagesProject) {
    Dependency dependency = null;
    if (remotePackagesProject.getGroupId() != null && remotePackagesProject.getArtifactId() != null) {
      dependency = createDepedency(remotePackagesProject.getGroupId(), remotePackagesProject.getArtifactId(), "pom", "${project.version}");
    }
    return dependency;
  }

  /**
   * Returns the given dependency as {@link MavenProject}
   *
   * @param dependency the dependency to convert to a {@link MavenProject}
   * @return the given dependency as {@link MavenProject}
   */
  @Nonnull
  private MavenProject createProjectFromDependency(@Nonnull Dependency dependency) {
    MavenProject mavenProject = new MavenProject();
    mavenProject.setArtifactId(dependency.getArtifactId());
    mavenProject.setGroupId(dependency.getGroupId());
    mavenProject.setVersion(dependency.getVersion());

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

    Dependency dependency = createDepedency(mavenProject);

    List<Dependency> dependencyList = remoteAggregator.getDependencyManagement().getDependencies();
    for (Dependency dependencyFromList : dependencyList) {
      if (dependencyFromList.getGroupId().equals(dependency.getGroupId())
              && dependencyFromList.getArtifactId().equals(dependency.getArtifactId())
              && dependencyFromList.getVersion().equals(dependency.getVersion())) {
        // should add a warning if version differs
        dependency.setVersion(null);
        break;
      }
    }

    dependency.setScope(Artifact.SCOPE_COMPILE);
    dependency.setType(SenchaUtils.PACKAGE_EXTENSION);

    return dependency;
  }

  private static Dependency createDepedency(@Nonnull MavenProject mavenProject) {
    return createDepedency(mavenProject.getGroupId(), mavenProject.getArtifactId(), null, mavenProject.getVersion());
  }

  private static Dependency createDepedency(String groupId, String artifactId, String type, String version) {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(artifactId);
    dependency.setGroupId(groupId);
    dependency.setType(type);
    dependency.setVersion(version);
    return dependency;
  }

  private String getPathRelativeToCurrentProjectFrom(String remotePackagePath)
          throws MojoExecutionException {
    Path absolutePathToCurrentProject = project.getBasedir().toPath().normalize();
    Path absolutePathFromProperty = Paths.get(remotePackagePath).normalize();
    return absolutePathToCurrentProject.relativize(absolutePathFromProperty).toString();
  }

  private boolean isRemoteAggregator(MavenProject project) {
    String remotePackageArtifactId = senchaConfiguration.getRemotePackagesArtifact();
    String projectId = project.getId();
    return projectId.startsWith(remotePackageArtifactId);
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
