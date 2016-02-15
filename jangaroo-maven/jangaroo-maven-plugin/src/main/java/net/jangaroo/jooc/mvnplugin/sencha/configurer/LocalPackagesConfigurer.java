package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.Types;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalPackagesConfigurer implements Configurer {

  static final String PACKAGES = "packages";
  static final String DIR = "dir";
  static final String EXTRACT = "extract";

  private MavenProject project;
  private SenchaConfiguration senchaConfiguration;

  public LocalPackagesConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration) {
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

    Set<Artifact> checkedArtifacts = new HashSet<Artifact>();
    Set<Artifact> localArtifacts = new HashSet<Artifact>();

    Set<MavenProject> mavenProjectsWithSenchaPackages = new HashSet<MavenProject>();

    @SuppressWarnings("unchecked") List<MavenProject> collectedProjects = (List<MavenProject>) project.getCollectedProjects();

    Plugin jangarooMavenPlugin = null;
    @SuppressWarnings("unchecked") List<Plugin> buildPlugins = (List<Plugin>) project.getBuildPlugins();
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

          /*
          checkedArtifacts.add(artifact);
          @SuppressWarnings("unchecked") List<Artifact> dependencies = (List<Artifact>) p.getDependencies();
          for (Artifact dependency : dependencies) {
            if (!localArtifacts.contains(dependency)
                    && !checkedArtifacts.contains(dependency)) {
              if (isSenchaPackageArtifact(dependency)) {
                // must be remote
                // TODO: extract pkg
              }
            }
          }
          */
        }
      }
    }

    Path rootPath;
    try {
      // toRealPath solves the problem that sometimes the root in the path is uppercase and sometimes lowercase,
      // causing relativize to fail
      rootPath = project.getFile().getParentFile().toPath().toRealPath();
    } catch (IOException e) {
      throw new MojoExecutionException("Could not determine root directory of the project", e);
    }
    for (MavenProject p : mavenProjectsWithSenchaPackages) {

      // TODO: check type by configuration not by name
      if (p.getArtifactId().endsWith("-webapp")) {
        // needs to be put into: apps: [], ignore for now path = Paths.get(p.getBuild().getDirectory() + File.separator + SENCHA_BASE_PATH);
      } else {
        Path path;
        // TODO: check type by configuration not by name
        if (p.getArtifactId().endsWith("-theme")) {
          path = Paths.get(p.getBuild().getDirectory() + File.separator + "..");
        } else {
          path = Paths.get(p.getBuild().getDirectory() + File.separator + SenchaUtils.SENCHA_BASE_PATH + File.separator + "packages");
        }
        Path relativePath = rootPath.relativize(path);
        String relativePathString = relativePath.toString();

        if (relativePathString.isEmpty()) {
          throw new MojoExecutionException("Cannot handle project because not relative path to root workspace could be build");
        }

        result.add("${workspace.dir}" + File.separator + relativePathString);
      }
    }

    return result;
  }

  private String absolutePath(String path) {
    return SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), path);
  }

}
