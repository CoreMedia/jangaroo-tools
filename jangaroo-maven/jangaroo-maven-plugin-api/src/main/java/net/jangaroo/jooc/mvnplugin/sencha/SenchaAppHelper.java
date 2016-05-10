package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
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
import java.util.UUID;

public class SenchaAppHelper extends SenchaPackageOrAppHelper<SenchaAppConfiguration, SenchaAppConfigBuilder> {

  public static final String DEFAULT_LOCALE = "en";

  private static final String APP_JSON_FILENAME = "/app.json";
  private static final String PRODUCTION_BUILD_PATH = "/build/production";
  private static final String APP_BUILD_PROPERTIES_FILE = "/.sencha/app/build.properties";

  private final String senchaAppPath;

  public SenchaAppHelper(MavenProject project, SenchaAppConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    this.senchaAppPath = project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY;
  }

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
        // If true will cause problems with class pre- and post-processors we use
        pw.println("app.output.js.optimize.defines=false");
        // If 0.99 (default), some deprecated API will not be available in production build:
        pw.println("build.options.minVersion=0");
      } catch (IOException e) {
        throw new MojoExecutionException("Could not write configuration to " + senchaCfg);
      }

    } else {
      throw new MojoExecutionException("Could not find sencha.cfg of package");
    }
  }

  public void prepareModule() throws MojoExecutionException {
    File senchaDirectory = new File(senchaAppPath);

    if (!senchaDirectory.exists()) {
      getLog().info("Generating Sencha into: " + senchaDirectory.getPath());
      getLog().debug("Created " + senchaDirectory.mkdirs());
    }

    copyFiles(senchaAppPath);

    File workingDirectory = new File(senchaAppPath);

    writeJson(new File(workingDirectory, APP_JSON_FILENAME));
    writeAppJs(workingDirectory);

    File buildPropertiesFile = new File(workingDirectory.getAbsolutePath() + APP_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "build.dir", "${app.dir}/build/${build.environment}",
            "build.temp.dir", "${app.dir}/build/temp/${build.environment}"
    ));
  }

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

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }

  @Override
  protected void configure(SenchaAppConfigBuilder configBuilder) throws MojoExecutionException {
    super.configure(configBuilder);
    configBuilder.id(generateSenchaAppId());
  }

  @Override
  protected String getDefaultsJsonFileName() {
    return "default.app.json";
  }

}
