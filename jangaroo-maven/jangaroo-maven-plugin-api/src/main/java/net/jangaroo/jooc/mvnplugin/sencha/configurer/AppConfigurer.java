package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppConfigurer implements Configurer {

  public static final String APP = "apps";

  private MavenProject project;

  public AppConfigurer(MavenProject project) {
    this.project = project;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(APP, getLocalPathsToApps());
  }

  private List<String> getLocalPathsToApps() throws MojoExecutionException {
    List<String> result = new ArrayList<>();

    Set<MavenProject> senchaApps = new HashSet<>();

    List<MavenProject> projectsInReactor = project.getCollectedProjects();

    if (null != projectsInReactor) {

      // filter apps from all projects
      for (MavenProject project : projectsInReactor) {
        String packaging = project.getPackaging();
        if (Type.JANGAROO_APP_PACKAGING.equals(packaging)) {
          senchaApps.add(project);
        }
      }

    }

    if (!senchaApps.isEmpty()) {
      Path rootPath = project.getBasedir().toPath().normalize();

      for (MavenProject senchaApp : senchaApps) {

        Path path = Paths.get(senchaApp.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY);
        Path relativePath = rootPath.relativize(path);
        String relativePathString = relativePath.toString();

        if (relativePathString.isEmpty()) {
          throw new MojoExecutionException("Cannot handle project because not relative path to root workspace could be build");
        }

        result.add(relativePathString);

      }

      // sort resulting paths deterministically so that it remains the same no matter what OS you are using
      Collections.sort(result);
    }

    return Collections.unmodifiableList(result);
  }

}
