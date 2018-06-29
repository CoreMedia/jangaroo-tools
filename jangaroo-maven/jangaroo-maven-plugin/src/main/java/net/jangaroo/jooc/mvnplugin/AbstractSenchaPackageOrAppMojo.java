package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageOrAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.APP_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.TEST_APP_DIRECTORY_NAME;

public abstract class AbstractSenchaPackageOrAppMojo<T extends SenchaPackageOrAppConfigBuilder>
        extends AbstractSenchaMojo
        implements SenchaProfileConfiguration {

  private static final String SENCHA_FALLBACK_VERSION = "0.0.1";

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

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

  void configure(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
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
    File jsonFile = new File(project.getBasedir(), getJsonConfigFileName());
    if (jsonFile.exists()) {
      try (FileInputStream fileInputStream = new FileInputStream(jsonFile)) {
        //noinspection unchecked
        configBuilder.namesValues((Map<String, Object>) SenchaUtils.getObjectMapper().readValue(fileInputStream, Map.class));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read json file", e);
      }
    }
  }

  private void configureMetadata(SenchaPackageOrAppConfigBuilder configBuilder)
          throws MojoExecutionException {
    String version = SenchaUtils.getSenchaVersionForMavenVersion(project.getVersion());
    if (version == null) {
      version = SENCHA_FALLBACK_VERSION;
      getLog().warn("Could not determine Sencha version from maven version " + project.getVersion() +
              ", falling back to " + version + ".");
    }
    configBuilder.name(getSenchaPackageName(project.getGroupId(), project.getArtifactId()));
    configBuilder.version(version);
    configBuilder.creator(StringUtils.defaultString(project.getOrganization() != null ? project.getOrganization().getName() : ""));
    configBuilder.summary(StringUtils.defaultString(project.getDescription()));
  }


  private void configureProfile(SenchaPackageOrAppConfigBuilder configBuilder,
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

  private static void configureProfile(SenchaProfileConfiguration senchaProfileConfiguration,
                                       SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    addAdditionalResources(configBuilder,
            SenchaPackageOrAppConfigBuilder.CSS,
            senchaProfileConfiguration == null ? Collections.emptyList() : senchaProfileConfiguration.getAdditionalCssNonBundle(),
            senchaProfileConfiguration == null ? Collections.emptyList() : senchaProfileConfiguration.getAdditionalCssIncludeInBundle());

    addAdditionalResources(configBuilder,
            SenchaPackageOrAppConfigBuilder.JS,
            senchaProfileConfiguration == null ? Collections.emptyList() : senchaProfileConfiguration.getAdditionalJsNonBundle(),
            senchaProfileConfiguration == null ? Collections.emptyList() : senchaProfileConfiguration.getAdditionalJsIncludeInBundle());
  }

  private void configureRequires(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    String senchaPackageName = getSenchaPackageName(project);
    getLog().debug("Computing required dependencies for " + senchaPackageName + "...");
    Set<String> compileDependencies = getRequiredDependencies(false);
    for (String dependency : compileDependencies) {
      configBuilder.require(dependency);
      getLog().debug("  adding required dependency " + senchaPackageName + " -> " + dependency);
    }

    Set<String> runtimeDependencies = getRequiredDependencies(true);
    runtimeDependencies.removeAll(compileDependencies);
    writeDynamicPackagesJson(runtimeDependencies);
  }

  private void writeDynamicPackagesJson(Set<String> runtimeDependencies) throws MojoExecutionException {
    getLog().info(String.format("Write %s for module %s.", DYNAMIC_PACKAGES_FILENAME, project.getName()));
    boolean isAppPackaging = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging());
    File workspaceDir = new File(project.getBuild().getDirectory(), isAppPackaging ? APP_DIRECTORY_NAME : TEST_APP_DIRECTORY_NAME);
    File dynamicPackagesFile = new File(workspaceDir, DYNAMIC_PACKAGES_FILENAME);
    if (!dynamicPackagesFile.exists()) {
      FileHelper.ensureDirectory(dynamicPackagesFile.getParentFile());
    } else {
      getLog().debug(DYNAMIC_PACKAGES_FILENAME+ " for module already exists, deleting...");
      if (!dynamicPackagesFile.delete()) {
        throw new MojoExecutionException("Could not delete " + DYNAMIC_PACKAGES_FILENAME + " file for module");
      }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(dynamicPackagesFile))) {
      pw.write(new JsonArray(runtimeDependencies.toArray()).toString(0, 2));
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + DYNAMIC_PACKAGES_FILENAME + " resource", e);
    }
  }

  private void configureModule(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
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

  private void configureResourcesEntry(SenchaPackageOrAppConfigBuilder configBuilder) {
    configBuilder.resource(SenchaUtils.absolutizeToModuleWithPlaceholder(getType(), SenchaUtils.SENCHA_RESOURCES_PATH));
  }

  private Set<String> getRequiredDependencies(boolean includeRuntimeDependencies) throws MojoExecutionException {
    Set<String> requiredDependencies = new LinkedHashSet<>();
    Dependency themeDependency = SenchaUtils.getThemeDependency(getTheme(), project);
    List<Dependency> projectDependencies = resolveRequiredDependencies(project);
    for (Dependency dependency : projectDependencies) {
      String senchaPackageNameForArtifact = getSenchaPackageName(dependency.getGroupId(), dependency.getArtifactId());
      if (!isExtFrameworkDependency(dependency) &&
              !MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,themeDependency) &&
              SenchaUtils.isRequiredSenchaDependency(dependency, false, includeRuntimeDependencies)) {
        requiredDependencies.add(senchaPackageNameForArtifact);
      }
    }
    return requiredDependencies;
  }

  @Nonnull
  private List<Dependency> resolveRequiredDependencies(@Nonnull MavenProject project)
          throws MojoExecutionException {
    List<Dependency> resolvedDependencies = new ArrayList<>();
    for (Dependency dependency : project.getDependencies()) {
      // only resolve POM packages that are not the remote packages artifact
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

  private static void addAdditionalResources(SenchaPackageOrAppConfigBuilder configBuilder,
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

  File getSenchaSrcDir() {
    return senchaSrcDir;
  }

  SenchaProfileConfiguration getProduction() {
    return production;
  }

  SenchaProfileConfiguration getDevelopment() {
    return development;
  }

  public SenchaProfileConfiguration getTesting() {
    return testing;
  }

  @Nonnull
  @Override
  public List<String> getRequiredClasses() {
    return requiredClasses == null ? Collections.emptyList() : requiredClasses;
  }

  public String getTheme() {
    return theme;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : Collections.emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : Collections.emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle != null ? ImmutableList.copyOf(additionalCssIncludeInBundle) : Collections.emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle != null ? ImmutableList.copyOf(additionalJsIncludeInBundle) : Collections.emptyList();
  }
}
