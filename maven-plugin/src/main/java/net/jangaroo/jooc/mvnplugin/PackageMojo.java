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
   * @parameter expression="${project.build.directory}"
   */
  private File targetDir;

  /**
   * @component
   */
  MavenProjectHelper projectHelper;

  /**
   * The output directory of the AS->JS compilation process.
   *
   * @parameter default-value="${project.build.outputDirectory}"
   */
  private File outputDirectory;


  /**
   * Source directory to scan for files to package in the sources archive. These files
   * have been compiled.
   *
   * @parameter default-value="${project.build.sourceDirectory}"
   */
  private File sourceDirectory;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter default-value="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * Source directory to scan for files to package in the sources archive. These files
   * have not been compiled since these classes are available by default. They are needed
   * to make them available via IDE and for the asdoc generations.
   *
   * @parameter default-value="${basedir}/src/main/joo-api"
   */
  private File jooApiDirectory;


  /**
   * The filename of the js file.
   *
   * @parameter default-value="${project.artifactId}"
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
  private File compilerOutputDirectory;

  public void execute()
          throws MojoExecutionException {
    File jsarchive = new File(targetDir, finalName + "." + Types.JAVASCRIPT_EXTENSION);
    try {
      if (manifest != null) {
        archiver.setManifest(manifest);
      } else {
        archiver.createDefaultManifest(project);
      }
      if (compilerOutputDirectory.exists()) {
        archiver.addDirectory(compilerOutputDirectory);
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
      File asarchive = new File(targetDir, finalName + "-sources." + Types.JAVASCRIPT_EXTENSION);
      try {
        if (manifest != null) {
          archiver.setManifest(manifest);
        } else {
          archiver.createDefaultManifest(project);
        }
        if (sourceDirectory.exists()) {
          archiver.addDirectory(sourceDirectory);
        }
        if (generatedSourcesDirectory.exists()) {
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
