package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaWorkspaceConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.writeFile;
import static org.apache.commons.io.FileUtils.cleanDirectory;

/**
 * Generates and packages Sencha app module.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Mojo(name = "generate-ws",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class SenchaGenerateWsMojo extends AbstractSenchaMojo {

  /**
   * Non-null if we have joounit tests in this module
   */
  @Parameter
  private String testSuite = null;


  private static final Object lock = new Object();

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    String packaging = project.getPackaging();
    if (!Type.JANGAROO_PKG_PACKAGING.equals(packaging) &&
            !Type.JANGAROO_SWC_PACKAGING.equals(packaging) &&
            !Type.JANGAROO_APP_PACKAGING.equals(packaging)) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"pkg\", \"swc\" or \"jangaroo-app\"");
    }
    if (Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging()) || testSuite != null) {
      File workspaceDir = new File(project.getBuild().getDirectory());
      generateWorkspace(workspaceDir);
    }
  }

  @Inject
  protected RepositorySystem repositorySystem;

  @Inject
  private ArtifactResolver artifactResolver;

  private void generateWorkspace(File workspaceDir) throws MojoExecutionException {
    getLog().info(String.format("Generating Sencha workspace in %s", workspaceDir.getPath()));
    FileHelper.ensureDirectory(workspaceDir);
    SenchaWorkspaceConfigBuilder configBuilder = new SenchaWorkspaceConfigBuilder();
    SenchaUtils.configureDefaults(configBuilder, "default.workspace.json");
    File remotePackagesDir = SenchaUtils.remotePackagesDir(session);
    File extDirectory = configurePackages(project, workspaceDir, remotePackagesDir, configBuilder);
    callSenchaGenerateWorkspace(workspaceDir, extDirectory, webRootDir(), remotePackagesDir);
    writeFile(configBuilder, workspaceDir.getPath(), SenchaUtils.SENCHA_WORKSPACE_FILENAME, SenchaUtils.AUTO_CONTENT_COMMENT, getLog());
  }

  private void callSenchaGenerateWorkspace(File workspaceDir, File extDirectory, File webRootDir, File remotePackagesDir)
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
    // special case: Sencha Cmd 6.5 made a breaking change in "generate workspace", and to restore
    // the original behavior, we have to specify the new parameter "--full" (which Cmd < 6.5 prompts with an error).
    try {
      int[] cmdVersion = SenchaCmdExecutor.queryVersion();
      // version is 6.5 or above?
      if (cmdVersion[0] > 6 || cmdVersion[0] == 6 && cmdVersion[1] >= 5) {
        // add new command line switch:
        arguments.add("--full");
      }
    } catch (IOException ioe) {
      getLog().warn("Could not determine Sencha Cmd version, assuming it is < 6.5.", ioe);
    }
    arguments.add(".");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workspaceDir,
            StringUtils.join(arguments, ' '), getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();

    // sencha.cfg should be recreated
    Map<String, Object> properties = new LinkedHashMap<>();
    if (extDirectory != null) {
      properties.put("ext.dir", absolutizeUsingWorkspace(workspaceDir, extDirectory));
    }
    // needed for sencha app watch:
    properties.put("build.web.root", absolutizeUsingWorkspace(workspaceDir, webRootDir));
    properties.put("remotePackages", absolutizeUsingWorkspace(workspaceDir, remotePackagesDir));
    SenchaUtils.createSenchaCfg(senchaCfg, senchaCfg, properties);
  }

  private String absolutizeUsingWorkspace(File workspaceDir, File file) {
    String relativePath = file.isAbsolute()
            ? FileHelper.relativize(workspaceDir, file)
            : FilenameUtils.separatorsToUnix(file.toString());
    return SenchaUtils.absolutizeToModuleWithPlaceholder(Type.WORKSPACE, relativePath);
  }

  private File configurePackages(MavenProject project, File workspaceDir, File remotePackagesDir, SenchaWorkspaceConfigBuilder configBuilder)
          throws MojoExecutionException {
    List<String> packagePaths = new ArrayList<>();
    // first package path indicates the path other packages are generated from, this needs to be the workspace dir
    packagePaths.add(SenchaUtils.PLACEHOLDERS.get(Type.WORKSPACE));
    Set<String> reactorProjectPackagesIds = new HashSet<>();
    addReactorProjectPackages(project, workspaceDir, packagePaths, reactorProjectPackagesIds);
    File extDir = addRemotePackages(project, packagePaths, remotePackagesDir, reactorProjectPackagesIds);
    // sort resulting paths deterministically so that it remains the same no matter what OS you are using
    Collections.sort(packagePaths);
    configBuilder.packagesDirs(packagePaths);
    return extDir;
  }

  private void addReactorProjectPackages(MavenProject project, File workspaceDir, List<String> packagePaths, Set<String> reactorProjectPackagesIds) throws MojoExecutionException {
    Set<MavenProject> referencedProjects = new HashSet<>();
    collectReferencedProjects(project, referencedProjects);
    for (MavenProject projectInReactor : referencedProjects) {
      String packageType = projectInReactor.getPackaging();
      if (Type.JANGAROO_SWC_PACKAGING.equals(packageType) || Type.JANGAROO_PKG_PACKAGING.equals(packageType)) {
        packagePaths.add(absolutizeUsingWorkspace(workspaceDir, relativePathForProject(workspaceDir, projectInReactor)));
        reactorProjectPackagesIds.add(reactorProjectId(projectInReactor));
      }
    }
  }

  private String reactorProjectId(MavenProject project) {
    return reactorProjectId(project.getGroupId(), project.getArtifactId(), project.getVersion());
  }

  private String reactorProjectId(String groupId, String artifactId, String version) {
    return groupId + ':' + artifactId + ':' + version;
  }

  private void collectReferencedProjects(MavenProject project, Set<MavenProject> referencedProjects) {
    if (!referencedProjects.contains(project)) {
      referencedProjects.add(project);
      for (MavenProject referencedProject : project.getProjectReferences().values()) {
        collectReferencedProjects(referencedProject, referencedProjects);
      }
    }
  }

  private File addRemotePackages(MavenProject project, List<String> packagePaths, File remotePackagesDir, Set<String> reactorProjectPackagesIds) throws MojoExecutionException {
    File extDir = null;
    Set<Artifact> dependencyArtifacts = project.getArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      Dependency dependency = MavenDependencyHelper.fromArtifact(artifact);
      boolean isExtFramework = isExtFrameworkArtifact(artifact);
      if (isExtFramework || SenchaUtils.isRequiredSenchaDependency(dependency, true)) {
        String reactorProjectId = reactorProjectId(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
        boolean isReactorProject = reactorProjectPackagesIds.contains(reactorProjectId);
        if (isReactorProject) {
          getLog().info(String.format("%s contained in reactor, excluding from remote packages list", reactorProjectId));
        } else {
          File pkgDir = unpackPkg(artifact, remotePackagesDir);
          if (isExtFramework) {
            extDir = pkgDir;
          } else {
            packagePaths.add(relativizeToRemotePackagesPlaceholder(remotePackagesDir, pkgDir));
          }
        }
      }
    }
    if (extDir == null) {
      getLog().warn("no Ext framework dependency found");
    }
    String myVersion = project.getPluginArtifactMap().get("net.jangaroo:jangaroo-maven-plugin").getVersion();
    ArtifactRepository localRepository = session.getLocalRepository();
    List<ArtifactRepository> remoteRepositories = project.getRemoteArtifactRepositories();

    Artifact artifactFromHelper = MavenPluginHelper.getArtifact(localRepository, remoteRepositories, artifactResolver,
            repositorySystem, SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID, SenchaUtils.SENCHA_APP_TEMPLATE_ARTIFACT_ID, myVersion, "runtime", "pkg");
    File appTemplate = unpackPkg(artifactFromHelper, remotePackagesDir);
    packagePaths.add(relativizeToRemotePackagesPlaceholder(remotePackagesDir, appTemplate));

    Artifact testArtifactFromHelper = MavenPluginHelper.getArtifact(localRepository, remoteRepositories, artifactResolver,
            repositorySystem, SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID, SenchaUtils.SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID, myVersion, "runtime", "pkg");
    File testAppTemplate = unpackPkg(testArtifactFromHelper, remotePackagesDir);
    packagePaths.add(relativizeToRemotePackagesPlaceholder(remotePackagesDir, testAppTemplate));

    return extDir;
  }

  private String relativizeToRemotePackagesPlaceholder(File remotePackagesDir, File pkgDir) {
    String relativePath = FileHelper.relativize(remotePackagesDir, pkgDir);
    return SenchaUtils.absolutizeWithPlaceholder("${remotePackages}", relativePath);
  }

  private File webRootDir() {
    return  session.getRequest().getMultiModuleProjectDirectory();
  }

  /**
   * Unpack the artifact and add the extraction path to the list of packagePaths, return the extraction path.
   * If a pkg file is present as a sibling file of the jar file, it is preferred, otherwise the jar file is used.
   */
  private File unpackPkg(Artifact artifact, File remotePackagesDir) throws MojoExecutionException {
    File jarFile = artifact.getFile();
    String fileName = jarFile.getName();
    String targetDirName = String.format("%s__%s__%s", artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
    File targetDir = new File(remotePackagesDir, targetDirName);
    unpackPkg(jarFile.exists() ? jarFile : jarFile, artifact, targetDir);
    return targetDir;
  }

  private void unpackPkg(File pkgFile, Artifact artifact, File targetDir) throws MojoExecutionException {
    synchronized (lock) {
      String groupId = artifact.getGroupId();
      String artifactId = artifact.getArtifactId();
      String version = artifact.getVersion();
      File mavenStampFile = mavenStampFile(targetDir, groupId, artifactId, version);
      long artifactLastModified = pkgFile.lastModified();
      if (mavenStampFile.exists() && mavenStampFile.lastModified() == artifactLastModified) {
        // already unpacked
        getLog().info(String.format("Already unpacked %s to %s, skipping", artifact, targetDir.getName()));
        return;
      }
      if (targetDir.exists()) {
        getLog().info(String.format("Cleaning %s", targetDir));
        clean(targetDir);
      }
      getLog().info(String.format("Extracting %s to %s", artifact, targetDir));
      SenchaUtils.extractPkg(pkgFile, targetDir);
      touch(mavenStampFile, artifactLastModified);
    }
  }

  private void clean(File dir) throws MojoExecutionException {
    try {
      cleanDirectory(dir);
    } catch (IOException e) {
      throw new MojoExecutionException("unable to clean directory " + dir.getAbsolutePath(), e);
    }
  }

  private void touch(File file, long timestamp) throws MojoExecutionException {
    try {
      FileUtils.touch(file);
      if (!file.setLastModified(timestamp)) {
        throw new MojoExecutionException("unable to set last-modified on timestamp file " + file.getPath());
      }
    } catch (IOException e) {
      throw new MojoExecutionException("unable to create timestamp file " + file.getAbsolutePath(), e);
    }
  }

  private File mavenStampFile(File packageTargetDir, String groupId, String artifactId, String version) {
    String fileName = "." + groupId + '_' + artifactId + '_' + version + "-timestamp";
    return new File(packageTargetDir, fileName);
  }

  private String relativize(MavenProject project, @Nonnull String path)
          throws MojoExecutionException {
    return FileHelper.relativize(project.getBasedir().toPath(), path);
  }

  private File relativePathForProject(File workspaceDir, MavenProject project) throws MojoExecutionException {
    String localPathToSrc = Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging()) ?
            SenchaUtils.APP_TARGET_DIRECTORY : SenchaUtils.LOCAL_PACKAGES_PATH;
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
