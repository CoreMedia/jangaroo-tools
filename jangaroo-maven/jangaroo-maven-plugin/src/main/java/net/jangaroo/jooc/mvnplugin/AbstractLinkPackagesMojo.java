package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isRequiredSenchaDependency;
import static org.apache.commons.io.FileUtils.cleanDirectory;

abstract class AbstractLinkPackagesMojo extends AbstractSenchaMojo {
  private static final Object lock = new Object();

  void createSymbolicLinkToPackage(Path packagesPath, String packageName, Path targetPath) throws MojoExecutionException {
    Path link = packagesPath.resolve(packageName);
    if (link.toFile().exists()) {
      return; // TODO: for now, assume that if something with that name already exists, it is correct.
    }
    Path target = packagesPath.relativize(targetPath);
    if (target.toString().equals(packageName)) {
      return; // Do not link a package to itself!
    }
    getLog().info("Linking " + link + " -> " + target);
    try {
      FileHelper.createSymbolicLink(link, target);
    } catch (IOException e) {
      throw new MojoExecutionException("Creating symbolic link for package " + packageName + " failed. Make sure you have sufficient rights to create symbolic links.", e);
    }
  }

  static Map<Artifact, Path> findReactorProjectPackages(MavenProject project) {
    Map<Artifact, Path> reactorProjectPackagePaths = new HashMap<>();
    Set<MavenProject> referencedProjects = new HashSet<>();
    collectReferencedProjects(project, referencedProjects);
    for (MavenProject projectInReactor : referencedProjects) {
      String packageType = projectInReactor.getPackaging();
      if (Type.JANGAROO_SWC_PACKAGING.equals(packageType) || Type.JANGAROO_PKG_PACKAGING.equals(packageType)) {
        String senchaPackageName = SenchaUtils.getSenchaPackageName(projectInReactor);
        reactorProjectPackagePaths.put(projectInReactor.getArtifact(), Paths.get(projectInReactor.getBuild().getDirectory() + SenchaUtils.LOCAL_PACKAGES_PATH + senchaPackageName));
      }
    }
    return reactorProjectPackagePaths;
  }

  private static void collectReferencedProjects(MavenProject project, Set<MavenProject> referencedProjects) {
    if (!referencedProjects.contains(project)) {
      referencedProjects.add(project);
      for (MavenProject referencedProject : project.getProjectReferences().values()) {
        collectReferencedProjects(referencedProject, referencedProjects);
      }
    }
  }

  void createSymbolicLinksForArtifacts(Set<Artifact> artifacts, Path packagesPath, File remotePackagesDir) throws MojoExecutionException {
    Map<Artifact, Path> reactorProjectPackagePaths = findReactorProjectPackages(project);
    for (Artifact artifact : artifacts) {
      String senchaPackageName = SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId());
      Path pkgDir = getPkgDir(artifact, remotePackagesDir, reactorProjectPackagePaths);
      createSymbolicLinkToPackage(packagesPath, senchaPackageName, pkgDir);
    }
  }

  Path getPkgDir(Artifact artifact, File remotePackagesDir, Map<Artifact, Path> reactorProjectPackagePaths) throws MojoExecutionException {
    Path pkgDir = reactorProjectPackagePaths.get(artifact);
    return pkgDir != null ? pkgDir : unpackPkg(artifact, remotePackagesDir).toPath();
  }

  /**
   * Unpack the artifact and add the extraction path to the list of packagePaths, return the extraction path.
   * If a pkg file is present as a sibling file of the jar file, it is preferred, otherwise the jar file is used.
   */
  File unpackPkg(Artifact artifact, File remotePackagesDir) throws MojoExecutionException {
    File jarFile = artifact.getFile();
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

  Set<Artifact> onlyRequiredSenchaDependencies(Set<Artifact> artifacts, boolean includeTestDependencies) {
    return artifacts.stream().filter(artifact ->
            !isExtFrameworkArtifact(artifact) &&
            isRequiredSenchaDependency(MavenDependencyHelper.fromArtifact(artifact), includeTestDependencies))
            .collect(Collectors.toSet());
  }
}
