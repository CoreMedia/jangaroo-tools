package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.apprunner.util.AppManifestDeSerializer;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import net.jangaroo.jooc.mvnplugin.util.MergeHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DEFAULT_LOCALE;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isRequiredSenchaDependency;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app",
        defaultPhase = LifecyclePhase.PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SenchaPackageAppMojo extends AbstractSenchaPackageOrAppMojo<SenchaAppConfigBuilder> {

  /**
   * Supported locales in addition to the default locale "{@value SenchaUtils#DEFAULT_LOCALE}"
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
    FileHelper.createAppOrAppOverlayJar(session, archiver, artifactHandlerManager, senchaAppBuild);
  }

  private void prepareModule() throws MojoExecutionException {
    FileHelper.ensureDirectory(senchaAppDirectory);
    getLog().info(String.format("Copy files from %s to %s", getSenchaSrcDir().getPath(), senchaAppDirectory.getPath()));
    FileHelper.copyFiles(getSenchaSrcDir(), senchaAppDirectory);

    SenchaAppConfigBuilder senchaConfigBuilder = createSenchaConfigBuilder();
    configure(senchaConfigBuilder);
    SenchaUtils.writeFile(senchaConfigBuilder, senchaAppDirectory.getPath(), SENCHA_APP_FILENAME, null, getLog());

    Set<String> locales = new HashSet<>();
    locales.add(DEFAULT_LOCALE);
    locales.addAll(additionalLocales);
    List<Artifact> packages = project.getArtifacts().stream().filter(artifact ->
            !isExtFrameworkArtifact(artifact) &&
                    isRequiredSenchaDependency(MavenDependencyHelper.fromArtifact(artifact), false))
            .collect(Collectors.toList());
    Map<String, Map<String, Object>> appManifestByLocale = prepareAppManifestByLocale(locales, packages);
    for (String locale : appManifestByLocale.keySet()) {
      Map<String, Object> appManifest = appManifestByLocale.get(locale);
      Map<String, Object> localizedAppManifestFragment = new HashMap<>();
      if (!DEFAULT_LOCALE.equals(locale)) {
        File appManifestFragmentForDefaultLocale = new File(project.getBasedir(), getAppManifestFragmentFileNameForLocale(DEFAULT_LOCALE));
        if (appManifestFragmentForDefaultLocale.exists()) {
          try {
            MergeHelper.mergeMapIntoBaseMap(localizedAppManifestFragment, AppManifestDeSerializer.readAppManifest(new FileInputStream(appManifestFragmentForDefaultLocale)), APP_MANIFEST_LOCALIZATION_MERGE_STRATEGY);
          } catch (IOException e) {
            throw new MojoExecutionException("Could not read app manifest", e);
          }
        }
      }
      File appManifestFragmentForLocale = new File(project.getBasedir(), getAppManifestFragmentFileNameForLocale(locale));
      if (appManifestFragmentForLocale.exists()) {
        try {
          MergeHelper.mergeMapIntoBaseMap(localizedAppManifestFragment, AppManifestDeSerializer.readAppManifest(new FileInputStream(appManifestFragmentForLocale)), APP_MANIFEST_LOCALIZATION_MERGE_STRATEGY);
        } catch (IOException e) {
          throw new MojoExecutionException("Could not read app manifest", e);
        }
      }
      MergeHelper.mergeMapIntoBaseMap(appManifest, localizedAppManifestFragment, APP_MANIFEST_CROSS_MODULE_MERGE_STRATEGY);
    }

    writeAppManifestJsonByLocale(appManifestByLocale);
  }

  private void configure(SenchaAppConfigBuilder configBuilder)
          throws  MojoExecutionException {
    SenchaUtils.configureDefaults(configBuilder, "default.app.json");
    super.configure(configBuilder);
    configBuilder.id(generateSenchaAppId());
    configureLocales(configBuilder);
  }

  private void configureLocales(SenchaAppConfigBuilder configBuilder) {
    configBuilder.locale(SenchaUtils.DEFAULT_LOCALE);
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
            .append(" --locale " + SenchaUtils.DEFAULT_LOCALE);
    if (!additionalLocales.isEmpty()) {
      args.append(" then config -prop skip.sass=1 -prop skip.resources=1");
      for (String locale : additionalLocales) {
        args.append(" then app build")
                .append(" --").append(buildEnvironment)
                .append(" --locale ").append(locale);
      }
    }
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, args.toString(), getSenchaJvmArgs(), getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();
  }

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }

  private void writeAppManifestJsonByLocale(Map<String, Map<String, Object>> appManifestByLocale) throws MojoExecutionException {
    for (String locale : appManifestByLocale.keySet()) {
      String appManifestFileName = getAppManifestFileNameForLocale(locale);
      File appManifestFile = prepareFile(new File(senchaAppDirectory, appManifestFileName));

      try {
        AppManifestDeSerializer.writeAppManifest(new FileOutputStream(appManifestFile), appManifestByLocale.get(locale));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not create " + appManifestFile + " resource", e);
      }
    }
  }

}
