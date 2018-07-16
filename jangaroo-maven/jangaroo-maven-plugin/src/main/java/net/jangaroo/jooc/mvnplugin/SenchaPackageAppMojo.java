package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.AbstractArchiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.FileSet;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.util.DefaultFileSet;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResourceCollection;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGES_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_TEMPLATE_ARTIFACT_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SEPARATOR;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app",
        defaultPhase = LifecyclePhase.PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SenchaPackageAppMojo extends AbstractSenchaPackageOrAppMojo<SenchaAppConfigBuilder> {

  private static final String DEFAULT_LOCALE = "en";

  private static final String APP_JSON_FILENAME = "/app.json";

  /**
   * Supported locales in addition to the default locale "{@value DEFAULT_LOCALE}"
   */
  @Parameter()
  private List<String> additionalLocales = Collections.emptyList();

  /**
   * Choose to create a 'production' build of the Sencha App instead of the standard 'development' build.
   * Note that when you do a 'mvn install -DsenchaAppBuild=production', the <em>Jangaroo app</em> build will be skipped!
   */
  @Parameter(property = "senchaAppBuild")
  private String senchaAppBuild = SenchaUtils.DEVELOPMENT_PROFILE;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY, readonly = true)
  private File senchaAppDirectory;

  @Parameter(defaultValue = "${localRepository}", required = true)
  private ArtifactRepository localRepository;

  @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
  private List<ArtifactRepository> remoteRepositories;

  @Parameter
  private String applicationClass;

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
    if (StringUtils.isEmpty(senchaAppBuild)) {
      senchaAppBuild = SenchaUtils.DEVELOPMENT_PROFILE;
    }
    if (!(SenchaUtils.PRODUCTION_PROFILE.equals(senchaAppBuild)
            || SenchaUtils.DEVELOPMENT_PROFILE.equals(senchaAppBuild)
            || SenchaUtils.TESTING_PROFILE.equals(senchaAppBuild))) {
      throw new MojoExecutionException("'senchaAppBuild' must be one of 'production', 'testing' or 'development'.");
    }

    prepareModule();
    packageModule();
    createJar();
  }

  private void createJar() throws MojoExecutionException {
    File appProductionBuildDir = senchaAppDirectory;
    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");
    if (SenchaUtils.DEVELOPMENT_PROFILE.equals(senchaAppBuild)) {
      // add the Jangaroo compiler resources to the resulting JAR
      DefaultFileSet fileSet = fileSet(appProductionBuildDir).prefixed(MavenPluginHelper.META_INF_RESOURCES);
      fileSet.setExcludes(new String[]{
              "**/build/temp/**",
              "**/" + PACKAGES_DIRECTORY_NAME + SEPARATOR + getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_APP_TEMPLATE_ARTIFACT_ID) + "/**",
              PACKAGES_DIRECTORY_NAME + SEPARATOR + getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID) + "/**",
              "**/*-timestamp"
      });
      addFileSet(archiver, fileSet);
    }
    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    try {
      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
      mavenArchiver.createArchive(session, project, archive);
    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }
    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(Type.JAR_EXTENSION));
  }

  /**
   * Method copied + slightly adapted from AbstractArchiver because of symlink handling (see below).
   */
  private static void addFileSet(@Nonnull AbstractArchiver archiver, @Nonnull final FileSet fileSet )
          throws ArchiverException
  {
    final File directory = fileSet.getDirectory();

    // The PlexusIoFileResourceCollection contains platform-specific File.separatorChar which
    // is an interesting cause of grief, see PLXCOMP-192
    final PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();

    // ALWAYS follow symlinks
    collection.setFollowingSymLinks(true);

    collection.setIncludes( fileSet.getIncludes() );
    collection.setExcludes( fileSet.getExcludes() );
    collection.setBaseDir( directory );
    collection.setFileSelectors( fileSet.getFileSelectors() );
    collection.setIncludingEmptyDirectories( fileSet.isIncludingEmptyDirectories() );
    collection.setPrefix( fileSet.getPrefix() );
    collection.setCaseSensitive( fileSet.isCaseSensitive() );
    collection.setUsingDefaultExcludes( fileSet.isUsingDefaultExcludes() );
    collection.setStreamTransformer( fileSet.getStreamTransformer() );

    if ( archiver.getOverrideDirectoryMode() > -1 || archiver.getOverrideFileMode() > -1 )
    {
      collection.setOverrideAttributes( -1, null, -1, null, archiver.getOverrideFileMode(), archiver.getOverrideDirectoryMode() );
    }

    if ( archiver.getDefaultDirectoryMode() > -1 || archiver.getDefaultFileMode() > -1 )
    {
      collection.setDefaultAttributes( -1, null, -1, null, archiver.getDefaultFileMode(), archiver.getDefaultDirectoryMode() );
    }

    archiver.addResources( collection );
  }

  private void prepareModule() throws MojoExecutionException {
    FileHelper.ensureDirectory(senchaAppDirectory);
    getLog().info(String.format("Copy files from %s to %s", getSenchaSrcDir().getPath(), senchaAppDirectory.getPath()));
    FileHelper.copyFiles(getSenchaSrcDir(), senchaAppDirectory);

    SenchaAppConfigBuilder senchaConfigBuilder = createSenchaConfigBuilder();
    configure(senchaConfigBuilder);
    SenchaUtils.writeFile(senchaConfigBuilder, senchaAppDirectory.getPath(), APP_JSON_FILENAME, null, getLog());
  }

  private void configure(SenchaAppConfigBuilder configBuilder)
          throws  MojoExecutionException {
    SenchaUtils.configureDefaults(configBuilder, "default.app.json");
    super.configure(configBuilder);
    configBuilder.id(generateSenchaAppId());
    configureLocales(configBuilder);
  }

  private void configureLocales(SenchaAppConfigBuilder configBuilder) {
    configBuilder.locale(DEFAULT_LOCALE);
    for (String locale : additionalLocales) {
      configBuilder.locale(locale);
    }
    if (!additionalLocales.isEmpty()) {
      configBuilder.require("locale");
    }
  }

  private void packageModule() throws MojoExecutionException {
    if (!senchaAppDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaAppDirectory.getPath());
    }
    buildSenchaApp(senchaAppDirectory, senchaAppBuild);
  }

  private String generateSenchaAppId() {
    String appIdString = getSenchaPackageName(project) + SenchaUtils.getSenchaVersionForMavenVersion(project.getVersion());
    return UUID.nameUUIDFromBytes(appIdString.getBytes()).toString();
  }

  private void buildSenchaApp(File senchaAppDirectory, String buildEnvironment) throws MojoExecutionException {
    getLog().info("Building Sencha app module for build environment '" + buildEnvironment + "'.");
    StringBuilder args = new StringBuilder();
    args.append("app build")
            .append(" --").append(buildEnvironment)
            .append(" --locale " + DEFAULT_LOCALE);
    if (!additionalLocales.isEmpty()) {
      args.append(" then config -prop skip.sass=1 -prop skip.resources=1");
      for (String locale : additionalLocales) {
        args.append(" then app build")
                .append(" --").append(buildEnvironment)
                .append(" --locale ").append(locale);
      }
    }
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, args.toString(), getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();
  }

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }

}
