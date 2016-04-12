package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaAppHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.commons.lang3.StringUtils;
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
@Mojo(name = "package-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaAppMojo extends AbstractSenchaMojo implements MavenSenchaAppConfiguration {

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession mavenSession;

  /**
   * The full qualified name of the application class of the Sencha app, e.g.:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApllication&lt;/applicationClass>
   * </pre>
   */
  @Parameter
  private String applicationClass;

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
  public String getApplicationClass() {
    return applicationClass;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app\"");
    }
    // parameter can not just be required="true" as this would also apply for the other packaging types and mojos
    if (StringUtils.isBlank(applicationClass)) {
      throw new MojoExecutionException("\"applicationClass\" is missing. This configuration is mandatory for \"jangaroo-app\" packaging.");
    }

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
