package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.apprunner.util.AppsDeSerializer;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.APPS_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_FILENAME;

/**
 * Generates and prepares packaging of Sencha apps modules.
 */
@Mojo(name = "prepare-package-apps",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PreparePackageAppsMojo extends AbstractLinkPackagesMojo {

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY, readonly = true)
  private File webResourcesOutputDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    if (!Type.JANGAROO_APPS_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-apps\"");
    }
    packageApps();
  }

  private void packageApps() throws MojoExecutionException {
    File appsDir = new File(webResourcesOutputDirectory, SenchaUtils.APPS_DIRECTORY_NAME);
    FileHelper.ensureDirectory(appsDir);
    Path appsPath = appsDir.toPath().normalize();

    JangarooApps jangarooApps = createJangarooApps(project);
    Dependency rootApp = getRootApp();
    for (JangarooApp jangarooApp : jangarooApps.apps) {
      // skip for root app
      if (rootApp != null
              && rootApp.getGroupId().equals(jangarooApp.mavenProject.getGroupId())
              && rootApp.getArtifactId().equals(jangarooApp.mavenProject.getArtifactId())) {
        continue;
      }
      String senchaAppName = SenchaUtils.getSenchaPackageName(jangarooApp.mavenProject);
      Path appPath = appsPath.resolve(senchaAppName);

      // TODO: check if in reactor
      // assuming no bootstrap file has been overridden in an app-overlay for now...
      File appReactorDir = new File(jangarooApp.getRootBaseApp().mavenProject.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY);
      File appJson = new File(appReactorDir, SENCHA_APP_FILENAME);

      final List<String> locales;
      try {
        locales = AppsDeSerializer.readLocales(new FileInputStream(appJson));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read " + appJson, e);
      }
      for (String locale : locales) {
        FileHelper.ensureDirectory(appPath.toFile());
        File bootstrapJsonSourceFile = new File(appReactorDir, locale + ".json");
        File bootstrapJsonTargetFile = new File(appPath.toFile(), locale + ".json");
        try {
          AppsDeSerializer.rewriteBootstrapJsonPaths(
                  new FileInputStream(bootstrapJsonSourceFile),
                  new FileOutputStream(bootstrapJsonTargetFile),
                  "packages/",
                  "../../packages/"
          );
        } catch (IOException e) {
          throw new MojoExecutionException("Could not rewrite bootstrap paths", e);
        }
      }
    }

    Set<String> appNames = jangarooApps.apps.stream()
            .map(app -> app.mavenProject.getArtifact())
            .map(artifact -> SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId()))
            .collect(Collectors.toSet());

    writeAppsJson(appNames);
  }

  private void writeAppsJson(Set<String> appNames) throws MojoExecutionException {
    File appsFile = prepareFile(new File(webResourcesOutputDirectory, APPS_FILENAME));

    try {
      AppsDeSerializer.writeApps(new FileOutputStream(appsFile), appNames);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + appsFile + " resource", e);
    }
  }
}
