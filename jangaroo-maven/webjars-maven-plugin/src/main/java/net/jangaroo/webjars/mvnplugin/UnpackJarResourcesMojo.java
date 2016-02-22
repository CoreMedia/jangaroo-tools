package net.jangaroo.webjars.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * A goal to be used in a "war" build to unpack resources contained in dependent JARs in their
 * META-INF/resources subdirectory.
 * This is useful when building web apps that are not supposed to be run in a Java Application
 * Server, but in a static web server like Apache or Lighttpd.
 */
@SuppressWarnings("UnusedDeclaration")
@Mojo(name = "unpack",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class UnpackJarResourcesMojo extends AbstractMojo {

  public static final String META_INF_RESOURCES = "META-INF/resources/";

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  /**
   * Plexus archiver.
   */
  @SuppressWarnings("UnusedDeclaration")
  @Component(role = Archiver.class, hint = "jar3")
  private UnArchiver unarchiver;

  @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}")
  private File webappDirectory;
  
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      unpack(webappDirectory);
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    }
  }

  public void unpack(File target)
      throws ArchiverException {

    unarchiver.setOverwrite(false);
    unarchiver.setFileSelectors(new FileSelector[]{new MetaInfResourcesFileSelector()});
    Set<Artifact> dependencies = getArtifacts();

    for (Artifact dependency : dependencies) {
      getLog().debug("Dependency: " + dependency);
      if (!dependency.isOptional() && "jar".equals(dependency.getType())) {
        unpack(unarchiver, dependency, target);
      }
    }
  }

  public void unpack(UnArchiver unarchiver, Artifact artifact, File target)
      throws ArchiverException {
    getLog().debug("Unpacking dependent JAR [" + artifact.getFile().getAbsolutePath() + "]'s META-INF/resources/... to " + target.getAbsolutePath());
    unarchiver.setSourceFile(artifact.getFile());
    if (target.mkdirs()) {
      getLog().debug("created unarchiver target directory " + target);
    }
    unarchiver.setDestDirectory(target);
    unarchiver.setOverwrite(true);
    try {
      unarchiver.extract();
    } catch (Exception e) {
      throw new ArchiverException("Failed to extract WebJar artifact to " + target, e);
    }
  }

  @SuppressWarnings({ "unchecked" })
  protected Set<Artifact> getArtifacts() {
    return project.getArtifacts();
  }

  private static class MetaInfResourcesFileSelector implements FileSelector {
    @Override
    public boolean isSelected(@Nonnull FileInfo fileInfo) throws IOException {
      return fileInfo.getName().startsWith(META_INF_RESOURCES) && !fileInfo.isDirectory();
    }
  }
}
