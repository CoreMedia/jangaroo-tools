package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
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
import java.util.HashMap;
import java.util.Map;
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

  private Map<String, MavenProject> mavenProjectByDependencyCache = new HashMap<>();

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
  MavenProject createProjectFromDependency(@Nonnull Dependency dependency) throws MojoExecutionException {
    String dependencyKey = dependency.toString();
    if (mavenProjectByDependencyCache.containsKey(dependencyKey)) {
      return mavenProjectByDependencyCache.get(dependencyKey);
    }
    getLog().debug("createProjectFromDependency(" + dependency + ")");
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
      MavenProject project = result.getProject();
      mavenProjectByDependencyCache.put(dependencyKey, project);
      return project;
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Could not resolve required dependencies of POM dependency " + artifactFromDependency, e);
    }
  }

  Dependency findRequiredJangarooAppDependency(MavenProject project) throws MojoExecutionException {
    return project.getDependencies().stream().filter(dependency -> {
      if (Type.JAR_EXTENSION.equals(dependency.getType())) {
        try {
          MavenProject mavenProject = createProjectFromDependency(dependency);
          String packaging = mavenProject.getPackaging();
          if (Type.JANGAROO_APP_PACKAGING.equals(packaging) || Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(packaging)) {
            return true;
          }
        } catch (MojoExecutionException e) {
          // ignore
        }
      }
      return false;
    }).findFirst()
            .orElseThrow(() ->
                    new MojoExecutionException("Module of type " + Type.JANGAROO_APP_OVERLAY_PACKAGING +" must have exactly one dependency on a module of type " + Type.JANGAROO_APP_PACKAGING + ".")
            );
  }

  Artifact getArtifact(Dependency dependency) {
    String versionlessKey = ArtifactUtils.versionlessKey(dependency.getGroupId(), dependency.getArtifactId());
    return project.getArtifactMap().get(versionlessKey);
  }
}
