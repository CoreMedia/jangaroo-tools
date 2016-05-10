package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageOrAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_OVERRIDES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * A set of helper functions for handling Sencha package structure.
 */
abstract class SenchaPackageOrAppHelper<T extends SenchaConfiguration, U extends SenchaPackageOrAppConfigBuilder> extends AbstractSenchaHelper<T, U> {

  static final String PRODUCTION = "production";
  static final String TESTING = "testing";
  static final String DEVELOPMENT = "development";

  private static final String SENCHA_SRC_PATH = "/src/main/sencha/";
  protected static final String SENCHA_CLASS_PATH = "/src";
  protected static final String SENCHA_APP_CLASS_PATH = "/app";

  private final String senchaModuleName;
  private final ExtendableProfileConfiguration commonProfileConfiguration;
  private final ExtendableProfileConfiguration productionProfileConfiguration;
  private final ExtendableProfileConfiguration testingProfileConfiguration;
  private final ExtendableProfileConfiguration developmentProfileConfiguration;

  public SenchaPackageOrAppHelper(MavenProject project, T senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);
    this.senchaModuleName = getSenchaPackageName(project.getGroupId(), project.getArtifactId());
    this.commonProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration);
    this.productionProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getProduction());
    this.testingProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getTesting());
    this.developmentProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getDevelopment());
  }

  protected void copyFilesFromSrc(String path) throws MojoExecutionException {
    File srcDir = new File(getProject().getBasedir() + SENCHA_SRC_PATH);
    File targetDir = new File(path);
    if (srcDir.exists()) {
      try {
        FileUtils.copyDirectory(srcDir, targetDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Copying sencha sources from %s to %s failed.", srcDir, targetDir ), e);
      }
    }
  }

  private void copyFilesFromJoo(String path) throws MojoExecutionException {
    File jangarooResourcesDir = new File(getProject().getBuild().getDirectory() + "/classes/META-INF/resources");
    File senchaResourcesDir = new File(path + File.separator + SENCHA_RESOURCES_PATH);
    if (jangarooResourcesDir.exists()) {
      try {
        FileUtils.copyDirectory(jangarooResourcesDir, senchaResourcesDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could not copy resources from %s to %s", jangarooResourcesDir, senchaResourcesDir), e);
      }
    }
    String senchaClassPath = Type.APP.equals(getSenchaConfiguration().getType()) ? SENCHA_APP_CLASS_PATH : SENCHA_CLASS_PATH;
    move(senchaResourcesDir, "joo/classes", path, senchaClassPath);
    move(senchaResourcesDir, "joo/overrides", path, SENCHA_OVERRIDES_PATH);
  }

  private void move(File sourceBaseDir, String subdir, String targetBasePath, String targetSubdir) throws MojoExecutionException {
    File sourceDir = new File(sourceBaseDir.getAbsolutePath() + File.separator + subdir);
    if (sourceDir.exists()) {
      File targetDir = new File(targetBasePath + File.separator + targetSubdir);
      try {
        // FileUtils.move fails if directory already exists
        FileUtils.copyDirectory(sourceDir, targetDir);
        FileUtils.deleteDirectory(sourceDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could not move files from %s to %s", sourceDir, targetDir), e);
      }
    }
  }

  protected void copyFiles(String path) throws MojoExecutionException {
    copyFilesFromSrc(path);
    copyFilesFromJoo(path);
  }

  protected String getSenchaModuleName() {
    return senchaModuleName;
  }

  protected ExtendableProfileConfiguration getCommonProfileConfiguration() {
    return commonProfileConfiguration;
  }

  public ExtendableProfileConfiguration getProductionProfileConfiguration() {
    return productionProfileConfiguration;
  }

  public ExtendableProfileConfiguration getTestingProfileConfiguration() {
    return testingProfileConfiguration;
  }

  public ExtendableProfileConfiguration getDevelopmentProfileConfiguration() {
    return developmentProfileConfiguration;
  }

  @Override
  protected void configure(U configBuilder) throws MojoExecutionException {
    configureMetadata(configBuilder);

    configureRequires(configBuilder);

    configureModule(configBuilder);

    configureProfile(configBuilder, getCommonProfileConfiguration(), null);
    configureProfile(configBuilder, getProductionProfileConfiguration(), PRODUCTION);
    configureProfile(configBuilder, getTestingProfileConfiguration(), TESTING);
    configureProfile(configBuilder, getDevelopmentProfileConfiguration(), DEVELOPMENT);
  }

  private void configureProfile(SenchaPackageOrAppConfigBuilder configBuilder, SenchaProfileConfiguration profile, String profileName) throws MojoExecutionException {
    configureProfile(configBuilder, profileName, profile);
  }

  private void configureProfile(SenchaPackageOrAppConfigBuilder configBuilder, String profileName, SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    if (null == profileName) {
      configureProfile(senchaProfileConfiguration, configBuilder);
    } else {
      U profileConfigBuilder = createSenchaConfigBuilder();
      configureProfile(senchaProfileConfiguration, profileConfigBuilder);
      configBuilder.profile(profileName, profileConfigBuilder.build());
    }
  }

  private static void configureProfile(SenchaProfileConfiguration senchaProfileConfiguration,
                                         SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    addAdditionalResources(configBuilder, SenchaPackageOrAppConfigBuilder.CSS,
            senchaProfileConfiguration.getAdditionalCssNonBundle(),
            senchaProfileConfiguration.getAdditionalCssIncludeInBundle());

    addAdditionalResources(configBuilder, SenchaPackageOrAppConfigBuilder.JS,
            senchaProfileConfiguration.getAdditionalJsNonBundle(),
            senchaProfileConfiguration.getAdditionalJsIncludeInBundle());
  }

  private static void addAdditionalResources(SenchaPackageOrAppConfigBuilder configBuilder, String resourceType,
                                             List<String> resourcesNonBundle, List<String> resourcesIncludeInBundle) {
    for (String resource : resourcesNonBundle) {
      configBuilder.cssOrJs(resourceType, resource, false, false);
    }

    for (String resource : resourcesIncludeInBundle) {
      configBuilder.cssOrJs(resourceType, resource, false, true);
    }
  }

  private void configureModule(U configBuilder) throws MojoExecutionException {
    SenchaConfiguration senchaConfiguration = getSenchaConfiguration();

    boolean useExtend = false;
    if (Type.CODE.equals(senchaConfiguration.getType())) {
      configBuilder.toolkit(senchaConfiguration.getToolkit());
    }
    if (Type.THEME.equals(senchaConfiguration.getType())) {
      configBuilder.toolkit(senchaConfiguration.getToolkit());
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

    if (Type.CODE.equals(senchaConfiguration.getType())
            || Type.THEME.equals(senchaConfiguration.getType())) {
      configureResourcesEntry(configBuilder);
    }
  }

  @Nonnull
  private String getThemePackageName() {
    T senchaConfiguration = getSenchaConfiguration();
    Dependency themeDependency = SenchaUtils.getThemeDependency(senchaConfiguration.getTheme(), getProject());
    String themePackageName;
    if (themeDependency == null) {
      themePackageName = org.apache.commons.lang3.StringUtils.defaultString(senchaConfiguration.getTheme());
      // print a warning if a theme was set but it could not be found in the list of dependencies
      if (senchaConfiguration.getTheme() != null) {
        getLog().warn(String.format("Could not identify theme dependency. Using theme  \"%s\" from configuration instead.",
                themePackageName));
      }
    } else {
      themePackageName = SenchaUtils.getSenchaPackageName(themeDependency.getGroupId(), themeDependency.getArtifactId());
      getLog().info(String.format("Setting theme to \"%s\"", themePackageName));
    }
    return themePackageName;
  }

  private void configureResourcesEntry(U configBuilder) {
    T senchaConfiguration = getSenchaConfiguration();

    if (senchaConfiguration instanceof SenchaPackageConfiguration) {
      SenchaPackageConfiguration packageConfiguration = (SenchaPackageConfiguration) senchaConfiguration;
      if (Type.CODE.equals(senchaConfiguration.getType()) && packageConfiguration.isShareResources()
              || Type.THEME.equals(senchaConfiguration.getType()) && !packageConfiguration.isIsolateResources()) {
        ((SenchaPackageConfigBuilder)configBuilder).shareResources();
      }
    }

    configBuilder.resource(SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), SenchaUtils.SENCHA_RESOURCES_PATH));
  }


  private void configureRequires(U configBuilder) throws MojoExecutionException {
    for (String dependency : getRequiredDependencies()) {
      configBuilder.require(dependency);
    }
  }

  private Set<String> getRequiredDependencies() throws MojoExecutionException {
    MavenProject project = getProject();
    SenchaConfiguration senchaConfiguration = getSenchaConfiguration();
    Set<String> requiredDependencies = new LinkedHashSet<>();

    Dependency themeDependency = SenchaUtils.getThemeDependency(senchaConfiguration.getTheme(), project);

    List<Dependency> projectDependencies = project.getDependencies();

    Dependency remotePackageDependency = MavenDependencyHelper.fromKey(senchaConfiguration.getRemotePackagesArtifact());
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(senchaConfiguration.getExtFrameworkArtifact());
    for (Dependency dependency : projectDependencies) {

      // TODO we should not assume that #getSenchaPackageNameForArtifact and #getSenchaPackageNameForTheme use the same string format
      String senchaPackageNameForArtifact = getSenchaPackageName(
              dependency.getGroupId(), dependency.getArtifactId()
      );

      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)
              && !MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,themeDependency)) {

        requiredDependencies.add(senchaPackageNameForArtifact);
      }

    }

    return requiredDependencies;
  }

  private void configureMetadata(SenchaPackageOrAppConfigBuilder configBuilder) throws MojoExecutionException {
    MavenProject project = getProject();
    String version = SenchaUtils.getSenchaVersionForMavenVersion(project.getVersion());
    if (null == version) {
      throw new MojoExecutionException("Could not determine Sencha version from maven version");
    }

    configBuilder.name(getSenchaPackageName(project.getGroupId(), project.getArtifactId()));
    configBuilder.version(version);
    configBuilder.creator(StringUtils.defaultString(project.getOrganization() != null ? project.getOrganization().getName() : ""));
    configBuilder.summary(StringUtils.defaultString(project.getDescription()));
  }
}
