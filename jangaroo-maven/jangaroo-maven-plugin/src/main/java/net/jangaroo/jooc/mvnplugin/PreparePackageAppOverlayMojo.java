package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
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
import java.util.Set;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isRequiredSenchaDependency;

/**
 * Generates and prepares packaging of Sencha app modules.
 */
@Mojo(name = "prepare-package-app-overlay",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PreparePackageAppOverlayMojo extends AbstractLinkPackagesMojo {

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY, readonly = true)
  private File webResourcesOutputDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    if (!Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app-overlay\"");
    }
    packageAppOverlay();
  }

  private void packageAppOverlay() throws MojoExecutionException {
    Dependency jangarooAppDependency = findRequiredJangarooAppDependency(project);

    File overlayPackagesDir = new File(webResourcesOutputDirectory, SenchaUtils.PACKAGES_DIRECTORY_NAME);
    FileHelper.ensureDirectory(overlayPackagesDir);
    Path packagesPath = overlayPackagesDir.toPath().normalize();
    File remotePackagesDir = SenchaUtils.remotePackagesDir(session);

    Set<Artifact> applicationArtifacts = new HashSet<>();
    addSenchaDependencyArtifacts(jangarooAppDependency, applicationArtifacts);
    Set<Artifact> overlayArtifacts = onlyRequiredSenchaDependencies(project.getArtifacts(), false);

    overlayArtifacts.removeIf(artifact -> containsSimilarArtifact(applicationArtifacts, artifact));

    createSymbolicLinksForArtifacts(overlayArtifacts, packagesPath, remotePackagesDir);

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

  private void addSenchaDependencyArtifacts(Dependency dependency, Set<Artifact> artifacts) throws MojoExecutionException {
    MavenProject projectFromDependency = createProjectFromDependency(dependency);
    if (artifacts.add(projectFromDependency.getArtifact())) {
      List<Dependency> dependencies = projectFromDependency.getDependencies();
      for (Dependency transitiveDependency : dependencies) {
        if (isRequiredSenchaDependency(transitiveDependency, false) || Type.POM_PACKAGING.equals(transitiveDependency.getType())) {
          addSenchaDependencyArtifacts(transitiveDependency, artifacts);
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
