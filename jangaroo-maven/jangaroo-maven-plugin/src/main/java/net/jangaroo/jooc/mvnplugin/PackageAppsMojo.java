package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Packages Sencha app overlay module.
 */
@Mojo(name = "package-apps",
        defaultPhase = LifecyclePhase.PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class PackageAppsMojo extends AbstractSenchaMojo {

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private JarArchiver archiver;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    JangarooApps jangarooApps = createJangarooApps(project);
    Map<String, List<File>> appNamesToDirsOrJars = new HashMap<>();
    for (JangarooApp jangarooApp : jangarooApps.apps) {
      String senchaAppName = SenchaUtils.getSenchaPackageName(jangarooApp.mavenProject);
      List<File> appReactorDirs = new ArrayList<>();

      do {
        appReactorDirs.add(getAppDirOrJar(jangarooApp.mavenProject));
        jangarooApp = jangarooApp instanceof JangarooAppOverlay ? ((JangarooAppOverlay) jangarooApp).baseApp : null;
      } while (jangarooApp != null);

      appNamesToDirsOrJars.put(senchaAppName, appReactorDirs);
    }
    Dependency rootApp = getRootApp();
    String rootAppName = rootApp == null ? null : SenchaUtils.getSenchaPackageName(rootApp.getGroupId(), rootApp.getArtifactId());
    FileHelper.createAppsJar(session, archiver, getManifestEntries(), artifactHandlerManager, null, null, appNamesToDirsOrJars, rootAppName);
  }
}
