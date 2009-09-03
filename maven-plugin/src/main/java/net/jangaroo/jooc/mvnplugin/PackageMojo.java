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
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * Generated Resources
   *
   * @parameter expression="${project.build.directory}/generated-resources"
   */
  private File generatedResourcesDirectory;

  /**
   * Source directory to scan for files to package in the sources archive. These files
   * have not been compiled since these classes are available by default. They are needed
   * to make them available via IDE and for the asdoc generations.
   *
   * @parameter expression="${basedir}/src/main/joo-api"
   */
  private File jooApiDirectory;

  /**
   * Source directory containing javascript that will be just included.
   *
   * @parameter expression="${basedir}/src/main/joo-js"
   */
  private File jooJsDirectory;

  /**
   * The filename of the js file.
   *
   * @parameter default-value="${project.build.finalName}"
   */
  private String finalName;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="jangaroo"
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
      if (jooJsDirectory.exists()) {
        archiver.addDirectory(jooJsDirectory);
      }
      if(generatedResourcesDirectory.exists()) {
        archiver.addDirectory(generatedResourcesDirectory);
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

    if (sourceDirectory.exists() && sourceDirectory.list().length != 0 ||
        generatedSourcesDirectory.exists() && generatedSourcesDirectory.list().length != 0 ||
        jooApiDirectory.exists() && jooApiDirectory.list().length != 0) {
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
        if(generatedSourcesDirectory.exists()) {
          archiver.addDirectory(generatedSourcesDirectory);
        }
        if (jooApiDirectory.exists()) {
          archiver.addDirectory(jooApiDirectory);
        }
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        archiver.addFile(project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
                + "/pom.xml");
        archiver.setDestFile(asarchive);
        archiver.createArchive();
        archiver.reset();
      }
      catch (Exception e) {
        throw new MojoExecutionException("Failed to create the actionscript archive", e);
      }
      projectHelper.attachArtifact(project, "jar"/*Types.JANGAROO_TYPE*/, "sources", asarchive);
    }
  }
}
