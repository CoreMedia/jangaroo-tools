package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaWorkspaceConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.*;

/**
 * Generates and packages Sencha app module.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Mojo(name = "generate-ws",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class SenchaGenerateWsMojo extends AbstractLinkPackagesMojo {

  private static final String EXT_TARGET_DIRECTORY = "ext";

  /**
   * Non-null if we have joounit tests in this module
   */
  @Parameter
  private String testSuite = null;


  @Override
  public void execute() throws MojoExecutionException {
    String packaging = project.getPackaging();
    if (!Type.JANGAROO_PKG_PACKAGING.equals(packaging) &&
            !Type.JANGAROO_SWC_PACKAGING.equals(packaging) &&
            !Type.JANGAROO_APP_PACKAGING.equals(packaging)) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"pkg\", \"swc\" or \"jangaroo-app\"");
    }
    boolean isAppPackaging = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging());
    if (isAppPackaging || testSuite != null) {
      File workspaceDir = new File(project.getBuild().getDirectory(), isAppPackaging ? APP_DIRECTORY_NAME : TEST_APP_DIRECTORY_NAME);
      if (!new File(workspaceDir, SenchaUtils.SENCHA_WORKSPACE_FILENAME).exists()) {
        generateWorkspace(workspaceDir);
      }
    }
  }

  @Inject
  private RepositorySystem repositorySystem;

  @Inject
  private ArtifactResolver artifactResolver;

  private void generateWorkspace(File workspaceDir) throws MojoExecutionException {
    getLog().info(String.format("Generating Sencha workspace in %s", workspaceDir.getPath()));
    FileHelper.ensureDirectory(workspaceDir);
    SenchaWorkspaceConfigBuilder configBuilder = new SenchaWorkspaceConfigBuilder();
    SenchaUtils.configureDefaults(configBuilder, "default.workspace.json");
    File remotePackagesDir = SenchaUtils.remotePackagesDir(session);
    boolean isAppPackaging = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging());
    configureSenchaAppTemplatePackage(configBuilder, workspaceDir, remotePackagesDir, !isAppPackaging);
    configurePackages(workspaceDir, remotePackagesDir, isAppPackaging);
    callSenchaGenerateWorkspace(workspaceDir, remotePackagesDir);
    writeFile(configBuilder, workspaceDir.getPath(), SenchaUtils.SENCHA_WORKSPACE_FILENAME, SenchaUtils.AUTO_CONTENT_COMMENT, getLog());
  }

  private void callSenchaGenerateWorkspace(File workspaceDir, File remotePackagesDir)
          throws MojoExecutionException {
    Path senchaCfg = Paths.get(workspaceDir.getAbsolutePath(), SenchaUtils.SENCHA_WORKSPACE_CONFIG);
    try {
      // delete existing senchaCfg so that Sencha Cmd is forced to re-create it
      Files.deleteIfExists(senchaCfg);
    } catch (IOException ioe) {
      throw new MojoExecutionException("Could not delete existing sencha.cfg file in " + senchaCfg, ioe);
    }

    getLog().info("Generating Sencha workspace module");
    List<String> arguments =  new ArrayList<>();
    arguments.add("generate workspace");

    SenchaUtils.addSwitchFullIfCmd6_5(arguments);

    arguments.add(".");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workspaceDir,
            StringUtils.join(arguments, ' '), getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    Map<String, Object> properties = new LinkedHashMap<>();
    properties.put("ext.dir", SenchaUtils.absolutizeToModuleWithPlaceholder(Type.WORKSPACE, EXT_TARGET_DIRECTORY));
    // needed for sencha app watch:
    // properties.put("build.web.root", absolutizeUsingWorkspace(workspaceDir, webRootDir));
    // properties.put("remotePackages", absolutizeUsingWorkspace(workspaceDir, remotePackagesDir));
    SenchaUtils.createSenchaCfg(senchaCfg, senchaCfg, properties);
  }

  private String absolutizeUsingWorkspace(File workspaceDir, File file) {
    String relativePath = file.isAbsolute()
            ? FileHelper.relativize(workspaceDir, file)
            : FilenameUtils.separatorsToUnix(file.toString());
    return SenchaUtils.absolutizeToModuleWithPlaceholder(Type.WORKSPACE, relativePath);
  }

  private void configurePackages(File workspaceDir, File remotePackagesDir, boolean isAppPackaging)
          throws MojoExecutionException {
    File packagesDir = new File(workspaceDir, SenchaUtils.PACKAGES_DIRECTORY_NAME);
    FileHelper.ensureDirectory(packagesDir);
    Path packagesPath = packagesDir.toPath().normalize();
    createSymbolicLinksForPackages(workspaceDir, packagesPath, remotePackagesDir, isAppPackaging);
  }

  private String reactorProjectId(MavenProject project) {
    return reactorProjectId(project.getGroupId(), project.getArtifactId(), project.getVersion());
  }

  private String reactorProjectId(String groupId, String artifactId, String version) {
    return groupId + ':' + artifactId + ':' + version;
  }

  private void createSymbolicLinksForPackages(File workspaceDir, Path packagesPath, File remotePackagesDir, boolean isAppPackaging) throws MojoExecutionException {
    Map<Artifact, Path> reactorProjectPackagePaths = findReactorProjectPackages(project);
    Set<Artifact> artifacts = project.getArtifacts();
    Optional<Artifact> extFrameworkArtifact = artifacts.stream().filter(this::isExtFrameworkArtifact).findFirst();
    if (extFrameworkArtifact.isPresent()) {
      createSymbolicLinkToPackage(workspaceDir.toPath(), "ext", getPkgDir(extFrameworkArtifact.get(), remotePackagesDir, reactorProjectPackagePaths));
    } else {
      getLog().warn("no Ext framework dependency found");
    }
    Set<Artifact> dependencyArtifacts = onlyRequiredSenchaDependencies(artifacts, !isAppPackaging);
    createSymbolicLinksForArtifacts(dependencyArtifacts, packagesPath, remotePackagesDir);
    if (!isAppPackaging) {
      // add link to package of module with the code to be tested:
      String senchaPackageName = SenchaUtils.getSenchaPackageName(project);
      File packageDir = new File(workspaceDir.getParentFile(), PACKAGES_DIRECTORY_NAME + SEPARATOR + senchaPackageName);
      FileHelper.ensureDirectory(packageDir); // make sure target folder exists, or symlink will look funny on Windows
      createSymbolicLinkToPackage(packagesPath, senchaPackageName, packageDir.toPath());
    }
  }

  private void configureSenchaAppTemplatePackage(SenchaWorkspaceConfigBuilder configBuilder, File workspaceDir, File remotePackagesDir, boolean isTestApp) throws MojoExecutionException {
    String myVersion = project.getPluginArtifactMap().get("net.jangaroo:jangaroo-maven-plugin").getVersion();
    ArtifactRepository localRepository = session.getLocalRepository();
    List<ArtifactRepository> remoteRepositories = project.getRemoteArtifactRepositories();

    String senchaAppTemplateArtifactId = isTestApp ? SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID : SENCHA_APP_TEMPLATE_ARTIFACT_ID;
    Artifact artifactFromHelper = MavenPluginHelper.getArtifact(localRepository, remoteRepositories, artifactResolver,
            repositorySystem, SENCHA_APP_TEMPLATE_GROUP_ID, senchaAppTemplateArtifactId, myVersion, "runtime", "pkg");
    if (artifactFromHelper == null) {
      throw new MojoExecutionException("Cannot find Sencha App template " + SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID + ":" + senchaAppTemplateArtifactId);
    }
    File appTemplate = unpackPkg(artifactFromHelper, remotePackagesDir);
    String relativeAppTemplatePath = absolutizeUsingWorkspace(workspaceDir, appTemplate);
    configBuilder.packagesDirs(Collections.singletonList(relativeAppTemplatePath));
  }

  private String relativizeToRemotePackagesPlaceholder(File remotePackagesDir, File pkgDir) {
    String relativePath = FileHelper.relativize(remotePackagesDir, pkgDir);
    return SenchaUtils.absolutizeWithPlaceholder("${remotePackages}", relativePath);
  }

  private String relativize(MavenProject project, @Nonnull String path) {
    return FileHelper.relativize(project.getBasedir().toPath(), path);
  }

  private File relativePathForProject(File workspaceDir, MavenProject project) throws MojoExecutionException {
    String localPathToSrc = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())
            ? SenchaUtils.APP_TARGET_DIRECTORY
            : SenchaUtils.LOCAL_PACKAGES_PATH + SenchaUtils.getSenchaPackageName(project);
    return relativePathForProject(workspaceDir, project, localPathToSrc);
  }

  private static File relativePathForProject(File workspaceDir, MavenProject project, String localPathToSrc) throws MojoExecutionException {
    Path rootPath = workspaceDir.toPath().normalize();
    Path path = Paths.get(project.getBuild().getDirectory() + localPathToSrc);
    Path relativePath = rootPath.relativize(path);
    String relativePathString = FilenameUtils.separatorsToUnix(relativePath.toString());
    if (relativePathString.isEmpty()) {
      throw new MojoExecutionException("Cannot handle project because not relative path to root workspace could be build");
    }
    return new File(relativePathString);
  }
}
