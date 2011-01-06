package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Creates the jangaroo archive and attaches them to the project.<br>
 * The jangaroo archive is created by zipping the <code>packageSourceDirectory</code>
 * (defaults to target/jangaroo-output).
 * <p/>
 * The <code>package</code> goal is executed in the <code>package</code> phase of the jangaroo lifecycle.
 *
 * @goal package
 * @phase package
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration"})
public class PackageMojo extends AbstractMojo {
  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Destination directory for the Maven artifacts (*.jar). Default is <code>
   * ${project.build.directory}</code>
   *
   * @parameter expression="${project.build.directory}"
   */
  private File targetDir;

  /**
   * @component
   */
  MavenProjectHelper projectHelper;

  /**
   * List of files to exclude. Specified as fileset patterns.
   *
   * @parameter
   */
  private String[] excludes;

  /**
   * The name of the JAR file (without extension) generated as this module's artifact. Defaults to ${project.artifactId}
   *
   * @parameter default-value="${project.build.finalName}"
   * "
   */
  private String finalName;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="jar"
   * @required
   */
  private JarArchiver archiver;

  /**
   * @parameter
   */
  private File manifest;

  /**
   * Location of files to be packaged. Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  /**
   * This parameter specifies the name of the file containing the JavaScript code to execute when this
   * module is used.
   * If this file is not created through copying the corresponding resource, and the jsClassesFile exists,
   * a file containing the code to load the concatenated Jangaroo classes file is created.
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/joo/${project.artifactId}.js"
   */
  private File moduleJsFile;

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/joo/${project.groupId}.${project.artifactId}.classes.js"
   */
  private File jsClassesFile;

  public void execute()
      throws MojoExecutionException {
    File jsarchive = new File(targetDir, finalName + "." + Types.JAVASCRIPT_EXTENSION);
    try {
      if (manifest != null) {
        archiver.setManifest(manifest);
      } else {
        createDefaultManifest(project, archiver);
      }
      if (packageSourceDirectory.exists()) {
        archiver.addDirectory(packageSourceDirectory);
        if (!moduleJsFile.exists() && jsClassesFile.exists()) {
          createDefaultModuleJsFile();
        }
      }

      String groupId = project.getGroupId();
      String artifactId = project.getArtifactId();
      archiver.addFile(project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
          + "/pom.xml");
      archiver.setDestFile(jsarchive);
      archiver.createArchive();
      archiver.reset();
    }
    catch (Exception e) {
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }
    project.getArtifact().setFile(jsarchive);

  }

  private void createDefaultModuleJsFile() throws IOException {
    File moduleJsDir = moduleJsFile.getParentFile();
    if (moduleJsDir != null) {
      moduleJsDir.mkdirs();
    }
    getLog().info("Creating Jangaroo module classes loader script '" + moduleJsFile.getAbsolutePath() + "'.");
    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(moduleJsFile), "UTF-8");
    writer.write("joo.loadModule(\"" + project.getGroupId() + "\",\"" + project.getArtifactId() + "\");\n");
    writer.close();
  }

  private static void createDefaultManifest(MavenProject project, JarArchiver jarArchiver)
      throws ManifestException, IOException, ArchiverException {
    Manifest manifest = new Manifest();
    Manifest.Attribute attr = new Manifest.Attribute("Created-By", "Apache Maven");
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Title", project.getName());
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Version", project.getVersion());
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Vendor-Id", project.getGroupId());
    manifest.addConfiguredAttribute(attr);
    if (project.getOrganization() != null) {
      String vendor = project.getOrganization().getName();
      attr = new Manifest.Attribute("Implementation-Vendor", vendor);
      manifest.addConfiguredAttribute(attr);
    }
    attr = new Manifest.Attribute("Built-By", System.getProperty("user.name"));
    manifest.addConfiguredAttribute(attr);

    File mf = File.createTempFile("maven", ".mf");
    mf.deleteOnExit();
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new FileWriter(mf));
      manifest.write(writer);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    jarArchiver.setManifest(mf);
  }

}
