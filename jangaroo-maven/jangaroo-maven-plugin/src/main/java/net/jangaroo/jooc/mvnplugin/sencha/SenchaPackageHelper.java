package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_RESOURCES_PATH;

public class SenchaPackageHelper extends SenchaPackageOrAppHelper<SenchaPackageConfiguration, SenchaPackageConfigBuilder> {

  private static final String SENCHA_PACKAGE_BUILD_PROPERTIES_FILE = "/.sencha/package/build.properties";

  private final String senchaPackagePath;
  private final String senchaPackageBuildOutputDir;

  public SenchaPackageHelper(MavenProject project, SenchaPackageConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    senchaPackagePath = project.getBuild().getDirectory() + SenchaUtils.LOCAL_PACKAGE_PATH;
    senchaPackageBuildOutputDir = project.getBuild().getDirectory() +  SenchaUtils.LOCAL_PACKAGE_BUILD_PATH;
  }

  public void createModule() throws MojoExecutionException {

    File workingDirectory;
    try {
      workingDirectory = new File(senchaPackagePath).getCanonicalFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not determine working directory", e);
    }

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("Could not create working directory " + workingDirectory);
    }

    File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_CONFIG);
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      if (!senchaCfg.delete()) {
        throw new MojoExecutionException("Could not delete " + senchaCfg);
      }
    }

    // This is a workaround

    // we must use the --name parameter to specify a path to the package directory as workspace.json cannot be modified
    // because of problems with parallel builds

    // because using "/" in package name is not valid we must prevent Sencha generate package to create a package.json
    // otherwise a temporary state exists where the whole workspace fails to build because of an invalid package name.
    // this can be achieved by creating a package.json before Sencha generate package is triggered.
    writePackageJson(workingDirectory);

    Path pathToWorkingDirectory = SenchaUtils.getRelativePathFromWorkspaceToWorkingDir(workingDirectory);

    String arguments = "generate package"
            + " --name=\"" + getSenchaModuleName() + "\""
            + " --namespace=\"\""
            + " --type=\"code\""
            + " " + pathToWorkingDirectory;
    getLog().info("Generating Sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      try (PrintWriter pw = new PrintWriter(new FileWriter(senchaCfg.getAbsoluteFile(), true)) ) {
        pw.println("skip.sass=1");
        pw.println("skip.slice=1");
      } catch (IOException e) {
        throw new MojoExecutionException("Could not append skip.sass and skip.slice to sencha.cfg of package");
      }
    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }

  }

  protected void addRegisterEditorPluginsResources(String modulePath) throws MojoExecutionException {
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH, getCommonProfileConfiguration());
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "production", getProductionProfileConfiguration());
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "testing", getTestingProfileConfiguration());
    addRegisterEditorPluginsResource(modulePath, SENCHA_RESOURCES_PATH + File.separator + "development", getDevelopmentProfileConfiguration());
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
      getProductionProfileConfiguration().addAdditionalJsIncludeInBundle(SenchaUtils.SENCHA_RESOURCES_PATH + SenchaUtils.SEPARATOR + REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME);
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

  public void prepareModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      getLog().info("Generating Sencha package into: " + senchaPackageDirectory.getPath());
      getLog().debug("Created " + senchaPackageDirectory.mkdirs());
    }

    copyFiles(senchaPackagePath);
    addRegisterEditorPluginsResources(senchaPackagePath);
    addRequireEditorPluginsResource(senchaPackagePath);

    File workingDirectory = new File(senchaPackagePath);

    writePackageJson(workingDirectory);

    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + SENCHA_PACKAGE_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "pkg.file.name", "${package.name}.pkg",
            "pkg.build.dir", "${package.dir}/build",
            "build.temp.dir", "${package.dir}/build/temp"
    ));
  }

  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaPackageDirectory.getPath());
    }

    buildSenchaPackage(senchaPackageDirectory);

    File pkg = new File(senchaPackageBuildOutputDir + getSenchaModuleName() + SenchaUtils.SENCHA_PKG_EXTENSION);
    if (!pkg.exists()) {
      throw new MojoExecutionException("Could not find " + SenchaUtils.SENCHA_PKG_EXTENSION + " for Sencha package " + getSenchaModuleName());
    }

    return pkg;
  }

  private void writePackageJson(File workingDirectory) throws MojoExecutionException {
    writeJson(new File(workingDirectory, SenchaUtils.SENCHA_PACKAGE_FILENAME));
  }

  private void buildSenchaPackage(File senchaPackageDirectory) throws MojoExecutionException {
    getLog().info("Building Sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaPackageDirectory, "package build", getLog());
    senchaCmdExecutor.execute();
  }

  @Override
  protected SenchaPackageConfigBuilder createSenchaConfigBuilder() {
    return new SenchaPackageConfigBuilder();
  }

  @Override
  protected String getDefaultsJsonFileName() {
    return "default.package.json";
  }

  @Override
  protected void configure(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    configBuilder.type(Type.THEME.equals(getSenchaConfiguration().getType()) ? "theme" : "code");
    super.configure(configBuilder);
  }

}
