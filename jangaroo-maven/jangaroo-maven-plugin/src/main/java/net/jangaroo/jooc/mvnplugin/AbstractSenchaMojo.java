package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  /**
   * Space-separated command line options for the JVM started by Sencha Cmd.
   * Typically, memory settings like <code>-Xms512m -Xmx4096m</code> are added to adapt to JVM memory requirements.
   * This corresponds to running <code>sencha -J-Xms512m -J-Xmx4096m ...</code>
   */
  @Parameter(property = "senchaJvmArgs")
  private String senchaJvmArgs;

  /**
   * The maven coordinates ("groupId:artifactId") an app that should be provided in the root
   * instead of the "apps/${appName}/".
   *
   * Only affects the packaging type "jangaroo-apps".
   */
  @Parameter
  private String rootApp;

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

  public Dependency getRootApp() {
    if (rootApp == null || rootApp.isEmpty()) {
      return null;
    }
    return MavenDependencyHelper.fromKey(rootApp);
  }

  // ***********************************************************************
  // ************************* SETTERS *************************************
  // ***********************************************************************

  public String getSenchaLogLevel() {
    return senchaLogLevel;
  }

  protected String getSenchaJvmArgs() {
    return senchaJvmArgs;
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
  MavenProject getProjectFromDependency(MavenProject project, Dependency dependency) throws MojoExecutionException {
    // fast path: look for dependent project in project's references:
    String key = ArtifactUtils.key(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
    if (project.getProjectReferences().containsKey(key)) {
      return project.getProjectReferences().get(key);
    }
    // expensive path: retrieve MavenProject via Maven's ProjectBuildingRequest:
    return createProjectFromDependency(dependency);
  }

  @Nonnull
  private MavenProject createProjectFromDependency(@Nonnull Dependency dependency) throws MojoExecutionException {
    String dependencyKey = dependency.toString();
    if (mavenProjectByDependencyCache.containsKey(dependencyKey)) {
      return mavenProjectByDependencyCache.get(dependencyKey);
    }
    getLog().debug("createProjectFromDependency(" + dependency + ")");
    Artifact artifactFromDependency = getArtifact(dependency);
    if (artifactFromDependency == null) {
      artifactFromDependency = new DefaultArtifact(
              dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getScope(),
              dependency.getType(), dependency.getClassifier(), artifactHandlerManager.getArtifactHandler(dependency.getType())
      );
    }
    ProjectBuildingRequest request = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
    request.setRemoteRepositories(project.getRemoteArtifactRepositories()); // The artifacts are available repositories defined in the projects - this also covers configured mirrors.
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

  Artifact getArtifact(Dependency dependency) {
    return getArtifact(dependency.getGroupId(), dependency.getArtifactId());
  }

  Artifact getArtifact(MavenProject mavenProject) {
    return getArtifact(mavenProject.getGroupId(), mavenProject.getArtifactId());
  }

  private Artifact getArtifact(String groupId, String artifactId) {
    String versionlessKey = ArtifactUtils.versionlessKey(groupId, artifactId);
    return project.getArtifactMap().get(versionlessKey);
  }

  JangarooApp createJangarooApp(MavenProject project) throws MojoExecutionException {
    String packaging = project.getPackaging();
    if (Type.JANGAROO_APP_PACKAGING.equals(packaging)) {
      return new JangarooApp(project);
    } else if (Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(packaging)) {
      return createJangarooAppOverlay(project);
    }
    return null;
  }

  JangarooAppOverlay createJangarooAppOverlay(MavenProject project) throws MojoExecutionException {
    List<Dependency> dependencies = project.getDependencies();
    for (Dependency dependency : dependencies) {
      if (Type.JAR_EXTENSION.equals(dependency.getType())) {
        // First, use MavenProject from project references, because it is already "evaluated" (${project.baseDir} etc.):
        MavenProject dependentProject = getProjectFromDependency(project, dependency);
        JangarooApp baseApp = createJangarooApp(dependentProject);
        if (baseApp != null) {
          return new JangarooAppOverlay(project, baseApp);
        }
      }
    }
    throw new MojoExecutionException("Module of type " + Type.JANGAROO_APP_OVERLAY_PACKAGING +" must have a dependency on a module of type " + Type.JANGAROO_APP_PACKAGING + " or " + Type.JANGAROO_APP_OVERLAY_PACKAGING + ".");
  }

  JangarooApps createJangarooApps(MavenProject project) throws MojoExecutionException {
    if (Type.JANGAROO_APPS_PACKAGING.equals(project.getPackaging())) {
      Set<JangarooApp> apps = new LinkedHashSet<>();
      List<Dependency> dependencies = project.getDependencies();
      for (Dependency dependency : dependencies) {
        if (Type.JAR_EXTENSION.equals(dependency.getType())) {
          // First, use MavenProject from project references, because it is already "evaluated" (${project.baseDir} etc.):
          MavenProject dependentProject = getProjectFromDependency(project, dependency);
          JangarooApp baseApp = createJangarooApp(dependentProject);
          if (baseApp != null) {
            apps.add(baseApp);
          }
        }
      }
      return new JangarooApps(project, apps);
    }
    return null;
  }

  static class JangarooApp {
    final MavenProject mavenProject;
    Set<Artifact> packages = new LinkedHashSet<>();

    JangarooApp(MavenProject mavenProject) {
      this.mavenProject = mavenProject;
    }
    
    JangarooApp getRootBaseApp() {
      return this;
    }
  }

  static class JangarooAppOverlay extends JangarooApp {
    final JangarooApp baseApp;

    JangarooAppOverlay(MavenProject mavenProject, JangarooApp baseApp) {
      super(mavenProject);
      this.baseApp = baseApp;
    }

    @Override
    JangarooApp getRootBaseApp() {
      return baseApp.getRootBaseApp();
    }

    Set<Artifact> getOwnDynamicPackages() {
      LinkedHashSet<Artifact> ownDynamicPackages = new LinkedHashSet<>(packages);
      ownDynamicPackages.removeAll(baseApp.packages);
      return ownDynamicPackages;
    }

    Set<Artifact> getAllDynamicPackages() {
      LinkedHashSet<Artifact> allDynamicPackages = new LinkedHashSet<>(packages);
      allDynamicPackages.removeAll(getRootBaseApp().packages);
      return allDynamicPackages;
    }
  }

  static class JangarooApps {
    final MavenProject mavenProject;
    final Set<JangarooApp> apps;

    JangarooApps(MavenProject mavenProject, Set<JangarooApp> apps) {
      this.mavenProject = mavenProject;
      this.apps = apps;
    }
  }
}
