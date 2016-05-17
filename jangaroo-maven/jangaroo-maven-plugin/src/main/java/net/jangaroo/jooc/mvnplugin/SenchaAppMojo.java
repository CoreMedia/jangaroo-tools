package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.Lists;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaAppConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaAppHelper;
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
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.util.List;

import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "package-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaAppMojo extends AbstractSenchaMojo implements SenchaAppConfiguration {

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

  @Parameter()
  private List<String> locales = Lists.newArrayList("en");

  /**
   * Choose to create a 'development' build of the Sencha App instead of the standard 'production' build.
   * Note that when you do a 'mvn install -DsenchaAppBuild=development', an incomplete artifact is installed!
   */
  @Parameter(defaultValue = "${senchaAppBuild}")
  private String senchaAppBuild;

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
  public List<String> getLocales() {
    return locales;
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
    if (StringUtils.isEmpty(senchaAppBuild)) {
      senchaAppBuild = SenchaAppHelper.PRODUCTION;
    }
    if (!(SenchaAppHelper.PRODUCTION.equals(senchaAppBuild) || SenchaAppHelper.DEVELOPMENT.equals(senchaAppBuild))) {
      throw new MojoExecutionException("'senchaAppBuild' must be one of 'production' or 'development'.");
    }

    SenchaAppHelper senchaHelper = new SenchaAppHelper(project, this, getLog(), senchaAppBuild);
    senchaHelper.prepareModule();
    File appProductionBuildDir = senchaHelper.packageModule();
    createJarFromProductionBuild(appProductionBuildDir);

  }

  private void createJarFromProductionBuild(File appProductionBuildDir) throws MojoExecutionException {

    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");

    // add the Jangaroo compiler resources to the resulting JAR
    archiver.addFileSet(fileSet( appProductionBuildDir ).prefixed( "META-INF/resources/" ));

    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    try {

      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
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
