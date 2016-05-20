package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageOrAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

public abstract class AbstractSenchaPackageOrAppMojo<T extends SenchaPackageOrAppConfigBuilder> extends AbstractSenchaMojo {

  public static final String PRODUCTION = "production";
  public static final String TESTING = "testing";
  public static final String DEVELOPMENT = "development";

  protected static final String SENCHA_SRC_PATH = "/src/main/sencha/";

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;


  protected void configure(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {

    configureMetadata(configBuilder);
    configureRequires(configBuilder);

    configureModule(configBuilder);

    configureProfile(configBuilder, null, this);
    configureProfile(configBuilder, PRODUCTION, getProduction());
    configureProfile(configBuilder, TESTING, getTesting());
    configureProfile(configBuilder, DEVELOPMENT, getDevelopment());
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

    List<Dependency> projectDependencies = project.getDependencies();

    Dependency remotePackageDependency = MavenDependencyHelper.fromKey(getRemotePackagesArtifact());
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(getExtFrameworkArtifact());
    for (Dependency dependency : projectDependencies) {

      // TODO we should not assume that #getSenchaPackageNameForArtifact and #getSenchaPackageNameForTheme use the same string format
      String senchaPackageNameForArtifact = getSenchaPackageName(dependency.getGroupId(), dependency.getArtifactId());

      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)
              && !MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,themeDependency)) {

        requiredDependencies.add(senchaPackageNameForArtifact);
      }

    }

    return requiredDependencies;
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

  protected static String getPluginName(String editorPlugin) {
    String editorPluginName = "No name";
    String[] parts = editorPlugin.split("\\.");
    if (parts.length > 0) {
      editorPluginName = parts[parts.length - 1];
    }
    return editorPluginName;
  }

  protected static void addConfigurationToSenchaConfig(File senchaCfg) throws MojoExecutionException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(senchaCfg.getAbsoluteFile(), true)) ) {
      pw.println("skip.sass=1");
      pw.println("skip.slice=1");
    } catch (IOException e) {
      throw new MojoExecutionException("Could not append skip.sass and skip.slice to sencha.cfg of package", e);
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

}
