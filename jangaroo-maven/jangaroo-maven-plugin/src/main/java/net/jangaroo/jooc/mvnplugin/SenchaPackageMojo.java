package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
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
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGE_CONFIG_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_BUNDLED_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_PKG_EXTENSION;
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

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_PATH, readonly = true)
  private String senchaPackagePath;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_BUILD_PATH, readonly = true)
  private String senchaPackageBuildOutputDir;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.LOCAL_PACKAGE_PATH + "/" + SENCHA_BUNDLED_RESOURCES_PATH + "/" + PACKAGE_CONFIG_FILENAME, readonly = true)
  private File packageDependencyOrderJsFile;

  /**
   * Defines a map of global resources (files or directories) which can be accessed in your application
   * by the entry's key. For example, the resource ...
   * <pre>
   * &lt;globalResourcesMap>
   *   &lt;exampleJs>example.js&lt;/exampleJs>
   * &lt;/globalResourcesMap>
   * </pre>
   * ... is accessible in your code as follows ...
   * <pre>
   *  Ext.manifest.globalResources['exampleJs'];
   * </pre>
   * <p>
   * During the build process Sencha Cmd copies the resources of a package into isolated subdirectories of the final
   * app's resource directory. The corresponding path change is managed by the plugin. If you access the resource
   * through the manifest you always get the correct absolute path.
   * </p>
   * <p>
   * If you want to want to access the global resources outside of an Ext class make sure that you wrapped the call
   * in a <code>Ext.onReady</code> callback function.
   * </p>
   * The given path must be relative to the package's resource directory, i.e. <i>src/main/sencha/resources</i>.
   */
  @Parameter
  private Map<String, String> globalResourcesMap;

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
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      FileHelper.addToConfigFile(senchaCfg, ImmutableList.of("skip.sass=1", "skip.slice=1"));
    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }
    writePackageConfig();
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

    FileHelper.copyFiles(getSenchaSrcDir(), senchaPackageDirectory);

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
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaPackageDirectory, "package build", getLog(), getSenchaLogLevel());
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
    configBuilder.type(Type.THEME.equals(getType()) ? Type.THEME : Type.CODE);
    configBuilder.destFile(workingDirectory + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME);

    configure(configBuilder);

    addRequiredClasses(configBuilder, null, getRequiredClasses());
    addRequiredClasses(configBuilder, SenchaUtils.PRODUCTION_PROFILE, getRequiredClassesFromConfiguration(getProduction()));
    addRequiredClasses(configBuilder, SenchaUtils.TESTING_PROFILE, getRequiredClassesFromConfiguration(getTesting()));
    addRequiredClasses(configBuilder, SenchaUtils.DEVELOPMENT_PROFILE, getRequiredClassesFromConfiguration(getDevelopment()));
  }

  @Nonnull
  private static List<String> getRequiredClassesFromConfiguration(@Nullable SenchaProfileConfiguration configuration) {
    return configuration == null ? Collections.<String>emptyList() : configuration.getRequiredClasses();
  }

  protected void addRequiredClasses(@Nonnull SenchaPackageConfigBuilder configBuilder,
                                    @Nullable String profile,
                                    @Nonnull List<String> requiredClassesForProfile) throws MojoExecutionException {
    if (requiredClassesForProfile.isEmpty()) {
      return;
    }

    File requireResourceFile = new File(getRequiredClassesFileName(profile, senchaPackagePath, File.separator));
    if (requireResourceFile.exists()) {
      getLog().warn("requireResourceFile file for require editor plugins already exists, deleting...");
      if (!requireResourceFile.delete()) {
        throw new MojoExecutionException("Could not delete requireResourceFile file for require editor plugins");
      }
    }
    // write file
    writeRequireResourceFile(requireResourceFile, requiredClassesForProfile);

    if (profile == null) {
      configBuilder.js(getRequiredClassesFileName(null, null, SenchaUtils.SEPARATOR), false, true);
    } else {
      SenchaPackageConfigBuilder requiredCLassesConfigBuilder = new SenchaPackageConfigBuilder();
      requiredCLassesConfigBuilder.js(getRequiredClassesFileName(profile, null, SenchaUtils.SEPARATOR), false, true);
      configBuilder.profile(profile, requiredCLassesConfigBuilder.build());
    }
  }

  @Nonnull
  private static String getRequiredClassesFileName(@Nullable String profile, String baseDir, String separator) {
    StringBuilder nameBuilder = new StringBuilder();
    if (baseDir != null) {
      nameBuilder.append(baseDir).append(separator);
    }
    nameBuilder.append(SENCHA_BUNDLED_RESOURCES_PATH).append(separator);
    if (profile != null) {
      nameBuilder.append(profile).append(separator);
    }
    nameBuilder.append(SenchaUtils.REQUIRED_CLASSES_FILENAME);
    return nameBuilder.toString();
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

  private void writePackageConfig() throws MojoExecutionException {
    FileHelper.ensureDirectory(packageDependencyOrderJsFile.getParentFile());
    if (packageDependencyOrderJsFile.exists()) {
      getLog().warn(PACKAGE_CONFIG_FILENAME + " for module already exists, deleting...");
      if (!packageDependencyOrderJsFile.delete()) {
        throw new MojoExecutionException("Could not delete " + PACKAGE_CONFIG_FILENAME + " file for module");
      }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(packageDependencyOrderJsFile, true))) {
      pw.println("(function(){");
      writePackageDependencyOrderJs(pw);
      writeGlobalResourceMapJs(pw);
      pw.println("}());");
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + PACKAGE_CONFIG_FILENAME + " resource", e);
    }
  }

  private void writePackageDependencyOrderJs(PrintWriter pw) throws MojoExecutionException {
      pw.printf("// START - Registering package dependency order%n" +
                "Ext.manifest.packageDependencyOrder.push('%s');%n" +
                "// END - Registering package dependency order%n", getSenchaPackageName(project));
  }

  private void writeGlobalResourceMapJs(PrintWriter pw) throws MojoExecutionException {
    if (globalResourcesMap != null && !globalResourcesMap.isEmpty()) {
      String senchaPackageName = SenchaUtils.getSenchaPackageName(project);
      pw.printf("// START - Adding global resources to ext manifest%n");
      pw.printf("function resolveAbsolutePath(packageName, resourcePath) {%n" +
                "  var resolvedPath = Ext.resolveResource('<@' + packageName + '>' + resourcePath);%n" +
                "  if (resolvedPath.indexOf('/') !== 0) {%n" +
                "    resolvedPath = window.location.pathname + resolvedPath;%n" +
                "  }%n" +
                "  return resolvedPath;%n" +
                "};%n" +
                "Ext.apply(Ext.manifest.globalResources, {%n");

      Iterator<Map.Entry<String, String>> globalResourceIterator = globalResourcesMap.entrySet().iterator();
      while (globalResourceIterator.hasNext()) {
        Map.Entry<String, String> globalResource = globalResourceIterator.next();
        pw.printf(
                "  '%s': resolveAbsolutePath('%s', '%s')", globalResource.getKey(), senchaPackageName, globalResource.getValue());
        if (globalResourceIterator.hasNext()) {
          pw.printf(",%n");
        }
      }
      pw.printf("%n});%n");
      pw.println("// END - Adding global resources to ext manifest");
    }
  }

  @Override
  protected SenchaPackageConfigBuilder createSenchaConfigBuilder() {
    return new SenchaPackageConfigBuilder();
  }
}
