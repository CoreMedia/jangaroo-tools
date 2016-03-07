/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

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
public class SenchaWorkspaceMojo extends AbstractMojo {

  @Parameter(defaultValue = "${session}")
  private MavenSession session;

  @Parameter(defaultValue = "${project}")
  private MavenProject project;

  /**
   * The sencha configuration to use.
   */
  @Parameter(property = "senchaConfiguration")
  private SenchaConfiguration senchaConfiguration;

  /**
   * Defines
   */
  @Parameter(property = "remotePackagesArtifact")
  private ArtifactItem remotePackagesArtifact;

  public void execute() throws MojoExecutionException, MojoFailureException {

    MavenProject remotePackagesProject = getRemotePackagesProject();

    String remotePackagesPath = getPathRelativeToCurrentProjectFrom("remote.packages.dir", remotePackagesProject);
    senchaConfiguration.setPackagesDir(remotePackagesPath);

    String extPath = getPathRelativeToCurrentProjectFrom("ext.dir", remotePackagesProject);
    senchaConfiguration.setExtFrameworkDir(extPath);

    senchaConfiguration.setType(SenchaConfiguration.Type.WORKSPACE);

    // for now:
    SenchaHelper senchaHelper = new SenchaModuleHelper(project, senchaConfiguration, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();

  }

  private MavenProject getRemotePackagesProject() throws MojoExecutionException {
    // TODO for some reason, the remotePackagesArtifact is set, but not the group or artifact id
    if (remotePackagesArtifact.groupId == null || remotePackagesArtifact.artifactId == null) {
      return project;
    }
    List<MavenProject> allReactorProjects = session.getProjects();
    for (MavenProject project : allReactorProjects) {

      if (project.getGroupId().equals(remotePackagesArtifact.groupId)
              && project.getArtifactId().equals(remotePackagesArtifact.artifactId)) {
        return project;
      }

    }
    throw new MojoExecutionException("Could not find local remote-packages module with coordinates " + remotePackagesArtifact.groupId + ":" + remotePackagesArtifact.artifactId);
  }

  private String getPathRelativeToCurrentProjectFrom(String pathFromProperty, MavenProject remotePackages) throws MojoExecutionException {
    Path absolutePathToCurrentProject = project.getBasedir().toPath().normalize();
    String remotePackagesDir = (String) remotePackages.getProperties().get(pathFromProperty);
    if (remotePackagesDir == null) {
      remotePackagesDir = project.getBuild().getDirectory() + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_PACKAGES; // TODO is this a good constant?
    }
    Path absolutePathFromProperty = Paths.get(remotePackagesDir).normalize();
    return absolutePathToCurrentProject.relativize(absolutePathFromProperty).toString();
  }

  public static final class ArtifactItem {

    @Parameter(defaultValue = "com.coremedia.cms")
    private String groupId;

    @Parameter(defaultValue = "remote-packages")
    private String artifactId;


    public void setGroupId(String groupId) {
      this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
    }
  }

}
