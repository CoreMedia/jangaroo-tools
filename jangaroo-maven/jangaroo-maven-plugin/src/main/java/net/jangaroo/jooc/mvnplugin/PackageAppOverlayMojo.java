package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isRequiredSenchaDependency;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app-overlay",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PackageAppOverlayMojo extends AbstractLinkPackagesMojo {

  private static final String OVERLAY_PACKAGES_DIRECTORY_NAME = "dynamic-packages";

  @Parameter(defaultValue = "${project.build.outputDirectory}/" + MavenPluginHelper.META_INF_RESOURCES)
  private File webResourcesOutputDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    if (!Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app-overlay\"");
    }
    packageAppOverlay();
  }

  private void packageAppOverlay() throws MojoExecutionException {
    MavenProject jangarooAppProject = project.getProjectReferences().values().stream().filter(mavenProject ->
            Type.JANGAROO_APP_PACKAGING.equals(mavenProject.getPackaging())).findFirst()
            .orElseThrow(() ->
                    new MojoExecutionException("Module of type jangaroo-app-overlay must have exactly one dependency on a module of type jangaroo.app.")
            );

    File overlayPackagesDir = new File(webResourcesOutputDirectory, OVERLAY_PACKAGES_DIRECTORY_NAME);
    FileHelper.ensureDirectory(overlayPackagesDir);
    Path packagesPath = overlayPackagesDir.toPath().normalize();
    File remotePackagesDir = SenchaUtils.remotePackagesDir(session);
    Map<Artifact, Path> reactorProjectPackagePaths = findReactorProjectPackages(project);

    Set<Artifact> applicationArtifacts = new HashSet<>();
    addSenchaDependencyArtifacts(jangarooAppProject, applicationArtifacts);
    Set<Artifact> overlayArtifacts = onlyRequiredSenchaDependencies(project.getArtifacts(), false);

    overlayArtifacts.removeIf(artifact -> containsSimilarArtifact(applicationArtifacts, artifact));

    createSymbolicLinksForArtifacts(overlayArtifacts, packagesPath, remotePackagesDir, reactorProjectPackagePaths);

    Set<String> overlayPackageNames = overlayArtifacts.stream().map(artifact ->
            SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId()))
            .collect(Collectors.toSet());
    writeDynamicPackagesJson(overlayPackageNames);
  }

  private boolean containsSimilarArtifact(Set<Artifact> artifacts, Artifact artifact) {
    return artifacts.stream().anyMatch(artifact1 ->
            artifact.getGroupId().equals(artifact1.getGroupId()) &&
            artifact.getArtifactId().equals(artifact1.getArtifactId()) &&
            artifact.getType().equals(artifact1.getType()));
  }

  private void addSenchaDependencyArtifacts(MavenProject project, Set<Artifact> artifacts) throws MojoExecutionException {
    if (artifacts.add(project.getArtifact())) {
      List<Dependency> dependencies = project.getDependencies();
      for (Dependency dependency : dependencies) {
        if (isRequiredSenchaDependency(dependency, false) || Type.POM_PACKAGING.equals(dependency.getType())) {
          MavenProject projectFromDependency = createProjectFromDependency(dependency);
          addSenchaDependencyArtifacts(projectFromDependency, artifacts);
        }
      }
    }
  }

  private void writeDynamicPackagesJson(Set<String> overlayPackageNames) throws MojoExecutionException {
    getLog().info(String.format("Write %s for module %s.", DYNAMIC_PACKAGES_FILENAME, project.getName()));
    File dynamicPackagesFile = new File(webResourcesOutputDirectory, DYNAMIC_PACKAGES_FILENAME);
    if (!dynamicPackagesFile.exists()) {
      FileHelper.ensureDirectory(dynamicPackagesFile.getParentFile());
    } else {
      getLog().debug(DYNAMIC_PACKAGES_FILENAME + " for module already exists, deleting...");
      if (!dynamicPackagesFile.delete()) {
        throw new MojoExecutionException("Could not delete " + DYNAMIC_PACKAGES_FILENAME + " file for module");
      }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(dynamicPackagesFile))) {
      pw.write(new JsonArray(overlayPackageNames.toArray()).toString(0, 2));
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + DYNAMIC_PACKAGES_FILENAME + " resource", e);
    }
  }

}
