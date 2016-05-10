package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequiresConfigurer implements Configurer {

  static final String REQUIRES = "requires";

  private MavenProject project;
  private SenchaConfiguration senchaConfiguration;

  public RequiresConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(REQUIRES, getRequiredDependencies());
  }

  private Set<Map<String, Object>> getRequiredDependencies() throws MojoExecutionException {
    Set<Map<String, Object>> requiredDependencies = new LinkedHashSet<>();

    Dependency themeDependency = SenchaUtils.getThemeDependency(senchaConfiguration.getTheme(), project);

    List<Dependency> projectDependencies = project.getDependencies();

    Dependency remotePackageDependency = MavenDependencyHelper.fromKey(senchaConfiguration.getRemotePackagesArtifact());
    Dependency extFrameworkDependency = MavenDependencyHelper.fromKey(senchaConfiguration.getExtFrameworkArtifact());
    for (Dependency dependency : projectDependencies) {

      // TODO we should not assume that #getSenchaPackageNameForArtifact and #getSenchaPackageNameForTheme use the same string format
      String senchaPackageNameForArtifact = SenchaUtils.getSenchaPackageName(
              dependency.getGroupId(), dependency.getArtifactId()
      );

      if (SenchaUtils.isRequiredSenchaDependency(dependency, remotePackageDependency, extFrameworkDependency)
              && !MavenDependencyHelper.equalsGroupIdAndArtifactId(dependency,themeDependency)) {

        Map<String, Object> require = new HashMap<>();
        require.put("name", senchaPackageNameForArtifact);
        requiredDependencies.add(require);
      }

    }

    return requiredDependencies;
  }

}
