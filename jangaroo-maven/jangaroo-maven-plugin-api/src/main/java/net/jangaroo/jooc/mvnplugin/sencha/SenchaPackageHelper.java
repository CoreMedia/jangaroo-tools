package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.MavenSenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaCodePackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaThemePackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Map;

public class SenchaPackageHelper extends AbstractSenchaHelper {

  private static final String SENCHA_PACKAGE_BUILD_PROPERTIES_FILE = "/.sencha/package/build.properties";

  private final PathConfigurer pathConfigurer;
  private final Configurer[] packageConfigurers;
  private final String senchaPackagePath;
  private final String senchaPackageBuildOutputDir;

  public SenchaPackageHelper(MavenProject project, MavenSenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project, senchaConfiguration);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(project, senchaConfiguration);
    pathConfigurer = new PathConfigurer(senchaConfiguration);

    senchaPackagePath = project.getBuild().getDirectory() + SenchaUtils.LOCAL_PACKAGE_PATH;
    senchaPackageBuildOutputDir = project.getBuild().getDirectory() +  SenchaUtils.LOCAL_PACKAGE_BUILD_PATH;

    Configurer defaultSenchaPackageConfigurer;
    if (Type.THEME.equals(senchaConfiguration.getType())) {
      defaultSenchaPackageConfigurer = DefaultSenchaThemePackageConfigurer.getInstance();
    } else {
      defaultSenchaPackageConfigurer = DefaultSenchaCodePackageConfigurer.getInstance();
    }

    this.packageConfigurers = new Configurer[]{
            defaultSenchaPackageConfigurer,
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
  }

  @Override
  public void createModule() throws MojoExecutionException {

    File workingDirectory;
    try {
      workingDirectory = new File(senchaPackagePath).getCanonicalFile();
    } catch (IOException e) {
      throw new MojoExecutionException("could not determine working directory", e);
    }

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("could not create working directory " + workingDirectory);
    }

    File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_CONFIG);
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      if (!senchaCfg.delete()) {
        throw new MojoExecutionException("could not delete " + SenchaUtils.SENCHA_PACKAGE_CONFIG + " for package");
      }
    }

    // This is a workaround

    // we must use the --name parameter to specify a path to the package directory as workspace.json cannot be modified
    // because of problems with parallel builds

    // because using "/" in package name is not valid we must prevent sencha generate package to create a package.json
    // otherwise a temporary state exists where the whole workspace fails to build because of an invalid package name.
    // this can be achieved by creating a package.json before sencha generate package is triggered.
    writePackageJson(workingDirectory);

    Path pathToWorkingDirectory = SenchaUtils.getRelativePathFromWorkspaceToWorkingDir(workingDirectory);

      String arguments = "generate package"
              + " --name=\"" + getSenchaModuleName() + "\""
              + " --namespace=\"\""
              + " --type=\"code\""
              + " " + pathToWorkingDirectory;
      getLog().info("generating sencha package module");
      SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
      senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      PrintWriter pw = null;
      FileWriter fw = null;
      try {
        fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);
        pw = new PrintWriter(fw);
        pw.println("skip.sass=1");
        pw.println("skip.slice=1");
      } catch (IOException e) {
        throw new MojoExecutionException("could not append skip.sass and skip.slice to sencha config of package");
      } finally {
        IOUtils.closeQuietly(pw);
        IOUtils.closeQuietly(fw);
      }
    } else {
      throw new MojoExecutionException("could not find sencha.cfg of package");
    }

  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      getLog().info("generating sencha package into: " + senchaPackageDirectory.getPath());
      getLog().debug("created " + senchaPackageDirectory.mkdirs());
    }

    copyFiles(senchaPackagePath);
    addRegisterEditorPluginsResource(senchaPackagePath);

    File workingDirectory = new File(senchaPackagePath);

    writePackageJson(workingDirectory);

    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + SENCHA_PACKAGE_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "pkg.file.name", "${package.name}.pkg",
            "pkg.build.dir", "${package.dir}/build"));
  }

  @Override
  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      throw new MojoExecutionException("sencha package directory does not exist: " + senchaPackageDirectory.getPath());
    }

    if (getSenchaConfiguration().isScssFromSrc()) {
      // rewrite package.json so the src path is removed in build
      getSenchaConfiguration().setScssFromSrc(false);
      writePackageJson(senchaPackageDirectory);
      getSenchaConfiguration().setScssFromSrc(true);
    }

    buildSenchaPackage(senchaPackageDirectory);

    File pkg = new File(senchaPackageBuildOutputDir + getSenchaModuleName() + SenchaUtils.SENCHA_PKG_EXTENSION);
    if (!pkg.exists()) {
      throw new MojoExecutionException("could not find " + SenchaUtils.SENCHA_PKG_EXTENSION + " for sencha package " + getSenchaModuleName());
    }

    if (getSenchaConfiguration().isScssFromSrc()) {
      // rewrite package.json so the src path is removed in build
      getSenchaConfiguration().setScssFromSrc(true);
      writePackageJson(senchaPackageDirectory);
      getSenchaConfiguration().setScssFromSrc(false);
    }

    return pkg;
  }

  private void writePackageJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> packageConfig = getPackageConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, packageConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_PACKAGE_FILENAME, e);
    }
  }

  private void buildSenchaPackage(File senchaPackageDirectory) throws MojoExecutionException {
    getLog().info("building sencha package module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaPackageDirectory, "package build", getLog());
    senchaCmdExecutor.execute();
  }

  private Map<String, Object> getPackageConfig() throws MojoExecutionException {
    return getConfig(packageConfigurers);
  }

}
