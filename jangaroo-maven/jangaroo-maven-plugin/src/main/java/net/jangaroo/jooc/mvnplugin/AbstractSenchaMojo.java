package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;


public abstract class AbstractSenchaMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  protected MavenSession session;

  @Component
  ArtifactHandlerManager artifactHandlerManager;

  @Component
  private ProjectBuilder projectBuilder;

  @Parameter
  private String toolkit = SenchaUtils.TOOLKIT_CLASSIC;

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

  @Nonnull
  protected MavenProject createProjectFromDependency(@Nonnull Dependency dependency) throws MojoExecutionException {
    Artifact artifactFromDependency = new DefaultArtifact(
            dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getScope(),
            dependency.getType(), dependency.getClassifier(), artifactHandlerManager.getArtifactHandler(dependency.getType())
    );

    ProjectBuildingRequest request = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
    request.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
    request.setProcessPlugins(false);
    request.setResolveDependencies(false);
    try {
      ProjectBuildingResult result = projectBuilder.build(artifactFromDependency, request);
      return result.getProject();
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Could not resolve required dependencies of POM dependency " + artifactFromDependency, e);
    }
  }
}
