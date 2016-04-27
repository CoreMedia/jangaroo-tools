package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Creates the jangaroo archive and attaches them to the project.<br>
 * The jangaroo archive is created by zipping the <code>outputDirectory</code>
 * (defaults to target/classes).
 * <p/>
 * The <code>package</code> goal is executed in the <code>package</code> phase of the jangaroo lifecycle.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class PackageMojo extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /**
   * The Maven session
   */
  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  /**
   * Destination directory for the Maven artifacts (*.jar). Default is <code>
   * ${project.build.directory}</code>
   */
  @Parameter(defaultValue = "${project.build.directory}")
  private File targetDir;

  @Component
  private MavenProjectHelper projectHelper;

  /**
   * List of files to exclude. Specified as fileset patterns.
   */
  @Parameter
  private String[] excludes;

  /**
   * The name of the JAR file (without extension) generated as this module's artifact. Defaults to ${project.build.finalName}
   */
  @Parameter(defaultValue = "${project.build.finalName}")
  private String finalName;

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private JarArchiver archiver;


  /**
   * The archive configuration to use.
   * See <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven Archiver Reference</a>.
   */
  @Parameter
  private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

  /**
   * Path to the default MANIFEST file to use. It will be used if
   * <code>useDefaultManifestFile</code> is set to <code>true</code>.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}/META-INF/MANIFEST.MF", required = true, readonly = true)
  private File defaultManifestFile;

  /**
   * Set this to <code>true</code> to enable the use of the <code>defaultManifestFile</code>.
   */
  @Parameter(defaultValue = "${jar.useDefaultManifestFile}")
  private boolean useDefaultManifestFile;
  
  /**
   * Location of files to be packaged, which are all files placed in the other goal's outputDirectory.
   * Defaults to ${project.build.outputDirectory}
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  /**
   * This parameter specifies the path and name of the file containing the JavaScript code to execute when this
   * module is used, relative to the outputDirectory.
   * If this file is not created through copying the corresponding resource, and the jsClassesFile exists,
   * a file containing the code to load the concatenated Jangaroo classes file is created.
   */
  @Parameter(defaultValue = "META-INF/resources/joo/${project.artifactId}.module.js")
  private String moduleJsFile;

  /**
   * The Sencha configuration to use.
   */
  @Parameter(property = "senchaConfiguration")
  private MavenSenchaConfiguration senchaConfiguration;

  @Component
  private ArtifactHandlerManager artifactHandlerManager;

  public void execute()
      throws MojoExecutionException {
    File jarFile = new File(targetDir, finalName + ".jar");
    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    try {
      if (archive.getManifestFile() == null) {
        if (useDefaultManifestFile && defaultManifestFile.exists()) {
          getLog().info( "Adding existing MANIFEST to archive. Found under: " + defaultManifestFile.getPath() );
          archive.setManifestFile( defaultManifestFile );
        } else {
          archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
        }
      }
      JarArchiver archiver = mavenArchiver.getArchiver();
      if (outputDirectory.exists()) {
        archiver.addDirectory(outputDirectory);
      }

      String groupId = project.getGroupId();
      String artifactId = project.getArtifactId();
      archiver.addFile(project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
              + "/pom.xml");

      mavenArchiver.createArchive(mavenSession, project, archive);
    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }

    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(Type.JAR_EXTENSION));
  }

  private File getModuleJsFile() {
    return new File(outputDirectory, moduleJsFile);
  }

  private void createDefaultModuleJsFile() throws IOException {
    File jsFile = getModuleJsFile();
    File moduleJsDir = jsFile.getParentFile();
    if (moduleJsDir != null) {
      if (moduleJsDir.mkdirs()) {
        getLog().debug("created module output directory " + moduleJsDir);
      }
    }
    getLog().info("Creating Jangaroo module classes loader script '" + jsFile.getAbsolutePath() + "'.");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(jsFile), "UTF-8")) {
      writer.write("joo.loadModule(\"" + project.getGroupId() + "\",\"" + project.getArtifactId() + "\");\n");
    }
  }

}
