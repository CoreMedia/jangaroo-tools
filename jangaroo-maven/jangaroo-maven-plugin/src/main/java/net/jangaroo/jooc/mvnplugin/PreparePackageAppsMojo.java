package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableMap;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
      appInfoList.add(new AppInfo(senchaAppName, rootPath.relativize(appPath).toString().replace('\\', '/')));

      // skip remaining stuff for root app
      if (isRootApp) {
        continue;
      }

      File appDirOrJar = getAppDirOrJar(jangarooApp.mavenProject);

      // assuming no bootstrap file has been overridden in an app-overlay for now...
      final List<String> locales;
      try {
        locales = AppsDeSerializer.readLocales(getInputStreamForDirOrJar(appDirOrJar, SENCHA_APP_FILENAME));
      } catch (IOException e) {
        throw new MojoExecutionException("Could not read " + SENCHA_APP_FILENAME, e);
      }
      for (String locale : locales) {
        FileHelper.ensureDirectory(appPath.toFile());
        try {
          String pathToRoot = appPath.relativize(rootPath).toString().replace('\\', '/');
          if (!pathToRoot.isEmpty() && !pathToRoot.endsWith(SEPARATOR)) {
            pathToRoot += SEPARATOR;
          }
          AppsDeSerializer.rewriteBootstrapJsonPaths(
                  getInputStreamForDirOrJar(appDirOrJar, locale + ".json"),
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
      writeAppsJson(webResourcesOutputDirectory.toPath().resolve(appInfo.path).toFile(), appInfoList);
    }
  }

  private void writeAppsJson(File folder, List<AppInfo> appNames) throws MojoExecutionException {
    File appsFile = prepareFile(new File(folder, APPS_FILENAME));

    try {
      AppsDeSerializer.writeApps(new FileOutputStream(appsFile), appNames);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + appsFile + " resource", e);
    }
  }

  private InputStream getInputStreamForDirOrJar(File dirOrJar, String relativePathInsideDirOrJar) throws MojoExecutionException {
    if (dirOrJar.isDirectory()) {
      try {
        return new FileInputStream(dirOrJar.toPath().resolve(relativePathInsideDirOrJar).toFile());
      } catch (FileNotFoundException e) {
        return null;
      }
    } else {
      URL inputURL;
      try {
        String urlString = "jar:" + dirOrJar.toURI().toURL().toString() + "!/" + META_INF_RESOURCES + relativePathInsideDirOrJar;
        inputURL = new URL(urlString);
      } catch (MalformedURLException ignored) {
        // will not happen
        return null;
      }

      try {
        JarURLConnection urlConnection = (JarURLConnection) inputURL.openConnection();
        if (urlConnection.getJarEntry() == null) {
          return null;
        }
        return urlConnection.getInputStream();
      } catch (IOException e) {
        throw new MojoExecutionException("Error reading " + inputURL, e);
      }
    }
  }
}
