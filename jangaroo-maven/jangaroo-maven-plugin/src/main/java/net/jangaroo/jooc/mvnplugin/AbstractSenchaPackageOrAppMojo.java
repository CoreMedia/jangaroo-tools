package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageOrAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

public abstract class AbstractSenchaPackageOrAppMojo<T extends SenchaPackageOrAppConfigBuilder>
        extends AbstractSenchaMojo
        implements SenchaProfileConfiguration {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  @Component
  private ProjectBuilder projectBuilder;

  @Component
  protected ArtifactHandlerManager artifactHandlerManager;

  @Parameter
  private MavenSenchaProfileConfiguration production;

  @Parameter
  private MavenSenchaProfileConfiguration testing;

  @Parameter
  private MavenSenchaProfileConfiguration development;

  @Parameter
  private List<String> requiredClasses;
  @Parameter
  private List<String> additionalCssNonBundle;
  @Parameter
  private List<String> additionalJsNonBundle;
  @Parameter
  private List<String> additionalCssIncludeInBundle;
  @Parameter
  private List<String> additionalJsIncludeInBundle;

  @Parameter
  private String theme;

  @Parameter (defaultValue = "${project.basedir}/src/main/sencha")
  private File senchaSrcDir;

  public abstract String getType();

  public abstract String getJsonConfigFileName();

  protected void configure(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {

    configureMetadata(configBuilder);
    configureRequires(configBuilder);

    configureModule(configBuilder);

    configureProfile(configBuilder, null, this);
    configureProfile(configBuilder, SenchaUtils.PRODUCTION_PROFILE, getProduction());
    configureProfile(configBuilder, SenchaUtils.TESTING_PROFILE, getTesting());
    configureProfile(configBuilder, SenchaUtils.DEVELOPMENT_PROFILE, getDevelopment());

    configureCustomProperties(configBuilder);
  }

  private void configureCustomProperties(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    File jsonFile = new File(getSenchaSrcDir(), getJsonConfigFileName());
    if (jsonFile.exists()) {
      try (FileInputStream fileInputStream = new FileInputStream(jsonFile)) {
        //noinspection unchecked
        configBuilder.namesValues((Map<String, Object>) SenchaUtils.getObjectMapper().readValue(fileInputStream, Map.class));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read json file", e);
      }
    }
  }

  protected void configureMetadata(SenchaPackageOrAppConfigBuilder configBuilder)
          throws MojoExecutionException {

    String version = SenchaUtils.getSenchaVersionForMavenVersion(project.getVersion());
    if (null == version) {
      throw new MojoExecutionException("Could not determine Sencha version from maven version");
    }

    configBuilder.name(getSenchaPackageName(project.getGroupId(), project.getArtifactId()));
    configBuilder.version(version);
    configBuilder.creator(StringUtils.defaultString(project.getOrganization() != null ? project.getOrganization().getName() : ""));
    configBuilder.summary(StringUtils.defaultString(project.getDescription()));
  }


  protected void configureProfile(SenchaPackageOrAppConfigBuilder configBuilder,
                                String profileName,
                                SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    if (null == profileName) {
      configureProfile(senchaProfileConfiguration, configBuilder);
    } else {
      SenchaPackageOrAppConfigBuilder profileConfigBuilder = createSenchaConfigBuilder();
      configureProfile(senchaProfileConfiguration, profileConfigBuilder);
      //noinspection unchecked
      configBuilder.profile(profileName, profileConfigBuilder.build());
    }
  }

  protected static void configureProfile(SenchaProfileConfiguration senchaProfileConfiguration,
                                       SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    addAdditionalResources(configBuilder,
            SenchaPackageOrAppConfigBuilder.CSS,
            senchaProfileConfiguration == null ? Collections.<String>emptyList() : senchaProfileConfiguration.getAdditionalCssNonBundle(),
            senchaProfileConfiguration == null ? Collections.<String>emptyList() : senchaProfileConfiguration.getAdditionalCssIncludeInBundle());

    addAdditionalResources(configBuilder,
            SenchaPackageOrAppConfigBuilder.JS,
            senchaProfileConfiguration == null ? Collections.<String>emptyList() : senchaProfileConfiguration.getAdditionalJsNonBundle(),
            senchaProfileConfiguration == null ? Collections.<String>emptyList() : senchaProfileConfiguration.getAdditionalJsIncludeInBundle());
  }



  protected void configureRequires(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    for (String dependency : getRequiredDependencies()) {
      configBuilder.require(dependency);
    }
  }

  protected void configureModule(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {

    boolean useExtend = false;
    if (Type.CODE.equals(getType())) {
      configBuilder.toolkit(getToolkit());
    }
    if (Type.THEME.equals(getType())) {
      configBuilder.toolkit(getToolkit());
      useExtend = true;
    }

    String themePackageName = getThemePackageName();
    if (org.apache.commons.lang3.StringUtils.isNotBlank(themePackageName)) {
      if (useExtend) {
        ((SenchaPackageConfigBuilder)configBuilder).extend(themePackageName);
      } else {
        configBuilder.theme(themePackageName);
      }
    }

    if (Type.CODE.equals(getType())
            || Type.THEME.equals(getType())) {
      configureResourcesEntry(configBuilder);
    }
  }

  protected void configureResourcesEntry(SenchaPackageOrAppConfigBuilder configBuilder) {
    configBuilder.resource(SenchaUtils.generateAbsolutePathUsingPlaceholder(getType(), SenchaUtils.SENCHA_RESOURCES_PATH));
  }

  private Set<String> getRequiredDependencies() throws MojoExecutionException {
    Set<String> requiredDependencies = new LinkedHashSet<>();

    Dependency themeDependency = SenchaUtils.getThemeDependency(getTheme(), project);

    List<Dependency> projectDependencies = resolveRequiredDependencies(project);

    Dependency remotePackageDependency = MavenDependencyHelper.fromKey(getRemotePackagesArtifact());
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(getExtFrameworkArtifact());
    for (Dependency dependency : projectDependencies) {

      String senchaPackageNameForArtifact = getSenchaPackageName(dependency.getGroupId(), dependency.getArtifactId());

      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)
              && !MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,themeDependency)) {

        requiredDependencies.add(senchaPackageNameForArtifact);
      }

    }

    return requiredDependencies;
  }

  @Nonnull
  private List<Dependency> resolveRequiredDependencies(@Nonnull MavenProject project) throws MojoExecutionException {
    List<Dependency> resolvedDependencies = new ArrayList<>();
    for (Dependency dependency : project.getDependencies()) {
      if (Type.POM_PACKAGING.equalsIgnoreCase(dependency.getType())) {
        MavenProject projectFromPom = createProjectFromPomDependency(dependency);
        List<Dependency> fromPomDependencies = resolveRequiredDependencies(projectFromPom);
        if (!fromPomDependencies.isEmpty()) {
          resolvedDependencies.addAll(fromPomDependencies);
        }

      } else {
        resolvedDependencies.add(dependency);
      }
    }
    return resolvedDependencies;
  }

  @Nonnull
  private MavenProject createProjectFromPomDependency(@Nonnull Dependency dependency) throws MojoExecutionException {
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

  protected static void addAdditionalResources(SenchaPackageOrAppConfigBuilder configBuilder,
                                               String resourceType,
                                               List<String> resourcesNonBundle,
                                               List<String> resourcesIncludeInBundle) {
    for (String resource : resourcesNonBundle) {
      configBuilder.cssOrJs(resourceType, resource, false, false);
    }

    for (String resource : resourcesIncludeInBundle) {
      configBuilder.cssOrJs(resourceType, resource, false, true);
    }
  }

  @Nonnull
  private String getThemePackageName() {
    Dependency themeDependency = SenchaUtils.getThemeDependency(getTheme(), project);
    String themePackageName;
    if (themeDependency == null) {
      themePackageName = org.apache.commons.lang3.StringUtils.defaultString(getTheme());
      // print a warning if a theme was set but it could not be found in the list of dependencies
      if (getTheme() != null) {
        getLog().warn(String.format("Could not identify theme dependency. Using theme  \"%s\" from configuration instead.",
                themePackageName));
      }
    } else {
      themePackageName = SenchaUtils.getSenchaPackageName(themeDependency.getGroupId(), themeDependency.getArtifactId());
      getLog().info(String.format("Setting theme to \"%s\"", themePackageName));
    }
    return themePackageName;
  }


  protected abstract  T createSenchaConfigBuilder();

  public File getSenchaSrcDir() {
    return senchaSrcDir;
  }

  public SenchaProfileConfiguration getProduction() {
    return production;
  }

  public SenchaProfileConfiguration getDevelopment() {
    return development;
  }

  public SenchaProfileConfiguration getTesting() {
    return testing;
  }

  @Nonnull
  @Override
  public List<String> getRequiredClasses() {
    return requiredClasses == null ? Collections.<String>emptyList() : requiredClasses;
  }

  public String getTheme() {
    return theme;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle != null ? ImmutableList.copyOf(additionalCssIncludeInBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle != null ? ImmutableList.copyOf(additionalJsIncludeInBundle) : Collections.<String>emptyList();
  }
}
