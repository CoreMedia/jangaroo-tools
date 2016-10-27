package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaWorkspaceConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SenchaPackageAppMojo extends AbstractSenchaPackageOrAppMojo<SenchaAppConfigBuilder> {

  public static final String DEFAULT_LOCALE = "en";

  private static final String APP_JSON_FILENAME = "/app.json";
  private static final String PACKAGES_PATH_NAME = "packages";
  public static final String JANGAROO_APP_DIRECTORY = "build/jangaroo-app";
  public static final String EXT_TARGET_DIRECTORY = "ext";

  private static final String[] PACKAGE_INCLUDES = new String[]{"*.js.map", "*.js", "src/**", "saas/**", "locale/**", "resources/**", "bundledResources/**"};
  private static final String[] EXT_FRAMEWORK_INCLUDES = new String[]{".sencha/**", "build/**", "classic/**", "cmd/**", "framework/**", "license/**", "packages/**", "*.*"};
  private static final String[] EXT_FRAMEWORK_EXCLUDES = null;


  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession mavenSession;

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

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY, readonly = true)
  private File senchaAppDirectory;

  @Parameter(defaultValue = "${localRepository}", required = true)
  private ArtifactRepository localRepository;

  @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
  private List<ArtifactRepository> remoteRepositories;

  @Parameter
  private String applicationClass;

  @Parameter(property = "skipJangarooApp", defaultValue = "false")
  private boolean skipJangarooApp;

  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Inject
  protected RepositorySystem repositorySystem;

  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Inject
  private ArtifactResolver artifactResolver;

  @Inject
  private ArchiverManager archiverManager;

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

    if (!skipJangarooApp) {
      File workingDirectory = generateJangarooApp();

      // refresh app
      new SenchaCmdExecutor(workingDirectory, "config -prop skip.sass=1 -prop skip.resources=1 then app refresh", getLog(), getSenchaLogLevel()).execute();

      createJarFromJangarooBuild();
    }
  }

  private File generateJangarooApp() throws MojoExecutionException {
    File workingDirectory = new File(senchaAppDirectory, JANGAROO_APP_DIRECTORY);
    FileHelper.ensureDirectory(workingDirectory);

    // we need to have a new workspace
    createJangarooAppWorkspace(workingDirectory);

    // extract all module packages files
    extractPackagesDirs(workingDirectory);

    // we need to copy some files, so Sencha knows it is an app dir
    copyFilesFromDevelopmentBuild(workingDirectory);

    // we need to fix the output dir, not needed if we copy directory
    fixAppJson(workingDirectory);

    // let Sencha create the app files with correct relative paths
    SenchaUtils.refreshApp(workingDirectory, getLog(), getSenchaLogLevel());

    return workingDirectory;
  }

  private void copyFilesFromDevelopmentBuild(@Nonnull File workingDirectory) throws MojoExecutionException {
    // copy some files from the development app
/*    FileHelper.copyDirectory(new File(senchaAppDirectory, SenchaUtils.SENCHA_DIRECTORYNAME + "/app"), new File(workingDirectory, SenchaUtils.SENCHA_DIRECTORYNAME));
    // need for config-init.js and before--ext-load.js and also for build css files
    FileHelper.copyDirectory(new File(senchaAppDirectory, "build/development/resources"), workingDirectory);
    FileHelper.copyFilesToDirectory(senchaAppDirectory, workingDirectory, "app.*|build.*|index.html");*/

    FileHelper.copyDirectories(senchaAppDirectory, workingDirectory, ImmutableSet.of("build"));
    FileHelper.copyDirectory(new File(senchaAppDirectory, "build/development/resources"), workingDirectory);
    FileHelper.copyFilesToDirectory(senchaAppDirectory, workingDirectory, "app.*|build.*|index.html");
  }

  private void createJangarooAppWorkspace(@Nonnull File newWorkspaceDirectory) throws MojoExecutionException {

    // Plan A: copy necessary files to new workspace
    File workspaceDirectory = SenchaUtils.findClosestSenchaWorkspaceDir(senchaAppDirectory);
    if (workspaceDirectory == null) {
      throw new MojoExecutionException("Could not find any workspace");
    }

    File workspaceJson = new File(newWorkspaceDirectory, SenchaUtils.SENCHA_WORKSPACE_FILENAME);
    FileHelper.copyDirectory(new File(workspaceDirectory, SenchaUtils.SENCHA_DIRECTORYNAME + "/workspace"), new File(newWorkspaceDirectory, SenchaUtils.SENCHA_DIRECTORYNAME));

    Path senchaCfg = Paths.get(workspaceDirectory.getAbsolutePath(), SenchaUtils.SENCHA_WORKSPACE_CONFIG);
    Path newSenchaCfg = Paths.get(newWorkspaceDirectory.getAbsolutePath(), SenchaUtils.SENCHA_WORKSPACE_CONFIG);

    SenchaUtils.createSenchaCfgWithExtDirectory(senchaCfg, newSenchaCfg, EXT_TARGET_DIRECTORY);

    // we only need to configure the packages dir used
    SenchaWorkspaceConfigBuilder configBuilder = new SenchaWorkspaceConfigBuilder();
    configBuilder.packagesDirs(ImmutableList.of(PACKAGES_PATH_NAME));
    configBuilder.destFile(workspaceJson);
    try {
      configBuilder.buildFile();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create workspace.json file", e);
    }

    // Alternative: use sencha generate workspace, then we do not need to copy ext framework in extractPackages!!!
    // SenchaUtils.generateSenchaWorkspace(workingDirectory, "ext", getLog(), getSenchaLogLevel());
  }


  private void extractPackagesDirs(File targetDir) throws MojoExecutionException {

    FileHelper.ensureDirectory(new File(targetDir, PACKAGES_PATH_NAME));

    // prevent unpacking for jar and pkg dependency
    Set<String> extractedModules = new HashSet<>();

    // get all dependencies including transitive ones
    Set<Artifact> artifacts = project.getArtifacts();
    for(Artifact artifact: artifacts) {
       try {
        String senchaPackageName = getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId());

        if (!extractedModules.contains(senchaPackageName)) {
          extractedModules.add(senchaPackageName);
          if (isExtFrameworkArtifact(artifact)) {
            // handle ext framework differently
            File extTargetDir = new File(targetDir, EXT_TARGET_DIRECTORY);
            getLog().info("Extract Ext framework to " + extTargetDir);
            extractPackageForProduction(artifact, extTargetDir, EXT_FRAMEWORK_INCLUDES, EXT_FRAMEWORK_EXCLUDES);
          } else {
            // extract pkg to packages dir
            File packaggeTargetDir = new File(targetDir, PACKAGES_PATH_NAME + "/" + senchaPackageName);
            extractPackageForProduction(artifact, packaggeTargetDir, null, null);
          }
        }
        // MojoExecutionException| ArchiverException
      } catch (Exception  e) {
        getLog().error(e.getMessage(), e);
      }

    }
  }

  private void extractPackageForProduction(Artifact artifact, File targetDir, String[] includes, String[] excludes) throws MojoExecutionException {

    // jar dependency, get pkg artifact and extract it somewhere under packages/groupId__artifactId
    Artifact pkgArtifact = MavenPluginHelper.getArtifact(localRepository, remoteRepositories, artifactResolver,
            repositorySystem, artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), "runtime", Type.PACKAGE_EXTENSION);
    if (pkgArtifact == null) {
      getLog().info("Could not find artifact for " + artifact);
      return;
    }

    File pkgArtifactFile = pkgArtifact.getFile();
    FileHelper.ensureDirectory(targetDir);

    getLog().info("Extract pkg file: " + pkgArtifactFile.getName());
    MavenPluginHelper.extractFileTemplate(targetDir, pkgArtifactFile, includes, excludes, archiverManager);
  }

  private void createJarFromJangarooBuild() throws MojoExecutionException {

    File appProductionBuildDir = new File(senchaAppDirectory, JANGAROO_APP_DIRECTORY);

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
    // necessary?
    FileHelper.ensureDirectory(senchaAppDirectory);
    getLog().info(String.format("Copy files from %s to %s", getSenchaSrcDir().getPath(), senchaAppDirectory.getPath()));
    FileHelper.copyFiles(getSenchaSrcDir(), senchaAppDirectory);

    SenchaAppConfigBuilder senchaConfigBuilder = createSenchaConfigBuilder();
    configure(senchaConfigBuilder);

    writeFile(senchaConfigBuilder, senchaAppDirectory.getPath(), APP_JSON_FILENAME, null);
  }

  protected void configure(SenchaAppConfigBuilder configBuilder)
          throws  MojoExecutionException {

    configureDefaults(configBuilder, "default.app.json");

    super.configure(configBuilder);

    configBuilder.id(generateSenchaAppId());
    configureLocales(configBuilder);
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

  public void packageModule() throws MojoExecutionException {
    if (!senchaAppDirectory.exists()) {
      throw new MojoExecutionException("Sencha package directory does not exist: " + senchaAppDirectory.getPath());
    }

    // TODO we do not want to build production!
    buildSenchaApp(senchaAppDirectory, SenchaUtils.DEVELOPMENT_PROFILE);

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getBasedir());
    if (null == workspaceDir) {
      throw new MojoExecutionException("Could not find Sencha workspace directory ");
    }
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
    args.append(" then config -prop skip.sass=1 -prop skip.resources=1");
    for (String locale : additionalLocales) {
      args.append(" then app build")
              .append(" --").append(buildEnvironment)
              .append(" --locale ").append(locale);
    }

    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(senchaAppDirectory, args.toString(), getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();
  }

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }

  private Map<String, Object> readJson(File jsonFile) throws IOException {
    //noinspection unchecked
    return (Map<String, Object>) SenchaUtils.getObjectMapper().readValue(jsonFile, Map.class);
  }

  private boolean isExtFrameworkArtifact(Artifact artifact) {
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(getExtFrameworkArtifact());

    return extFrameworkDependency.getGroupId().equals(artifact.getGroupId())
            && extFrameworkDependency.getArtifactId().equals(artifact.getArtifactId());
  }

  private void fixAppJson(File workingDirectory) throws MojoExecutionException {
    // TODO fix app.json, fix base dir for output
    try {
      File jangarooAppJsonFile = new File(workingDirectory, SenchaUtils.SENCHA_APP_FILENAME);
      Map<String, Object> appJson = readJson(jangarooAppJsonFile);

      Map<String, String> outputMap = (Map<String, String>) appJson.get("output");
      outputMap.put("base", "${app.dir}");

/*      // fix classic/js/path
      Map<String, Object> classicMap = (Map<String, Object>) appJson.get("classic");
      List<Map<String,String>> jsPaths = (List<Map<String,String>>) classicMap.get("js");
      for(Map<String,String> jsConfig: jsPaths) {
        String path = jsConfig.get("path");
        String frameworkPrefix = "${framework.dir}/";
        if (path.startsWith(frameworkPrefix)) {
          jsConfig.put("path", path.substring(frameworkPrefix.length()));
        }
      }*/

      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(jangarooAppJsonFile, appJson);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not configure app.json", e);
    }
  }
}
