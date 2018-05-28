package net.jangaroo.jooc.mvnplugin;

import com.google.common.io.Files;
import net.jangaroo.jooc.PackagerImpl;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaPackageConfigBuilder;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGE_CONFIG_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_BUNDLED_RESOURCES_PATH;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Generates and packages Sencha package modules of type "test" and "code"
 */
@Mojo(name = "package-pkg", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyCollection = ResolutionScope.COMPILE, threadSafe = true )
public class SenchaPackageMojo extends AbstractSenchaPackageOrAppMojo<SenchaPackageConfigBuilder> {

  /**
   * Defines the packageType of the Sencha package that will be generated. Possible values are "code" (default) and "theme".
   *
   * @since 4.0
   */
  @Parameter(defaultValue = Type.CODE)
  private String packageType;

  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
  private String buildDirectoryPath;

  private File senchaPackageDirectory;

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
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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
    if (!Type.JANGAROO_PKG_PACKAGING.equals(project.getPackaging()) && !Type.JANGAROO_SWC_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"pkg\" or \"swc\"");
    }
    getLog().info("Execute sencha packaging mojo");
    FileHelper.ensureDirectory(new File(getSenchaPackageDirectory().getPath()));
    SenchaPackageConfigBuilder configBuilder = createSenchaConfigBuilder();
    configure(configBuilder);
    prepareModule(configBuilder);
  }

  public void prepareModule(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    writePackageConfig(configBuilder);
    writePackageJson(configBuilder);
    compileJavaScriptSources(getSenchaPackageDirectory());
    writeSenchaCfgFile();
  }

  private void writeSenchaCfgFile() throws MojoExecutionException {
    File senchaCfg = new File(getSenchaPackageDirectory().getPath(), SenchaUtils.SENCHA_PACKAGE_CONFIG);
    getLog().info("Write " + SenchaUtils.SENCHA_PACKAGE_CONFIG);
    try {
      Files.createParentDirs(senchaCfg);
      Files.touch(senchaCfg);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + senchaCfg.getAbsolutePath(), e);
    }
  }

  private void compileJavaScriptSources(File dir) throws MojoExecutionException {
    try {
      new PackagerImpl().doPackage(
              new File(dir, "src"),
              new File(dir, "overrides"),
              new File(dir, "locale"),
              dir,
              getSenchaPackageName(project));
    } catch (IOException e) {
      throw new MojoExecutionException("exception while packaging JavaScript sources", e);
    }
  }

  private void writePackageJson(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    getLog().info("Write package.json file");
    SenchaUtils.writeFile(configBuilder, getSenchaPackageDirectory().getPath(), SenchaUtils.SENCHA_PACKAGE_FILENAME, null, getLog());
  }

  private void configure(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    SenchaUtils.configureDefaults(configBuilder, "default.package.json");
    super.configure(configBuilder);
    configBuilder.type(Type.THEME.equals(getType()) ? Type.THEME : Type.CODE);
    addRequiredClasses(configBuilder, null, getRequiredClasses());
    addRequiredClasses(configBuilder, SenchaUtils.PRODUCTION_PROFILE, getRequiredClassesFromConfiguration(getProduction()));
    addRequiredClasses(configBuilder, SenchaUtils.TESTING_PROFILE, getRequiredClassesFromConfiguration(getTesting()));
    addRequiredClasses(configBuilder, SenchaUtils.DEVELOPMENT_PROFILE, getRequiredClassesFromConfiguration(getDevelopment()));
  }

  @Nonnull
  private static List<String> getRequiredClassesFromConfiguration(@Nullable SenchaProfileConfiguration configuration) {
    return configuration == null ? Collections.emptyList() : configuration.getRequiredClasses();
  }

  protected void addRequiredClasses(@Nonnull SenchaPackageConfigBuilder configBuilder,
                                    @Nullable String profile,
                                    @Nonnull List<String> requiredClassesForProfile) throws MojoExecutionException {
    if (requiredClassesForProfile.isEmpty()) {
      return;
    }

    File requireResourceFile = new File(getRequiredClassesFileName(profile, getSenchaPackageDirectory().getPath(), File.separator));
    if (requireResourceFile.exists()) {
      getLog().info("requireResourceFile file for require editor plugins already exists, deleting...");
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

  private void writeRequireResourceFile(File requireResourceFile, List<String> requiredClasses)
          throws MojoExecutionException {
    getLog().info(String.format("Write %s for module", requireResourceFile.getPath()));

    FileHelper.ensureDirectory(requireResourceFile.getParentFile());

    try (PrintWriter pw = new PrintWriter(new FileWriter(requireResourceFile, true))) {
      for (String requiredClass : requiredClasses) {
        pw.printf("Ext.require(%s);%n", CompilerUtils.quote(requiredClass));
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write require resource file", e);
    }
  }

  private void writePackageConfig(SenchaPackageConfigBuilder configBuilder) throws MojoExecutionException {
    if (globalResourcesMap == null || globalResourcesMap.isEmpty()) {
      return;
    }
    getLog().info(String.format("Write %s for module", PACKAGE_CONFIG_FILENAME));
    configBuilder.js("bundledResources/packageConfig.js", false, true);
    String senchaPackageBuildOutputDirectoryPath = buildDirectoryPath + SenchaUtils.getPackagesPath(project);
    File packageConfigJsFile = new File(senchaPackageBuildOutputDirectoryPath + "/" + SENCHA_BUNDLED_RESOURCES_PATH + "/" + PACKAGE_CONFIG_FILENAME);
    FileHelper.ensureDirectory(packageConfigJsFile.getParentFile());
    if (packageConfigJsFile.exists()) {
      getLog().debug(PACKAGE_CONFIG_FILENAME + " for module already exists, deleting...");
      if (!packageConfigJsFile.delete()) {
        throw new MojoExecutionException("Could not delete " + PACKAGE_CONFIG_FILENAME + " file for module");
      }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(packageConfigJsFile, true))) {
      pw.println("(function(){");
      writeGlobalResourceMapJs(pw);
      pw.println("}());");
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + PACKAGE_CONFIG_FILENAME + " resource", e);
    }
  }

  private void writeGlobalResourceMapJs(PrintWriter pw) throws MojoExecutionException {
    String senchaPackageName = getSenchaPackageName(project);
    getLog().info("Write global resource map JavaScript for " + PACKAGE_CONFIG_FILENAME);
    pw.printf("// START - Adding global resources to ext manifest%n");
    pw.printf("function resolveAbsolutePath(packageName, resourcePath) {%n" +
              "  var resolvedPath = Ext.resolveResource('<@' + packageName + '>' + resourcePath);%n" +
              "  if (resolvedPath.indexOf('/') !== 0) {%n" +
              "    var pathname = window.location.pathname;%n" +
              "    resolvedPath = pathname.substring(0, pathname.lastIndexOf('/') + 1) + resolvedPath;%n" +
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

  public File getSenchaPackageDirectory() {
    if (senchaPackageDirectory == null) {
      senchaPackageDirectory = new File(buildDirectoryPath + SenchaUtils.getPackagesPath(project));
    }
    return senchaPackageDirectory;
  }

  @Override
  protected SenchaPackageConfigBuilder createSenchaConfigBuilder() {
    return new SenchaPackageConfigBuilder();
  }
}
