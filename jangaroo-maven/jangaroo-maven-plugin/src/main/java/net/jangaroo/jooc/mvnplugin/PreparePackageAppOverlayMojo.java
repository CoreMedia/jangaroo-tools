package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.apprunner.util.AppDeSerializer;
import net.jangaroo.apprunner.util.AppManifestDeSerializer;
import net.jangaroo.apprunner.util.DynamicPackagesDeSerializer;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.DYNAMIC_PACKAGES_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_FILENAME;
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
    File overlayPackagesDir = new File(webResourcesOutputDirectory, SenchaUtils.PACKAGES_DIRECTORY_NAME);
    FileHelper.ensureDirectory(overlayPackagesDir);
    Path packagesPath = overlayPackagesDir.toPath().normalize();
    File remotePackagesDir = SenchaUtils.remotePackagesDir(session);

    JangarooAppOverlay jangarooAppOverlay = createJangarooAppOverlay(project);
    populatePackages(jangarooAppOverlay, project);

    Set<Artifact> ownDynamicPackages = jangarooAppOverlay.getOwnDynamicPackages();
    createSymbolicLinksForArtifacts(ownDynamicPackages, packagesPath, remotePackagesDir);

    Set<Artifact> allDynamicPackages = jangarooAppOverlay.getAllDynamicPackages();
    Set<String> overlayPackageNames = allDynamicPackages.stream().map(artifact ->
            SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId()))
            .collect(Collectors.toSet());
    writeDynamicPackagesJson(overlayPackageNames);

    JangarooApp rootBaseApp = jangarooAppOverlay.getRootBaseApp();
    if (rootBaseApp != null) {
      InputStream appJson = getInputStreamForDirOrJar(getArtifactFile(rootBaseApp.mavenProject), SENCHA_APP_FILENAME, META_INF_RESOURCES);
      final Set<String> locales;
      try {
        locales = new LinkedHashSet<>(AppDeSerializer.readLocales(appJson));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read locales", e);
      }

      Set<Artifact> artifacts = new HashSet<>();
      JangarooApp current = jangarooAppOverlay;
      while (current != null) {
        artifacts.addAll(current.packages);
        if (current instanceof JangarooAppOverlay) {
          current = ((JangarooAppOverlay) current).baseApp;
        } else {
          current = null;
        }
      }
      artifacts.add(getArtifact(rootBaseApp.mavenProject));
      writeAppManifestJsonByLocale(prepareAppManifestByLocale(locales, new ArrayList<>(artifacts)));
    }
  }

  private void populatePackages(JangarooApp jangarooApp, MavenProject project) throws MojoExecutionException {
    List<Dependency> dependencies = project.getDependencies();
    JangarooAppOverlay jangarooAppOverlay = jangarooApp instanceof JangarooAppOverlay ? (JangarooAppOverlay) jangarooApp : null;
    for (Dependency dependency : dependencies) {
      if (isRequiredSenchaDependency(dependency, false) || Type.POM_PACKAGING.equals(dependency.getType())
              || (Type.JAR_EXTENSION.equals(dependency.getType()) && Artifact.SCOPE_RUNTIME.equals(dependency.getScope()))) {
        MavenProject mavenProject = getProjectFromDependency(project, dependency);
        if (jangarooAppOverlay != null && jangarooAppOverlay.baseApp != null
                && jangarooAppOverlay.baseApp.mavenProject.equals(mavenProject)) {
          populatePackages(jangarooAppOverlay.baseApp, mavenProject);
        } else {
          if (!SenchaUtils.isSenchaDependency(dependency.getType()) || jangarooApp.packages.add(getArtifact(dependency))) {
            populatePackages(jangarooApp, mavenProject);
          }
        }
      }
    }
  }

  private void writeDynamicPackagesJson(Set<String> dynamicPackageNames) throws MojoExecutionException {
    File dynamicPackagesFile = prepareFile(new File(webResourcesOutputDirectory, DYNAMIC_PACKAGES_FILENAME));

    try {
      DynamicPackagesDeSerializer.writeDynamicPackages(new FileOutputStream(dynamicPackagesFile), dynamicPackageNames);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + dynamicPackagesFile + " resource", e);
    }
  }

  private void writeAppManifestJsonByLocale(Map<String, Map<String, Object>> appManifestByLocale) throws MojoExecutionException {
    for (String locale : appManifestByLocale.keySet()) {
      String appManifestFileName = getAppManifestFileNameForLocale(locale);
      File appManifestFile = prepareFile(new File(webResourcesOutputDirectory, appManifestFileName));

      try {
        AppManifestDeSerializer.writeAppManifest(new FileOutputStream(appManifestFile), appManifestByLocale.get(locale));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not create " + appManifestFile + " resource", e);
      }
    }
  }

}
