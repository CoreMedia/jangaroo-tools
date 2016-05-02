package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaApplicationConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.ModuleConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.ProfileConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

public class SenchaAppHelper extends AbstractSenchaHelper<SenchaAppConfiguration> {

  private static final String APP_JSON_FILENAME = "/app.json";
  private static final String PRODUCTION_BUILD_PATH = "/build/production";
  private static final String APP_BUILD_PROPERTIES_FILE = "/.sencha/app/build.properties";
  private static final String APP_ID_ATTRIBUTE = "id";

  private final Configurer[] appConfigurers;
  private final String senchaAppPath;

  public SenchaAppHelper(MavenProject project, SenchaAppConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    this.senchaAppPath = project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY;

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project, senchaConfiguration);
    ModuleConfigurer moduleConfigurer = new ModuleConfigurer(project, senchaConfiguration, log);

    ProfileConfigurer commonProfileConfigurer = new ProfileConfigurer(getCommonProfileConfiguration());
    ProfileConfigurer productionProfileConfigurer = new ProfileConfigurer(getProductionProfileConfiguration(), PRODUCTION);
    ProfileConfigurer testingProfileConfigurer = new ProfileConfigurer(getTestingProfileConfiguration(), TESTING);
    ProfileConfigurer developmentProfileConfigurer = new ProfileConfigurer(getDevelopmentProfileConfiguration(), DEVELOPMENT);

    this.appConfigurers = new Configurer[]{
            DefaultSenchaApplicationConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            moduleConfigurer,
            commonProfileConfigurer,
            productionProfileConfigurer,
            testingProfileConfigurer,
            developmentProfileConfigurer
    };
  }

  @Override
  public void createModule() throws MojoExecutionException {
    File workingDirectory = new File(senchaAppPath);

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("Could not create working directory.");
    }

    File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_CONFIG);
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      if (!senchaCfg.delete()) {
        throw new MojoExecutionException("Could not delete " + senchaCfg);
      }
    }

    String arguments = "generate app"
            + " -ext"
            + " -" + getSenchaConfiguration().getToolkit()
            + " --path=\"\""
            + " " + getSenchaModuleName();
    getLog().info("Generating Sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
    senchaCmdExecutor.execute();

    // remove example resources from application's classpath directory
    File appDir = new File(workingDirectory.getAbsolutePath() + SENCHA_APP_CLASS_PATH);
    try {
      FileUtils.cleanDirectory(appDir);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not delete example app directory: " + appDir, e);
    }

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {

      try (PrintWriter pw = new PrintWriter(new FileWriter(senchaCfg.getAbsoluteFile(), true))) {
        pw.println("skip.slice=1");
        // If true will cause problems with class pre- and postprocessors we use
        pw.println("app.output.js.optimize.defines=false");
      } catch (IOException e) {
        throw new MojoExecutionException("Could not write configuration to " + senchaCfg);
      }

    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    File senchaDirectory = new File(senchaAppPath);

    if (!senchaDirectory.exists()) {
      getLog().info("Generating Sencha into: " + senchaDirectory.getPath());
      getLog().debug("Created " + senchaDirectory.mkdirs());
    }

    copyFiles(senchaAppPath);

    File workingDirectory = new File(senchaAppPath);

    writeAppJson(workingDirectory);
    writeAppJs(workingDirectory);

    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + APP_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "build.dir", "${app.dir}/build/${build.environment}",
            "build.temp.dir", "${app.dir}/build/temp/${build.environment}"
    ));
  }

  @Override
  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaAppDirectory = new File(senchaAppPath);

    if (!senchaAppDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaAppDirectory.getPath());
    }

    buildSenchaApp(senchaAppDirectory);

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(getProject().getBasedir());

    if (null == workspaceDir) {
      throw new MojoExecutionException("Could not find Sencha workspace directory ");
    }

    File productionDirectory = new File(senchaAppPath + PRODUCTION_BUILD_PATH);
    if (!productionDirectory.isDirectory() && !productionDirectory.exists()) {
      throw new MojoExecutionException("Could not find production directory for Sencha app " + productionDirectory);
    }

    return productionDirectory;
  }

  private void writeAppJs(File workingDirectory) throws MojoExecutionException {
    Path appJs = Paths.get(workingDirectory.getAbsolutePath() + "/resources/app.js");
    if (Files.exists(appJs)) {
      getLog().info(String.format("Using existing %s to define the main application class.", appJs));
    } else {
      String applicationClass = getSenchaConfiguration().getApplicationClass();
      getLog().info(String.format("Generating %s with main application class %s.", appJs, applicationClass));
      try {
        String defineAppJsStatement = String.format("Ext.application(\"AS3.%s\");", applicationClass);
        Files.write(appJs, defineAppJsStatement.getBytes());
      } catch (IOException e) {
        throw new MojoExecutionException("An error occurred during creation of " + appJs, e);
      }
    }
  }

  private void writeAppJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> appConfig = getAppConfig();
    appConfig.put(APP_ID_ATTRIBUTE, generateSenchaAppId());

    File fAppJson = new File(workingDirectory.getAbsolutePath() + APP_JSON_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, appConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write " + fAppJson, e);
    }
  }

  private String generateSenchaAppId() {
    String appIdString = SenchaUtils.getSenchaPackageName(getProject().getGroupId(), getProject().getArtifactId()) +
            SenchaUtils.getSenchaVersionForMavenVersion(getProject().getVersion());
    return UUID.nameUUIDFromBytes(appIdString.getBytes()).toString();
  }

  private void buildSenchaApp(File senchaAppDirectory) throws MojoExecutionException {
    getLog().info("Building Sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, "app build --production", getLog());
    senchaCmdExecutor.execute();
  }

  private Map<String, Object> getAppConfig() throws MojoExecutionException {
    return getConfig(appConfigurers);
  }
}
