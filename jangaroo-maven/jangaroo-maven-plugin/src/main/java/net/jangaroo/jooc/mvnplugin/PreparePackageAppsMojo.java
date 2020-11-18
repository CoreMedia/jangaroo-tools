package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableMap;
import net.jangaroo.apprunner.util.AppDeSerializer;
import net.jangaroo.apprunner.util.AppsDeSerializer;
import net.jangaroo.apprunner.util.AppsDeSerializer.AppInfo;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.APPS_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.EXT_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGES_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_FILENAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SEPARATOR;
import static net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper.META_INF_RESOURCES;

/**
 * Generates and prepares packaging of Sencha apps modules.
 */
@Mojo(name = "prepare-package-apps",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PreparePackageAppsMojo extends AbstractLinkPackagesMojo {

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APPS_TARGET_DIRECTORY, readonly = true)
  private File webResourcesOutputDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    if (!Type.JANGAROO_APPS_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-apps\"");
    }
    packageApps();
  }

  private void packageApps() throws MojoExecutionException {
    Path rootPath = webResourcesOutputDirectory.toPath().normalize();
    Path appsPath = rootPath.resolve(SenchaUtils.APPS_DIRECTORY_NAME).normalize();
    FileHelper.ensureDirectory(appsPath.toFile());

    JangarooApps jangarooApps = createJangarooApps(project);
    Dependency rootApp = getRootApp();
    List<AppInfo> appInfoList = new ArrayList<>();
    for (JangarooApp jangarooApp : jangarooApps.apps) {
      String senchaAppName = SenchaUtils.getSenchaPackageName(jangarooApp.mavenProject);
      boolean isRootApp = rootApp != null
              && rootApp.getGroupId().equals(jangarooApp.mavenProject.getGroupId())
              && rootApp.getArtifactId().equals(jangarooApp.mavenProject.getArtifactId());
      Path appPath = isRootApp ? rootPath : appsPath.resolve(senchaAppName);

      // assuming no bootstrap file has been overridden in an app-overlay for now...
      final List<String> locales;
      File appDirOrJar = getAppDirOrJar(jangarooApp.getRootBaseApp().mavenProject);
      try {
        locales = AppDeSerializer.readLocales(getInputStreamForDirOrJar(appDirOrJar, SENCHA_APP_FILENAME, META_INF_RESOURCES));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read " + SENCHA_APP_FILENAME, e);
      }

      appInfoList.add(
              new AppInfo(
                      senchaAppName,
                      appPath,
                      locales
              )
      );

      // skip remaining stuff for root app
      if (isRootApp) {
        continue;
      }

      for (String locale : locales) {
        FileHelper.ensureDirectory(appPath.toFile());
        try {
          String pathToRoot = appPath.relativize(rootPath).toString().replace('\\', '/');
          if (!pathToRoot.isEmpty() && !pathToRoot.endsWith(SEPARATOR)) {
            pathToRoot += SEPARATOR;
          }
          AppsDeSerializer.rewriteBootstrapJsonPaths(
                  getInputStreamForDirOrJar(appDirOrJar, locale + ".json", META_INF_RESOURCES),
                  new FileOutputStream(new File(appPath.toFile(), locale + ".json")),
                  ImmutableMap.of(
                          EXT_DIRECTORY_NAME + SEPARATOR, pathToRoot + EXT_DIRECTORY_NAME + SEPARATOR,
                          PACKAGES_DIRECTORY_NAME + SEPARATOR, pathToRoot + PACKAGES_DIRECTORY_NAME + SEPARATOR
                  )
          );
        } catch (IOException e) {
          throw new MojoExecutionException("Could not rewrite bootstrap paths", e);
        }
      }
    }

    for (AppInfo appInfo : appInfoList) {
      writeAppsJson(webResourcesOutputDirectory.toPath().resolve(appInfo.path).toFile(), appInfo.path, appInfoList);
    }
  }

  private void writeAppsJson(File folder, Path rootPath, List<AppInfo> apps) throws MojoExecutionException {
    File appsFile = prepareFile(new File(folder, APPS_FILENAME));

    try {
      AppsDeSerializer.writeApps(new FileOutputStream(appsFile), rootPath, apps);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + appsFile + " resource", e);
    }
  }

}
