package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.MavenSenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaApplicationConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class SenchaAppHelper extends AbstractSenchaHelper {

  private static final String SENCHA_APP_BUILD_PROPERTIES_FILE = "/.sencha/app/build.properties";

  private final PathConfigurer pathConfigurer;
  private final Configurer[] appConfigurers;
  private final String senchaAppPath;

  public SenchaAppHelper(MavenProject project, MavenSenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    this.senchaAppPath = project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY;

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project, senchaConfiguration);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(project, senchaConfiguration);
    pathConfigurer = new PathConfigurer(senchaConfiguration);

    this.appConfigurers = new Configurer[]{
            DefaultSenchaApplicationConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
  }

  @Override
  public void createModule() throws MojoExecutionException {
    File workingDirectory = new File(senchaAppPath);

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("could not create working directory");
    }

    File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_CONFIG);
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      if (!senchaCfg.delete()) {
        throw new MojoExecutionException("could not delete " + SenchaUtils.SENCHA_APP_CONFIG + " for app");
      }
    }

      String themePackageName = SenchaUtils.getSenchaPackageNameForTheme(getSenchaConfiguration().getTheme(), getProject());

      String arguments = "generate app"
              + " -ext"
              + " -" + getSenchaConfiguration().getToolkit()
              + " --theme-name=\"" + themePackageName + "\""
              + " --path=\"\""
              + " " + getSenchaModuleName();
      getLog().info("generating sencha app module");
      SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
      senchaCmdExecutor.execute();

      // sencha.cfg should be recreated
      // for normal packages skip generating css and slices
      if (senchaCfg.exists()) {
        PrintWriter pw = null;
        try {
          FileWriter fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);

          pw = new PrintWriter(fw);
          pw.println("skip.slice=1");
          // If true will cause problems with class pre- and postprocessors we use
          pw.println("app.output.js.optimize.defines=false");
        } catch (IOException e) {
          throw new MojoExecutionException("could disable derive and minifying in sencha config of app");
        } finally {
          IOUtils.closeQuietly(pw);
          IOUtils.closeQuietly(fw);        }
      } else {
        throw new MojoExecutionException("could not find sencha.cfg of package");
      }
    }
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    File senchaDirectory = new File(senchaAppPath);

    if (!senchaDirectory.exists()) {
      getLog().info("generating sencha into: " + senchaDirectory.getPath());
      getLog().debug("created " + senchaDirectory.mkdirs());
    }

    copyFiles(senchaAppPath);

    File workingDirectory = new File(senchaAppPath);

    writeAppJson(workingDirectory);

    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + SENCHA_APP_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of("build.dir", "${app.dir}/build/${build.environment}"));
  }

  @Override
  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaAppDirectory = new File(senchaAppPath);

    if (!senchaAppDirectory.exists()) {
      throw new MojoExecutionException("sencha package directory does not exist: " + senchaAppDirectory.getPath());
    }

    if (getSenchaConfiguration().isScssFromSrc()) {
      // rewrite package.json so the src path is removed in build
      getSenchaConfiguration().setScssFromSrc(false);
      File workingDirectory = new File(senchaAppPath);
      writeAppJson(workingDirectory);
      getSenchaConfiguration().setScssFromSrc(true);
    }

    buildSenchaApp(senchaAppDirectory);

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(getProject().getBasedir());

    if (null == workspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace directory");
    }

    File productionDirectory = new File(senchaAppPath + "/build/" + SenchaUtils.SENCHA_RELATIVE_PRODUCTION_PATH);
    if (!productionDirectory.isDirectory() && !productionDirectory.exists()) {
      throw new MojoExecutionException("could not find production directory for sencha app " + productionDirectory);
    }


    if (getSenchaConfiguration().isScssFromSrc()) {
      // rewrite package.json so the src path is removed in build
      getSenchaConfiguration().setScssFromSrc(true);
      writeAppJson(senchaAppDirectory);
      getSenchaConfiguration().setScssFromSrc(false);
    }

    return productionDirectory;
  }


  private void writeAppJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> appConfig = getAppConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, appConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_APP_FILENAME, e);
    }

  }

  private void buildSenchaApp(File senchaAppDirectory) throws MojoExecutionException {
    getLog().info("building sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, "app build --production", getLog());
    senchaCmdExecutor.execute();
  }

  private Map<String, Object> getAppConfig() throws MojoExecutionException {
    return getConfig(appConfigurers);
  }
}
