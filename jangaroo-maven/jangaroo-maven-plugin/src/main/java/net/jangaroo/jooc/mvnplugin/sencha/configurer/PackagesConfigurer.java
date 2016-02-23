package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.Types;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackagesConfigurer implements Configurer {

  public static final String PACKAGES = "packages";
  public static final String DIR = "dir";
  public static final String EXTRACT = "extract";

  private MavenProject project;
  private SenchaConfiguration senchaConfiguration;

  public PackagesConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    Map<String, Object> packages = new LinkedHashMap<String, Object>();

    packages.put(DIR, getLocalPackagePathsJsonAndExtractRemotePackages());
    packages.put(EXTRACT, absolutePath(senchaConfiguration.getPackagesDir() + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_PACKAGES_REMOTE));

    config.put(PACKAGES, packages);
  }

  private List<String> getLocalPackagePathsJsonAndExtractRemotePackages() throws MojoExecutionException {
    List<String> result = new ArrayList<String>();

    // first package path indicates the path other packages are generated from, this needs to be the workspace dir
    result.add(absolutePath(""));

    Set<MavenProject> mavenProjectsWithSenchaPackages = new HashSet<MavenProject>();

    List<MavenProject> collectedProjects = project.getCollectedProjects();

    Plugin jangarooMavenPlugin = null;
    List<Plugin> buildPlugins = project.getBuildPlugins();
    for (Plugin plugin : buildPlugins) {
      // TODO: replace with injected ids
      if ("net.jangaroo".equals(plugin.getGroupId())
              && "jangaroo-maven-plugin".equals(plugin.getArtifactId())) {
        jangarooMavenPlugin = plugin;
        break;
      }
    }

    if (null == jangarooMavenPlugin) {
      throw new MojoExecutionException("could not find myself");
    }

    if (null != collectedProjects) {

      for (MavenProject p : collectedProjects) {
        if (Types.JANGAROO_TYPE.equals(p.getPackaging())
                && p.getBuildPlugins().contains(jangarooMavenPlugin)) {
          mavenProjectsWithSenchaPackages.add(p);
        }
      }
    }

    Path rootPath;
    try {
      // toRealPath solves the problem that sometimes the root in the path is uppercase and sometimes lowercase,
      // causing relativize to fail
      rootPath = project.getBasedir().toPath().toRealPath();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not determine root directory of the project", e);
    }
    for (MavenProject p : mavenProjectsWithSenchaPackages) {

      // TODO: check type by configuration not by name
      if (p.getArtifactId().endsWith("-webapp")) {
        // needs to be put into: apps: [], ignore for now path = Paths.get(p.getBuild().getDirectory() + "/" + SENCHA_BASE_PATH);
      } else {
        Path path;
        path = Paths.get(p.getBuild().getDirectory() + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_BASE_PATH + SenchaUtils.SEPARATOR + "packages" + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_PACKAGES_LOCAL);
        Path relativePath = rootPath.relativize(path);
        String relativePathString = relativePath.toString();

        if (relativePathString.isEmpty()) {
          throw new MojoExecutionException("Cannot handle project because not relative path to root workspace could be build");
        }

        result.add("${workspace.dir}" + SenchaUtils.SEPARATOR + relativePathString);
      }
    }

    return result;
  }

  private String absolutePath(String path) {
    return SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), path);
  }

}
