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
import java.util.List;

public class SenchaAppHelper extends SenchaPackageOrAppHelper<SenchaAppConfiguration, SenchaAppConfigBuilder> {

  public static final String DEFAULT_LOCALE = "en";

  private static final String APP_JSON_FILENAME = "/app.json";
  private static final String BUILD_PATH = "/build/";
  private static final String APP_BUILD_PROPERTIES_FILE = "/.sencha/app/build.properties";

  private final String senchaAppPath;
  private final String build;

  public SenchaAppHelper(MavenProject project, SenchaAppConfiguration senchaConfiguration, Log log, String build) {
    super(project, senchaConfiguration, log);
    this.build = build;

    this.senchaAppPath = project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY;
  }

  public void createModule() throws MojoExecutionException {
    File workingDirectory = new File(senchaAppPath);

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("Could not create working directory.");
    }

    File senchaCfg = new File(workingDirectory, SenchaUtils.SENCHA_APP_CONFIG);
    // only generate app if senchaCfg does not exist
    if (senchaCfg.exists()) {
      return;
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

    // sencha.cfg should have been recreated.
    if (!senchaCfg.exists()) {
      throw new MojoExecutionException("Could not find sencha.cfg of app");
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(senchaCfg.getAbsoluteFile(), true))) {
      // For apps, skip generating slices.
      pw.println("skip.slice=1");
      // If true will cause problems with class pre- and post-processors we use.
      pw.println("app.output.js.optimize.defines=false");
      // If 0.99 (default), some deprecated API will not be available in production build:
      pw.println("build.options.minVersion=0");
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write configuration to " + senchaCfg);
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

    buildSenchaApp(senchaAppDirectory, build);

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(getProject().getBasedir());

    if (null == workspaceDir) {
      throw new MojoExecutionException("Could not find Sencha workspace directory ");
    }

    File appOutputDirectory = new File(senchaAppPath + BUILD_PATH + build);
    if (!appOutputDirectory.isDirectory() && !appOutputDirectory.exists()) {
      throw new MojoExecutionException("Could not find build directory for Sencha app " + appOutputDirectory);
    }

    return appOutputDirectory;
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

  private void buildSenchaApp(File senchaAppDirectory, String buildEnvironment) throws MojoExecutionException {
    getLog().info("Building Sencha app module for build environment '" + buildEnvironment + "'.");
    List<String> locales = getSenchaConfiguration().getLocales();
    StringBuilder args = new StringBuilder();
    args.append("app build")
            .append(" --").append(buildEnvironment)
            .append(" --locale ").append(locales.isEmpty() ? DEFAULT_LOCALE : locales.get(0));
    args.append(" then config -prop skip.sass=1 -prop skip.resources=1");
    for (int i = 1; i < locales.size(); i++) {
      args.append(" then app build")
              .append(" --").append(buildEnvironment)
              .append(" --locale ").append(locales.get(i));
    }
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, args.toString(), getLog());
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
    configureLocales(configBuilder);
  }

  public void configureLocales(SenchaAppConfigBuilder configBuilder) throws MojoExecutionException {
    List<String> locales = getSenchaConfiguration().getLocales();
    configBuilder.defaultLocale(locales.isEmpty() ? DEFAULT_LOCALE : locales.get(0));
    for (String locale : locales) {
      configBuilder.locale(locale);
    }
    if (locales.size() > 1) {
      configBuilder.require("locale");
    }
  }

  @Override
  protected String getDefaultsJsonFileName() {
    return "default.app.json";
  }

}
