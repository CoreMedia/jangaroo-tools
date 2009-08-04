package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.javascript.archive.JavascriptArchiver;
import org.codehaus.mojo.javascript.archive.Types;

import java.io.File;

/**
 * Packages the javascript stuff.
 *
 * @goal package
 * @phase package
 */
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
   * @component
   */
  MavenProjectHelper projectHelper;

  /**
   * The output directory of the js file.
   *
   * @parameter expression="${project.build.directory}"
   */
  private File outputDirectory;


  /**
   * Source directory to scan for files to package in the sources archive. These files
   * have been compiled.
   *
   * @parameter expression="${basedir}/src/main/joo"
   */
  private File sourceDirectory;

  /**
   * Source directory to scan for files to package in the sources archive. These files
   * have not been compiled since these classes are available by default. They are needed
   * to make them available via IDE.
   *
   * @parameter expression="${basedir}/src/main/ide-resources"
   */
  private File ideResourceDirectory;

  /**
   * Source directory containing pregenrated javascript that will be just included.
   *
   * @parameter expression="${basedir}/src/main/pregenerated"
   */
  private File pregeneratedDirectory;

  /**
   * The filename of the js file.
   *
   * @parameter default-value="${project.build.finalName}"
   */
  private String finalName;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="javascript"
   * @required
   */
  private JavascriptArchiver archiver;

  /**
   * @parameter
   */
  private File manifest;

  /**
   * Location of the scripts files.
   *
   * @parameter expression="${project.build.directory}/joo/"
   */
  private File scriptsDirectory;

  public void execute()
          throws MojoExecutionException {
    File jsarchive = new File(outputDirectory, finalName + "." + Types.JAVASCRIPT_EXTENSION);
    try {
      if (manifest != null) {
        archiver.setManifest(manifest);
      } else {
        archiver.createDefaultManifest(project);
      }
      if (scriptsDirectory.exists()) {
        archiver.addDirectory(scriptsDirectory);
      }
      if (pregeneratedDirectory.exists()) {
        archiver.addDirectory(pregeneratedDirectory); 
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

    File asarchive = new File(outputDirectory, finalName + "-sources." + Types.JAVASCRIPT_EXTENSION);
    try {
      if (manifest != null) {
        archiver.setManifest(manifest);
      } else {
        archiver.createDefaultManifest(project);
      }
      if (sourceDirectory.exists()) {
        archiver.addDirectory(sourceDirectory);
      }
      if (ideResourceDirectory.exists()) {
        archiver.addDirectory(ideResourceDirectory);
      }
      String groupId = project.getGroupId();
      String artifactId = project.getArtifactId();
      archiver.addFile(project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
              + "/pom.xml");
      archiver.setDestFile(asarchive);
      archiver.createArchive();
    }
    catch (Exception e) {
      throw new MojoExecutionException("Failed to create the actionscript archive", e);
    }
    projectHelper.attachArtifact(project, "jar"/*Types.JANGAROO_TYPE*/, "sources", asarchive);
  }
}
