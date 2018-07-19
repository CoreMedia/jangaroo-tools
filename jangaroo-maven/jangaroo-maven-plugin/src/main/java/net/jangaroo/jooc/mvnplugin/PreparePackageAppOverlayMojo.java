package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.DynamicPackagesDeSerializer;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.isRequiredSenchaDependency;
import static net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper.META_INF_RESOURCES;

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
    writeDynamicPackagesJson(jangarooAppDependency, overlayPackageNames);
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
        if (isRequiredSenchaDependency(transitiveDependency, false) || Type.POM_PACKAGING.equals(transitiveDependency.getType())
                || (Type.JAR_EXTENSION.equals(transitiveDependency.getType()) && Artifact.SCOPE_RUNTIME.equals(transitiveDependency.getScope()))) {
          addSenchaDependencyArtifacts(transitiveDependency, artifacts);
        }
      }
    }
  }

  private void writeDynamicPackagesJson(Dependency jangarooAppDependency, Set<String> overlayPackageNames) throws MojoExecutionException {
    getLog().info(String.format("Writing %s for module %s.", DYNAMIC_PACKAGES_FILENAME, project.getName()));
    File dynamicPackagesFile = new File(webResourcesOutputDirectory, DYNAMIC_PACKAGES_FILENAME);
    if (!dynamicPackagesFile.exists()) {
      FileHelper.ensureDirectory(dynamicPackagesFile.getParentFile());
    } else {
      getLog().debug(DYNAMIC_PACKAGES_FILENAME + " for module already exists, deleting...");
      if (!dynamicPackagesFile.delete()) {
        throw new MojoExecutionException("Could not delete " + DYNAMIC_PACKAGES_FILENAME + " file for module");
      }
    }

    try {
      Set<String> dynamicPackages;
      MavenProject jangarooAppProject = createProjectFromDependency(jangarooAppDependency);
      if (Type.JANGAROO_APP_OVERLAY_PACKAGING.equals(jangarooAppProject.getPackaging())) {
        // Attention: createProjectFromDependency() returns a project with a null artifact file :-(
        File overlayArtifactFile = getArtifact(jangarooAppDependency).getFile();
        if (overlayArtifactFile == null) {
          throw new MojoExecutionException("Overlay artifact " + jangarooAppDependency + " does not exist.");
        } else if (!overlayArtifactFile.exists()) {
          throw new MojoExecutionException("Overlay artifact " + overlayArtifactFile.getAbsolutePath() + " does not exist.");
        }
        // TODO: if overlay app is a "referenced project", we should use the dynamic-packages.json from its target directory
        JarFile overlayAppJarFile = new JarFile(overlayArtifactFile);
        dynamicPackages = new LinkedHashSet<>(DynamicPackagesDeSerializer.readDynamicPackages(overlayAppJarFile.getInputStream(overlayAppJarFile.getEntry(META_INF_RESOURCES + DYNAMIC_PACKAGES_FILENAME))));
        dynamicPackages.addAll(overlayPackageNames);
      } else {
        dynamicPackages = overlayPackageNames;
      }
      DynamicPackagesDeSerializer.writeDynamicPackages(new FileOutputStream(dynamicPackagesFile), dynamicPackages);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + DYNAMIC_PACKAGES_FILENAME + " resource", e);
    }
  }

}
