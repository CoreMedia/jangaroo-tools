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
   * Optional classifier
   *
   * @parameter
   */
  private String classifier;

  /**
   * @parameter
   */
  private File manifest;

  /**
   * Location of the scripts files.
   *
   * @parameter expression="${project.build.directory}/joo/classes"
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
      archiver.addDirectory(scriptsDirectory);
      String groupId = project.getGroupId();
      String artifactId = project.getArtifactId();
      archiver.addFile(project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
              + "/pom.xml");
      archiver.setDestFile(jsarchive);
      archiver.createArchive();
    }
    catch (Exception e) {
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }

    if (classifier != null) {
      projectHelper.attachArtifact(project, Types.JAVASCRIPT_TYPE, classifier, jsarchive);
    } else {
      project.getArtifact().setFile(jsarchive);
    }
  }
}
