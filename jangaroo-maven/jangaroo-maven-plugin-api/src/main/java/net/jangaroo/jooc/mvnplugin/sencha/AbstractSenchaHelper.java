package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_OVERRIDES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * A set of helper functions for handling Sencha package structure.
 */
abstract class AbstractSenchaHelper<T extends SenchaConfiguration> implements SenchaHelper {

  static final String PRODUCTION = "production";
  static final String TESTING = "testing";
  static final String DEVELOPMENT = "development";

  private static final String SENCHA_SRC_PATH = "/src/main/sencha/";
  protected static final String SENCHA_CLASS_PATH = "/src";
  protected static final String SENCHA_APP_CLASS_PATH = "/app";

  private final MavenProject project;
  private final T senchaConfiguration;
  private final Log log;

  private final String senchaModuleName;
  private final ExtendableProfileConfiguration commonProfileConfiguration;
  private final ExtendableProfileConfiguration productionProfileConfiguration;
  private final ExtendableProfileConfiguration testingProfileConfiguration;
  private final ExtendableProfileConfiguration developmentProfileConfiguration;

  public AbstractSenchaHelper(MavenProject project, T senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;

    this.senchaModuleName = getSenchaPackageName(project.getGroupId(), project.getArtifactId());
    this.commonProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration);
    this.productionProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getProduction());
    this.testingProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getTesting());
    this.developmentProfileConfiguration = new ExtendableProfileConfiguration(senchaConfiguration.getDevelopment());
  }

  protected void copyFilesFromSrc(String path) throws MojoExecutionException {
    File srcDir = new File(project.getBasedir() + SENCHA_SRC_PATH);
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
    File jangarooResourcesDir = new File(project.getBuild().getDirectory() + "/classes/META-INF/resources");
    File senchaResourcesDir = new File(path + File.separator + SENCHA_RESOURCES_PATH);
    if (jangarooResourcesDir.exists()) {
      try {
        FileUtils.copyDirectory(jangarooResourcesDir, senchaResourcesDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could not copy resources from %s to %s", jangarooResourcesDir, senchaResourcesDir), e);
      }
    }

    File jangarooClassDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/classes");
    if (jangarooClassDir.exists()) {
      String senchaClassPath = Type.APP.equals(getSenchaConfiguration().getType()) ? SENCHA_APP_CLASS_PATH : SENCHA_CLASS_PATH;
      File senchaClassDir = new File(path + senchaClassPath);
      try {
        // FileUtils.move fails if directory already exists
        FileUtils.copyDirectory(jangarooClassDir, senchaClassDir);
        FileUtils.deleteDirectory(jangarooClassDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could not copy classes from %s to %s", jangarooClassDir, senchaClassDir), e);
      }
    }

    File jangarooOverridesDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/overrides");
    if (jangarooOverridesDir.exists()) {
      File senchaOverridesDir = new File(path + File.separator + SENCHA_OVERRIDES_PATH);
      try {
        // FileUtils.move fails if directory already exists
        FileUtils.copyDirectory(jangarooOverridesDir, senchaOverridesDir);
        FileUtils.deleteDirectory(jangarooOverridesDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could not copy overrides from %s to %s", jangarooOverridesDir, senchaOverridesDir), e);
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

  protected MavenProject getProject() {
    return project;
  }

  protected T getSenchaConfiguration() {
    return senchaConfiguration;
  }

  protected Log getLog() {
    return log;
  }

  protected String getSenchaModuleName() {
    return senchaModuleName;
  }

  protected Map<String, Object> getConfig(Configurer[] configurers) throws MojoExecutionException {
    Map<String, Object> config = new LinkedHashMap<>();

    for (Configurer configurer : configurers) {
      configurer.configure(config);
    }

    return config;
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
}
