package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.regex.Pattern;


public abstract class AbstractSenchaMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  protected MavenSession session;

  @Parameter
  private String toolkit = SenchaUtils.TOOLKIT_CLASSIC;

  @Parameter(defaultValue = "${project.groupId}:${project.artifactId}")
  private String remotePackagesArtifact;

  /**
   * a regexp matching the group and artifact id of a sencha framewrok artifact
   */
  @Parameter(defaultValue = "((net\\.jangaroo\\.com)|(com\\.coremedia))\\.sencha:ext-js(-pkg)?(-gpl)?")
  private String extFrameworkArtifactRegexp;

  /**
   * The log level to use for Sencha Cmd.
   * The log level for Maven is kind of the base line which determines which log entries are actually shown in the output.
   * When you Maven log level is "info", no "debug" messages for Sencha Cmd are logged.
   * If no log level is given, the Maven log level will be used.
   */
  @Parameter(property = "senchaLogLevel")
  private String senchaLogLevel;

  private volatile Pattern extFrameworkArtifactPattern;

  // ***********************************************************************
  // ************************* GETTERS *************************************
  // ***********************************************************************

  public String getToolkit() {
    return toolkit;
  }

  public Pattern getExtFrameworkArtifactPattern() {
    if (extFrameworkArtifactPattern == null) {
      extFrameworkArtifactPattern = Pattern.compile(getExtFrameworkArtifactRegexp());
    }
    return extFrameworkArtifactPattern;
  }

  public String getExtFrameworkArtifactRegexp() {
    return extFrameworkArtifactRegexp;
  }

  public String getRemotePackagesArtifact() {
    return remotePackagesArtifact;
  }

  // ***********************************************************************
  // ************************* SETTERS *************************************
  // ***********************************************************************

  public String getSenchaLogLevel() {
    return senchaLogLevel;
  }

  protected boolean isExtFrameworkArtifact(Artifact artifact) {
    return isExtFramework(artifact.getGroupId(), artifact.getArtifactId());
  }

  protected boolean isExtFrameworkDependency(Dependency dependency) {
    return isExtFramework(dependency.getGroupId(), dependency.getArtifactId());
  }

  private boolean isExtFramework(String groupId, String artifactId) {
    String key = groupId + ":" + artifactId;
    return getExtFrameworkArtifactPattern().matcher(key).matches();
  }

  /**
   * Returns the configured remote packages project or the current project if none is configured.
   *
   * @return the configured remote packages project or the current project if none is configured
   * @throws MojoExecutionException
   */
  @Nonnull
  protected MavenProject getRemotePackagesProject()
          throws MojoExecutionException {
    String remotePackageArtifactId = getRemotePackagesArtifact();
    if (StringUtils.isEmpty(remotePackageArtifactId)) {
      return session.getCurrentProject();
    }
    Collection<MavenProject> allReactorProjects = session.getCurrentProject().getProjectReferences().values();
    for (MavenProject reactorProject : allReactorProjects) {
      if (isRemoteAggregator(reactorProject)) {
        return reactorProject;
      }
    }
    throw new MojoExecutionException("Could not find local remote-packages module with coordinates "
            + remotePackageArtifactId);
  }

  private boolean isRemoteAggregator(@Nonnull MavenProject project) {
    Dependency dependency = MavenDependencyHelper.fromProject(project);
    Dependency remotePackagesDependency = MavenDependencyHelper.fromKey(getRemotePackagesArtifact());
    return MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,remotePackagesDependency);
  }
}
