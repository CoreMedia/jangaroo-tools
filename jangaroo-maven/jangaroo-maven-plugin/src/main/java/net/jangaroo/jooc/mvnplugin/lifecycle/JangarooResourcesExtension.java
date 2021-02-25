package net.jangaroo.jooc.mvnplugin.lifecycle;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "JangarooResourcesExtension")
public class JangarooResourcesExtension extends AbstractMavenLifecycleParticipant {

  private static final String DEFAULT_SOURCE_DIRECTORY = "src" + File.separator + "main" + File.separator + "java";
  private static final String DEFAULT_TEST_SOURCE_DIRECTORY = "src" + File.separator + "test" + File.separator + "java";
  private static final String DEFAULT_JOO_SOURCE_DIR = "src/main/joo";
  private static final String DEFAULT_JOO_TEST_SOURCE_DIR = "src/test/joo";
  private static final String DEFAULT_SENCHA_RESOURCES_DIR = "src/main/sencha";
  private static final String DEFAULT_SENCHA_TEST_RESOURCES_DIR = "src/test/sencha";

  @Override
  public void afterProjectsRead(MavenSession session) {
    for (MavenProject project : session.getProjects()) {
      if (Type.containsJangarooSources(project)) {

        Resource resource = new Resource();
        resource.setDirectory(DEFAULT_SENCHA_RESOURCES_DIR);

        // ".." unfortunately the target path is relative to the build.outputDir and it
        // is not possible to define an absolute path.
        String outDir = project.getBuild().getOutputDirectory();
        String buildDir = project.getBuild().getDirectory();
        String targetPath;
        if (Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())) {
          targetPath = relativizePath(buildDir + SenchaUtils.APP_TARGET_DIRECTORY, outDir);
        } else {
          targetPath = relativizePath(buildDir + SenchaUtils.getPackagesPath(project), outDir);
        }
        resource.setTargetPath(targetPath);
        resource.setFiltering(false);
        resource.addExclude("sass/var/**");
        resource.addExclude("sass/src/**");
        project.addResource(resource);

        Resource testResource = new Resource();
        testResource.setDirectory(DEFAULT_SENCHA_TEST_RESOURCES_DIR);
        testResource.setFiltering(false);
        project.addTestResource(testResource);


        String sourceDirectory = project.getBuild().getSourceDirectory();
        // overwrite the sourceDirectory from the build configuration only if it is the default.
        // if it is explicitly in the project's POM then it obviously
        // happened on purpose
        if (isDefaultSourceDirectory(sourceDirectory)) {
          project.getBuild().setSourceDirectory(DEFAULT_JOO_SOURCE_DIR);
          project.addCompileSourceRoot(DEFAULT_JOO_SOURCE_DIR);
        }

        String testSourceDirectory = project.getBuild().getTestSourceDirectory();
        // overwrite the sourceDirectory from the build configuration only if it is different
        // from the default. if it is explicitly in the project's POM then it obviously
        // happened on purpose
        if (isDefaultTestSourceDirectory(testSourceDirectory)) {
          project.getBuild().setTestSourceDirectory(DEFAULT_JOO_TEST_SOURCE_DIR);
          project.addTestCompileSourceRoot(DEFAULT_JOO_TEST_SOURCE_DIR);
        }

      }
    }
  }

  private boolean isDefaultSourceDirectory(String sourceDirectory) {
    return sourceDirectory == null
            || sourceDirectory.endsWith(DEFAULT_SOURCE_DIRECTORY);
  }

  private boolean isDefaultTestSourceDirectory(String testSourceDirectory) {
    return testSourceDirectory == null
            || testSourceDirectory.endsWith(DEFAULT_TEST_SOURCE_DIRECTORY);
  }

  private String relativizePath(String target, String base) {
    Path basePath = Paths.get(base).normalize();
    Path targetPath = Paths.get(target).normalize();
    return basePath.relativize(targetPath).toString();
  }

}
