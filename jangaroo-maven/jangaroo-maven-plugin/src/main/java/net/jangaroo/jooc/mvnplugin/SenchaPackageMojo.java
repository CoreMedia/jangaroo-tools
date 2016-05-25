package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.REGISTER_PACKAGE_ORDER_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_PKG_EXTENSION;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Generates and packages Sencha package modules of type "test" and "code"
 */
@Mojo(name = "package-pkg", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyCollection = ResolutionScope.COMPILE, threadSafe = true )
public class SenchaPackageMojo extends AbstractSenchaPackageOrAppMojo<SenchaPackageConfigBuilder> {

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

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_PATH)
  private String senchaPackagePath;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_BUILD_PATH)
  private String senchaPackageBuildOutputDir;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_PATH + "/" + SENCHA_RESOURCES_PATH + "/" + REGISTER_PACKAGE_ORDER_FILENAME)
  private File packageDependencyOrderJsFile;

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
  public String getJsonConfigFileName() {
    return SenchaUtils.SENCHA_PACKAGE_FILENAME;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_PKG_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-pkg\"");
    }
    // for now:
    createModule();
    prepareModule();

    if (!skipPkg) {
      File pkg = packageModule();
      helper.attachArtifact(project, Type.PACKAGE_EXTENSION, pkg);
    }

  }
  private void createModule() throws MojoExecutionException {

    File workingDirectory = getWorkingDirectory();

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
            + " --name=\"" + getSenchaPackageName(project) + "\""
            + " --namespace=\"\""
            + " --type=\"code\""
            + " " + pathToWorkingDirectory;
    getLog().info("Generating Sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      FileHelper.addToConfigFile(senchaCfg, ImmutableList.of("skip.sass=1", "skip.slice=1"));
    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }
    writePackageDependencyOrderJs();
  }

  private File getWorkingDirectory() throws MojoExecutionException {
    File workingDirectory;
    try {
      workingDirectory = new File(senchaPackagePath).getCanonicalFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not determine working directory", e);
    }

    FileHelper.ensureDirectory(workingDirectory);

    return workingDirectory;
  }

  public void prepareModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      getLog().info("Generating Sencha package into: " + senchaPackageDirectory.getPath());
      getLog().debug("Created " + senchaPackageDirectory.mkdirs());
    }

    FileHelper.copyFiles(getSenchaSrcDir(), senchaPackageDirectory, SenchaUtils.SENCHA_PACKAGE_FILENAME);

    try {
      SenchaPackageConfigBuilder configBuilder = getConfigBuilder(senchaPackagePath);
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

  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaPackageDirectory.getPath());
    }

    buildSenchaPackage(senchaPackageDirectory);

    File pkg = new File(senchaPackageBuildOutputDir + getSenchaPackageName(project) + SENCHA_PKG_EXTENSION);
    if (!pkg.exists()) {
      throw new MojoExecutionException("Could not find " + SENCHA_PKG_EXTENSION + " for Sencha package " + getSenchaPackageName(project));
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

    addRequiredClasses(configBuilder, null, this);
    addRequiredClasses(configBuilder, PRODUCTION_PROFILE, getProduction());
    addRequiredClasses(configBuilder, TESTING_PROFILE, getTesting());
    addRequiredClasses(configBuilder, DEVELOPMENT_PROFILE, getDevelopment());
  }

  protected void addRequiredClasses(SenchaPackageConfigBuilder configBuilder,
                                    String profile,
                                    SenchaProfileConfiguration configuration) throws MojoExecutionException {
    if (configuration == null) {
      return;
    }

    List<String> requiredClasses = configuration.getRequiredClasses();
    if (!requiredClasses.isEmpty()) {

      File requireResourceFile = new File(senchaPackagePath + File.separator + SENCHA_RESOURCES_PATH +
              File.separator + SenchaUtils.REQUIRED_CLASSES_FILENAME);
      if (requireResourceFile.exists()) {
        getLog().warn("requireResourceFile file for require editor plugins already exists, deleting...");
        if (!requireResourceFile.delete()) {
          throw new MojoExecutionException("Could not delete requireResourceFile file for require editor plugins");
        }
      }
      // write file
      writeRequireResourceFile(requireResourceFile, requiredClasses);

      SenchaPackageConfigBuilder requiredCLassesConfigBuilder = new SenchaPackageConfigBuilder();
      requiredCLassesConfigBuilder.js(SENCHA_RESOURCES_PATH + SenchaUtils.SEPARATOR + SenchaUtils.REQUIRED_CLASSES_FILENAME, false, true);
      configBuilder.profile(profile, requiredCLassesConfigBuilder.build());
    }
  }

  private static void writeRequireResourceFile(File requireResourceFile, List<String> requiredClasses)
          throws MojoExecutionException {

    FileHelper.ensureDirectory(requireResourceFile.getParentFile());

    try (PrintWriter pw = new PrintWriter(new FileWriter(requireResourceFile, true))) {
      for (String requiredClass : requiredClasses) {
        pw.printf("Ext.require(%s);%n", CompilerUtils.quote(requiredClass));
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write require resource file", e);
    }
  }

  @Override
  protected void configureResourcesEntry(SenchaPackageOrAppConfigBuilder configBuilder) {

    if (Type.CODE.equals(getType()) && shareResources
            || Type.THEME.equals(getType()) && !isolateResources) {
      ((SenchaPackageConfigBuilder)configBuilder).shareResources();
    }

    super.configureResourcesEntry(configBuilder);
  }


  private void writePackageDependencyOrderJs() throws MojoExecutionException {
    if (packageDependencyOrderJsFile.exists()) {
      getLog().warn("register package dependency order file for module already exists, deleting...");
      if (!packageDependencyOrderJsFile.delete()) {
        throw new MojoExecutionException("Could not delete registerPackageDependencyOrder file for module");
      }
    }
    try (PrintWriter pw = new PrintWriter(new FileWriter(packageDependencyOrderJsFile, true))) {
      pw.printf("Ext.manifest.packageDependencyOrder.push(\"%s\");%n", getSenchaPackageName(project));
    } catch (IOException e) {
      throw new MojoExecutionException("could not create package dependency order resource", e);
    }
  }

  @Override
  protected SenchaPackageConfigBuilder createSenchaConfigBuilder() {
    return new SenchaPackageConfigBuilder();
  }
}
