package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaAppConfigBuilder;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Helper for Jangaroo test Mojo.
 */
public class SenchaTestAppHelper extends AbstractSenchaHelper<SenchaConfiguration, SenchaAppConfigBuilder> {

  public SenchaTestAppHelper(MavenProject project, Log log) {
    super(project, null, log);
  }

  @Override
  protected SenchaAppConfigBuilder createSenchaConfigBuilder() {
    return new SenchaAppConfigBuilder();
  }

  @Override
  protected void configure(SenchaAppConfigBuilder configBuilder) throws MojoExecutionException {
    MavenProject project = getProject();
    // require the package to test:
    configBuilder.require(getSenchaPackageName(project.getGroupId(), project.getArtifactId()));
    // add test scope dependencies:
    List<Dependency> projectDependencies = project.getDependencies();
    for (Dependency dependency : projectDependencies) {
      if (isTestDependency(dependency)) {
        String senchaPackageNameForArtifact = getSenchaPackageName(
                dependency.getGroupId(), dependency.getArtifactId()
        );
        configBuilder.require(senchaPackageNameForArtifact);
      }
    }
  }

  private boolean isTestDependency(Dependency dependency) {
    return Artifact.SCOPE_TEST.equals(dependency.getScope()) && Type.JAR_EXTENSION.equals(dependency.getType());
  }

  @Override
  protected String getDefaultsJsonFileName() {
    return "default.test.app.json";
  }

  public void createApp() throws MojoExecutionException {
    File appJsonFile = new File(getProject().getBuild().getTestOutputDirectory(), "app.json");
    getLog().info(String.format("Generating Sencha App %s for unit tests...", appJsonFile.getPath()));
    writeJson(appJsonFile, "Auto-generated test application configuration. DO NOT EDIT!");
  }
}
