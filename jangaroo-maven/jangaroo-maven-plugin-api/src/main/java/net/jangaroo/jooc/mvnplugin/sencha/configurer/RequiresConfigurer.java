package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.MavenSenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.MavenDependency;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequiresConfigurer implements Configurer {

  static final String REQUIRES = "requires";

  private MavenProject project;
  private MavenSenchaConfiguration senchaConfiguration;

  public RequiresConfigurer(MavenProject project, MavenSenchaConfiguration senchaConfiguration) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(REQUIRES, getRequiredDependencies());
  }

  private Set<Map<String, Object>> getRequiredDependencies() throws MojoExecutionException {
    Set<Map<String, Object>> requiredDependencies = new HashSet<Map<String, Object>>();

    MavenDependency themeDependency = SenchaUtils.getThemeDependency(senchaConfiguration.getTheme(), project);

    List<Dependency> projectDependencies = project.getDependencies();

    MavenDependency remotePackageDependency = MavenDependency.fromKey(senchaConfiguration.getRemotePackagesArtifact());
    MavenDependency extFrameworkDependency = MavenDependency.fromKey(senchaConfiguration.getExtFrameworkArtifact());
    for (Dependency dependency : projectDependencies) {

      // TODO we should not assume that #getSenchaPackageNameForArtifact and #getSenchaPackageNameForTheme use the same string format
      String senchaPackageNameForArtifact = SenchaUtils.getSenchaPackageName(
              dependency.getGroupId(), dependency.getArtifactId()
      );

      MavenDependency projectDependency = MavenDependency.fromDependency(dependency);
      if (SenchaUtils.isRequiredSenchaDependency(projectDependency, remotePackageDependency, extFrameworkDependency)
              && !projectDependency.equalsGroupIdAndArtifactId(themeDependency)) {
        String version = SenchaUtils.getSenchaVersionForMavenVersion(dependency.getVersion());
        if (null == version) {
          throw new MojoExecutionException("Could not determine Sencha version from maven version of artifact "
                  + dependency.getManagementKey());
        }
        Map<String, Object> require = new HashMap<String, Object>();
        require.put("name", senchaPackageNameForArtifact);
        require.put("version", version);
        requiredDependencies.add(require);
      }

    }

    return requiredDependencies;
  }

}
