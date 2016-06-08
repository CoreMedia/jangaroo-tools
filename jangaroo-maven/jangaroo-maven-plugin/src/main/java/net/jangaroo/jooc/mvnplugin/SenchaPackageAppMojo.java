package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaPackageAppMojo extends AbstractSenchaPackageOrAppMojo<SenchaAppConfigBuilder> {

  public static final String DEFAULT_LOCALE = "en";

  private static final String APP_JSON_FILENAME = "/app.json";
  private static final String BUILD_PATH = "/build/";
  private static final String APP_BUILD_PROPERTIES_FILE = "/.sencha/app/build.properties";


  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession mavenSession;

  /**
   * The full qualified name of the application class of the Sencha app, e.g.:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApllication&lt;/applicationClass>
   * </pre>
   */
  @Parameter
  private String applicationClass;

  /**
   * Supported locales in addition to the default locale "{@value DEFAULT_LOCALE}"
   */
  @Parameter()
  private List<String> additionalLocales = Collections.emptyList();

  /**
   * Choose to create a 'development' build of the Sencha App instead of the standard 'production' build.
   * Note that when you do a 'mvn install -DsenchaAppBuild=development', an incomplete artifact is installed!
   */
  @Parameter(defaultValue = "${senchaAppBuild}")
  private String senchaAppBuild;

  private String senchaAppPath;

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private JarArchiver archiver;

  @Override
  public String getType() {
    return Type.APP;
  }

  @Override
  public String getJsonConfigFileName() {
    return SenchaUtils.SENCHA_APP_FILENAME;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app\"");
    }
    // parameter can not just be required="true" as this would also apply for the other packaging types and mojos
    if (StringUtils.isBlank(applicationClass)) {
      throw new MojoExecutionException("\"applicationClass\" is missing. This configuration is mandatory for \"jangaroo-app\" packaging.");
    }
    if (StringUtils.isEmpty(senchaAppBuild)) {
      senchaAppBuild = SenchaUtils.PRODUCTION_PROFILE;
    }
    if (!(SenchaUtils.PRODUCTION_PROFILE.equals(senchaAppBuild) || SenchaUtils.DEVELOPMENT_PROFILE.equals(senchaAppBuild))) {
      throw new MojoExecutionException("'senchaAppBuild' must be one of 'production' or 'development'.");
    }

    senchaAppPath = project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY;

    prepareModule();
    File appProductionBuildDir = packageModule();
    createJarFromProductionBuild(appProductionBuildDir);

  }

  private void createJarFromProductionBuild(File appProductionBuildDir) throws MojoExecutionException {

    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");

    // add the Jangaroo compiler resources to the resulting JAR
    archiver.addFileSet(fileSet( appProductionBuildDir ).prefixed( "META-INF/resources/" ));

    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    try {

      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
      mavenArchiver.createArchive(mavenSession, project, archive);

    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }

    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(Type.JAR_EXTENSION));
  }


  public void prepareModule() throws MojoExecutionException {
    File senchaDirectory = new File(senchaAppPath);

    if (!senchaDirectory.exists()) {
      getLog().info("Generating Sencha into: " + senchaDirectory.getPath());
      getLog().debug("Created " + senchaDirectory.mkdirs());
    }

    FileHelper.copyFiles(getSenchaSrcDir(), senchaDirectory);

    SenchaAppConfigBuilder senchaConfigBuilder = createSenchaConfigBuilder();

    try {
      configure(senchaConfigBuilder, senchaAppPath);
      senchaConfigBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not build app file", e);
    }

    writeAppJs(senchaDirectory);

    File buildPropertiesFile = new File(senchaDirectory.getAbsolutePath() + APP_BUILD_PROPERTIES_FILE);
    FileHelper.writeBuildProperties(buildPropertiesFile, ImmutableMap.of(
            "build.dir", "${app.dir}/build/${build.environment}",
            "build.temp.dir", "${app.dir}/build/temp/${build.environment}"
    ));
  }

  protected void configure(SenchaAppConfigBuilder configBuilder, String workingDirectory)
          throws IOException, MojoExecutionException {

    configBuilder.destFile(workingDirectory + APP_JSON_FILENAME);
    configBuilder.defaults("default.app.json");
    configBuilder.id(generateSenchaAppId());
    configureLocales(configBuilder);

    configure(configBuilder);
  }

  public void configureLocales(SenchaAppConfigBuilder configBuilder) {
    configBuilder.locale(DEFAULT_LOCALE);
    for (String locale : additionalLocales) {
      configBuilder.locale(locale);
    }
    if (!additionalLocales.isEmpty()) {
      configBuilder.require("locale");
    }
  }

  @Nonnull
  public File packageModule() throws MojoExecutionException {
    File senchaAppDirectory = new File(senchaAppPath);

    if (!senchaAppDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaAppDirectory.getPath());
    }

    buildSenchaApp(senchaAppDirectory, senchaAppBuild);

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());

    if (null == workspaceDir) {
      throw new MojoExecutionException("Could not find Sencha workspace directory ");
    }

    File appOutputDirectory = new File(senchaAppPath + BUILD_PATH + senchaAppBuild);
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
      getLog().info(String.format("Generating %s with main application class %s.", appJs, applicationClass));
      try {
        String defineAppJsStatement = String.format("Ext.application(%s);", CompilerUtils.quote(applicationClass));
        Files.write(appJs, defineAppJsStatement.getBytes());
      } catch (IOException e) {
        throw new MojoExecutionException("An error occurred during creation of " + appJs, e);
      }
    }
  }

  private String generateSenchaAppId() {
    String appIdString = SenchaUtils.getSenchaPackageName(project) +
            SenchaUtils.getSenchaVersionForMavenVersion(project.getVersion());
    return UUID.nameUUIDFromBytes(appIdString.getBytes()).toString();
  }

  private void buildSenchaApp(File senchaAppDirectory, String buildEnvironment) throws MojoExecutionException {
    getLog().info("Building Sencha app module for build environment '" + buildEnvironment + "'.");
    StringBuilder args = new StringBuilder();
    args.append("app build")
            .append(" --").append(buildEnvironment)
            .append(" --locale " + DEFAULT_LOCALE);
    args.append(" then config -prop skip.sass=1 -prop skip.resources=1");
    for (String locale : additionalLocales) {
      args.append(" then app build")
              .append(" --").append(buildEnvironment)
              .append(" --locale ").append(locale);
    }

    getLog().info(String.format("Execute sencha cmd for %s with arguments %s", buildEnvironment, args.toString()));

    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, args.toString(), getLog());
    senchaCmdExecutor.execute();
  }

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }
}
