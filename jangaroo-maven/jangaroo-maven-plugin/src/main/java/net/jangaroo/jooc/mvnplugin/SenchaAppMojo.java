package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaAppHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import javax.inject.Inject;
import java.io.File;

import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "sencha-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaAppMojo extends AbstractSenchaMojo {

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession mavenSession;

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private JarArchiver archiver;

  @Component
  private ArtifactHandlerManager artifactHandlerManager;

  @Override
  public String getType() {
    return Type.APP;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    SenchaHelper senchaHelper = new SenchaAppHelper(project, this, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
    File appProductionBuildDir = senchaHelper.packageModule();
    createJarFromProductionBuild(appProductionBuildDir);

  }

  private void createJarFromProductionBuild(File appProductionBuildDir) throws MojoExecutionException {
    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");
    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    try {

      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));

      JarArchiver archiver = mavenArchiver.getArchiver();
      archiver.addFileSet(fileSet( appProductionBuildDir ).prefixed( "META-INF/resources/" ));

      mavenArchiver.createArchive(mavenSession, project, archive);

    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }

    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(Type.JAR_EXTENSION));
  }

}
