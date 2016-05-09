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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME;
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

  protected void addRegisterEditorPluginsResources(String modulePath) throws MojoExecutionException {
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH, commonProfileConfiguration);
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "production", productionProfileConfiguration);
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "testing", testingProfileConfiguration);
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "development", developmentProfileConfiguration);
  }

  private void addRegisterEditorPluginsResource(String modulePath, String resourcesPath, ExtendableProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    List<? extends EditorPluginDescriptor> editorPlugins = senchaProfileConfiguration.getEditorPlugins();
    if (!editorPlugins.isEmpty()) {
      File resource = new File(modulePath + File.separator + resourcesPath + File.separator + REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME);
      if (resource.exists()) {
        getLog().warn("resource file for editor plugins already exists, deleting...");
        if (!resource.delete()) {
          throw new MojoExecutionException("Could not delete resource file for editor plugins");
        }
      }

      try (PrintWriter pw = new PrintWriter(new FileWriter(resource, true))) {

        for (EditorPluginDescriptor editorPlugin : editorPlugins) {
          if (null == editorPlugin.getMainClass()) {
            getLog().warn("EditorPluginDescriptor without mainClass was ignored.");
            continue;
          }
          String name = editorPlugin.getName();
          if (null == name) {
            name = getPluginName(editorPlugin.getMainClass());
          }
          pw.println("coremediaEditorPlugins.push({");
          // optional parameters are added first so they can always be followed by a comma
          if (null != editorPlugin.getRequiredLicenseFeature()) {
            pw.println("\trequiredLicenseFeature: \"" + StringUtils.escape(editorPlugin.getRequiredLicenseFeature()) + "\",");
          }
          if (null != editorPlugin.getRequiredGroup()) {
            pw.println("\trequiredGroup: \"" + StringUtils.escape(editorPlugin.getRequiredGroup()) + "\",");
          }
          pw.println("\tname: \"" + StringUtils.escape(name) + "\",");
          pw.println("\tmainClass: \"" + StringUtils.escape(editorPlugin.getMainClass()) + "\"");

          pw.println("});");
        }
      } catch (IOException e) {
        throw new MojoExecutionException("could not create editor plugins resource");
      }
      senchaProfileConfiguration.addAdditionalJsIncludeInBundle(resourcesPath + SenchaUtils.SEPARATOR + REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME);
    }
  }

  protected void addRequireEditorPluginsResource(String modulePath) throws MojoExecutionException {
    // collect normal and build editor plugins
    List<EditorPluginDescriptor> relevantEditorPlugins = new ArrayList<>();
    relevantEditorPlugins.addAll(getSenchaConfiguration().getEditorPlugins());
    if (null != getSenchaConfiguration().getProduction()) {
      relevantEditorPlugins.addAll(getSenchaConfiguration().getProduction().getEditorPlugins());
    }
    if (!relevantEditorPlugins.isEmpty()) {

      File resource = new File(modulePath + File.separator + SENCHA_RESOURCES_PATH + File.separator + REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME);
      if (resource.exists()) {
        getLog().warn("resource file for require editor plugins already exists, deleting...");
        if (!resource.delete()) {
          throw new MojoExecutionException("Could not delete resource file for require editor plugins");
        }
      }

      try (PrintWriter pw = new PrintWriter(new FileWriter(resource, true))) {

        for (EditorPluginDescriptor editorPlugin : relevantEditorPlugins) {
          if (null == editorPlugin.getMainClass()) {
            getLog().warn("EditorPluginDescriptor without mainClass was ignored.");
            continue;
          }
          String editorPluginCompiledClassName = "AS3." + editorPlugin.getMainClass();
          pw.println("Ext.require(\"" + StringUtils.escape(editorPluginCompiledClassName) + "\");");
        }
      } catch (IOException e) {
        throw new MojoExecutionException("could not append skip.sass and skip.slice to Sencha config of package");
      }
      productionProfileConfiguration.addAdditionalJsIncludeInBundle(SenchaUtils.SENCHA_RESOURCES_PATH + SenchaUtils.SEPARATOR + REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME);
    }
  }

  private String getPluginName(String editorPlugin) {
    String editorPluginName = "No name";
    String[] parts = editorPlugin.split("\\.");
    if (parts.length > 0) {
      editorPluginName = parts[parts.length - 1];
    }
    return editorPluginName;
  }

  protected String getSenchaModuleName() {
    return senchaModuleName;
  }

  protected SenchaProfileConfiguration getCommonProfileConfiguration() {
    return commonProfileConfiguration;
  }

  public SenchaProfileConfiguration getProductionProfileConfiguration() {
    return productionProfileConfiguration;
  }

  public SenchaProfileConfiguration getTestingProfileConfiguration() {
    return testingProfileConfiguration;
  }

  public SenchaProfileConfiguration getDevelopmentProfileConfiguration() {
    return developmentProfileConfiguration;
  }

  @Override
  protected void configure(U configBuilder) throws MojoExecutionException {
    configBuilder.type(Type.THEME.equals(getSenchaConfiguration().getType()) ? "theme" : "code");

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

    configBuilder.resource(SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), SenchaUtils.SENCHA_RESOURCES_PATH));

    if (senchaConfiguration instanceof SenchaPackageConfiguration) {
      SenchaPackageConfiguration packageConfiguration = (SenchaPackageConfiguration) senchaConfiguration;
      if (Type.CODE.equals(senchaConfiguration.getType()) && packageConfiguration.isShareResources()
              || Type.THEME.equals(senchaConfiguration.getType()) && !packageConfiguration.isIsolateResources()) {
        ((SenchaPackageConfigBuilder)configBuilder).shareResources();
      }
    }
  }


  private void configureRequires(U configBuilder) throws MojoExecutionException {
    for (String dependency : getRequiredDependencies()) {
      configBuilder.require(dependency);
    }
  }

  private Set<String> getRequiredDependencies() throws MojoExecutionException {
    MavenProject project = getProject();
    SenchaConfiguration senchaConfiguration = getSenchaConfiguration();
    Set<String> requiredDependencies = new HashSet<>();

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
