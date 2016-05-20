package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.sencha.EditorPluginDescriptor;
import net.jangaroo.jooc.mvnplugin.sencha.FileUtils;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageOrAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Generates and packages Sencha package modules of type "test" and "code"
 */
@Mojo(name = "package-pkg", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyCollection = ResolutionScope.COMPILE, threadSafe = true )
public class SenchaPackageMojo extends AbstractSenchaPackageOrAppMojo<SenchaPackageConfigBuilder> implements SenchaPackageConfiguration {

  private static final String SENCHA_PACKAGE_BUILD_PROPERTIES_FILE = "/.sencha/package/build.properties";


  @Inject
  private MavenProjectHelper helper;

  /**
   * Skips the build process of the Sencha package which results in a separate <em>pkg</em>
   * artifact. The <em>pkg</em> artifact is required if any other non-local Maven module
   * depends on this project.
   * <p />
   * Enabling this option speeds up the build process.
   *
   * @since 4.0
   */
  @Parameter(property = "skipPkg", defaultValue = "false")
  private boolean skipPkg;

  /**
   * Defines the packageType of the Sencha package that will be generated. Possible values are "code" (default) and "theme".
   *
   * @since 4.0
   */
  @Parameter(defaultValue = Type.CODE)
  private String packageType;

  /**
   * Defines if the resources of this package will be shared between different profiles of an application production
   * build.
   *
   * Due to restrictions in Sencha CMD only usable if {@link #packageType} is set to {@link Type#CODE}
   *
   * @since 4.0
   */
  @Parameter(defaultValue = "false")
  private boolean shareResources;

  /**
   * Defines if the resources of this package will isolated in an application production build.
   *
   * Due to restrictions in Sencha CMD only usable if {@link #packageType} is set to {@link Type#THEME}
   *
   * @since 4.0
   */
  @Parameter(defaultValue = "false")
  private boolean isolateResources;

  private String senchaPackagePath;
  private String senchaPackageBuildOutputDir;


  @Override
  public String getType() {
    if (Type.CODE.equals(packageType) || Type.THEME.equals(packageType)) {
      return packageType;
    }
    getLog().error(String.format("%s is not a valid packaging packageType. Using \"code\" instead.", packageType));
    packageType = Type.CODE;
    return packageType;
  }

  @Override
  public boolean isIsolateResources() {
    return isolateResources;
  }

  @Override
  public boolean isShareResources() {
    return shareResources;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_PKG_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-pkg\"");
    }

    senchaPackagePath = project.getBuild().getDirectory() + SenchaUtils.LOCAL_PACKAGE_PATH;
    senchaPackageBuildOutputDir = project.getBuild().getDirectory() +  SenchaUtils.LOCAL_PACKAGE_BUILD_PATH;

    // for now:
    createModule();
    prepareModule();

    if (!skipPkg) {
      File pkg = packageModule();
      helper.attachArtifact(project, Type.PACKAGE_EXTENSION, pkg);
    }

  }
  private void createModule() throws MojoExecutionException {

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
    if (senchaCfg.exists() && !senchaCfg.delete()) {
      throw new MojoExecutionException("Could not delete " + senchaCfg);
    }

    // This is a workaround

    // we must use the --name parameter to specify a path to the package directory as workspace.json cannot be modified
    // because of problems with parallel builds

    // because using "/" in package name is not valid we must prevent Sencha generate package to create a package.json
    // otherwise a temporary state exists where the whole workspace fails to build because of an invalid package name.
    // this can be achieved by creating a package.json before Sencha generate package is triggered.
    writePackageJson(workingDirectory.getPath());

    Path pathToWorkingDirectory = SenchaUtils.getRelativePathFromWorkspaceToWorkingDir(workingDirectory);

    String arguments = "generate package"
            + " --name=\"" + getSenchaPackageName(project.getGroupId(), project.getArtifactId()) + "\""
            + " --namespace=\"\""
            + " --type=\"code\""
            + " " + pathToWorkingDirectory;
    getLog().info("Generating Sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      addConfigurationToSenchaConfig(senchaCfg);
    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }

  }

  public void prepareModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      getLog().info("Generating Sencha package into: " + senchaPackageDirectory.getPath());
      getLog().debug("Created " + senchaPackageDirectory.mkdirs());
    }

    FileUtils.copyFiles(project.getBasedir() + SENCHA_SRC_PATH, senchaPackagePath);

    try {
      SenchaPackageConfigBuilder configBuilder = getConfigBuilder(senchaPackagePath);

      addRegisterEditorPluginsResources(configBuilder);
      addRequireEditorPluginsResource(configBuilder);

      configBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not build package.json for project " + project, e);
    }

    File workingDirectory = new File(senchaPackagePath);
    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + SENCHA_PACKAGE_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "pkg.file.name", "${package.name}.pkg",
            "pkg.build.dir", "${package.dir}/build",
            "build.temp.dir", "${package.dir}/build/temp"
    ));
  }

  protected void addRegisterEditorPluginsResources(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    addRegisterEditorPluginsResource(configBuilder, SENCHA_RESOURCES_PATH, this);
    addRegisterEditorPluginsResource(configBuilder, SENCHA_RESOURCES_PATH + File.separator + PRODUCTION, getProduction());
    addRegisterEditorPluginsResource(configBuilder, SENCHA_RESOURCES_PATH + File.separator + TESTING, getTesting());
    addRegisterEditorPluginsResource(configBuilder, SENCHA_RESOURCES_PATH + File.separator + DEVELOPMENT, getDevelopment());
  }

  private void addRegisterEditorPluginsResource(SenchaPackageConfigBuilder configBuilder,
                                                String resourcesPath,
                                                SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    List<? extends EditorPluginDescriptor> editorPlugins =
            senchaProfileConfiguration == null ? Collections.<EditorPluginDescriptor>emptyList() :
                    senchaProfileConfiguration.getEditorPlugins();
    if (!editorPlugins.isEmpty()) {
      File resource = new File(senchaPackagePath + File.separator + resourcesPath + File.separator + REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME);
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
        throw new MojoExecutionException("could not create editor plugins resource", e);
      }
      configBuilder.js(resourcesPath + SenchaUtils.SEPARATOR + REGISTER_EDITOR_PLUGIN_RESOURCE_FILENAME, false, true);
    }
  }

  protected void addRequireEditorPluginsResource(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    // collect normal and build editor plugins
    List<EditorPluginDescriptor> relevantEditorPlugins = new ArrayList<>();
    relevantEditorPlugins.addAll(getEditorPlugins());
    if (null != getProduction()) {
      relevantEditorPlugins.addAll(getProduction().getEditorPlugins());
    }
    if (!relevantEditorPlugins.isEmpty()) {

      File resource = new File(senchaPackagePath + File.separator + SENCHA_RESOURCES_PATH + File.separator + REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME);
      if (resource.exists()) {
        getLog().warn("resource file for require editor plugins already exists, deleting...");
        if (!resource.delete()) {
          throw new MojoExecutionException("Could not delete resource file for require editor plugins");
        }
      }

      try (PrintWriter pw = new PrintWriter(new FileWriter(resource, true))) {

        for (EditorPluginDescriptor editorPlugin : relevantEditorPlugins) {
          String editorPluginMainClass = editorPlugin.getMainClass();
          if (null == editorPluginMainClass) {
            getLog().warn("EditorPluginDescriptor without mainClass was ignored.");
            continue;
          }
          pw.printf("Ext.require(%s);%n", CompilerUtils.quote(editorPluginMainClass));
        }
      } catch (IOException e) {
        throw new MojoExecutionException("could not append skip.sass and skip.slice to Sencha config of package", e);
      }
      // require plugin needs to be added to production
      SenchaPackageConfigBuilder productionConfigBuilder = new SenchaPackageConfigBuilder();
      productionConfigBuilder.js(SenchaUtils.SENCHA_RESOURCES_PATH + SenchaUtils.SEPARATOR + REQUIRE_EDITOR_PLUGIN_RESOURCE_FILENAME,false, true);
      configBuilder.profile(PRODUCTION, productionConfigBuilder.build());
    }
  }

  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaPackageDirectory.getPath());
    }

    buildSenchaPackage(senchaPackageDirectory);

    File pkg = new File(senchaPackageBuildOutputDir + getSenchaPackageName(project.getGroupId(), project.getArtifactId()) + SenchaUtils.SENCHA_PKG_EXTENSION);
    if (!pkg.exists()) {
      throw new MojoExecutionException("Could not find " + SenchaUtils.SENCHA_PKG_EXTENSION + " for Sencha package " + getSenchaPackageName(project.getGroupId(), project.getArtifactId()));
    }

    return pkg;
  }


  private void writePackageJson(String workingDirectory) throws MojoExecutionException {

    try {
      SenchaPackageConfigBuilder configBuilder = getConfigBuilder(workingDirectory);
      configBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write package json", e);
    }
  }

  private void buildSenchaPackage(File senchaPackageDirectory) throws MojoExecutionException {
    getLog().info("Building Sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaPackageDirectory, "package build", getLog());
    senchaCmdExecutor.execute();
  }

  private SenchaPackageConfigBuilder getConfigBuilder(String workingDirectory) throws IOException, MojoExecutionException {

    SenchaPackageConfigBuilder configBuilder = new SenchaPackageConfigBuilder();
    configure(configBuilder, workingDirectory);

    return configBuilder;
  }

  protected void configure(SenchaPackageConfigBuilder configBuilder, String workingDirectory) throws MojoExecutionException {
    try {
      configBuilder.defaults("default.package.json");
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot laod default.package.json", e);
    }
    configBuilder.destFile(workingDirectory + SenchaUtils.SENCHA_PACKAGE_FILENAME);
    configBuilder.type(Type.THEME.equals(getType()) ? "theme" : "code");
    configBuilder.destFile(workingDirectory + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME);

    configure(configBuilder);
  }

  protected void configureResourcesEntry(SenchaPackageOrAppConfigBuilder configBuilder) {

    if (Type.CODE.equals(getType()) && isShareResources()
            || Type.THEME.equals(getType()) && !isIsolateResources()) {
      ((SenchaPackageConfigBuilder)configBuilder).shareResources();
    }

    super.configureResourcesEntry(configBuilder);
  }

  protected SenchaPackageConfigBuilder createSenchaConfigBuilder() {
    return new SenchaPackageConfigBuilder();
  }
}
