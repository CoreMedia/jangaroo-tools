package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.MavenSenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    packages.put(EXTRACT, absolutePath(senchaConfiguration.getPackagesDir()));

    config.put(PACKAGES, packages);
  }

  private List<String> getLocalPackagePathsJsonAndExtractRemotePackages() throws MojoExecutionException {
    List<String> result = new ArrayList<String>();

    // first package path indicates the path other packages are generated from, this needs to be the workspace dir
    result.add(absolutePath(""));

    Set<MavenProject> mavenProjectsWithSenchaPackages = new HashSet<MavenProject>();

    List<MavenProject> collectedProjects = project.getCollectedProjects();

    if (null != collectedProjects) {

      for (MavenProject p : collectedProjects) {
        if (MavenSenchaConfiguration.isSenchaPackaging(p.getPackaging())) {
          mavenProjectsWithSenchaPackages.add(p);
        }
      }

    }

    Path rootPath = project.getBasedir().toPath().normalize();

    for (MavenProject mavenProjectWithSenchaPackage : mavenProjectsWithSenchaPackages) {

      Path path;
      path = Paths.get(mavenProjectWithSenchaPackage.getBuild().getDirectory() + SenchaUtils.LOCAL_PACKAGES_PATH);
      Path relativePath = rootPath.relativize(path);
      String relativePathString = relativePath.toString();

      if (relativePathString.isEmpty()) {
        throw new MojoExecutionException("Cannot handle project because not relative path to root workspace could be build");
      }

      result.add("${workspace.dir}" + SenchaUtils.SEPARATOR + relativePathString);

    }

    // sort resulting paths deterministically so that it remains the same no matter what OS you are using
    Collections.sort(result);

    return Collections.unmodifiableList(result);
  }

  private String absolutePath(String path) {
    return SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), path);
  }

}
