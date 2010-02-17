package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.model.Resource;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Prepares the Javascript Testenvironment including generation of the HTML page and decompression
 * of jangaroo dependencies.
 * This plugin is executed in the <code>generate-test-resources</code> phase of the jangaroo lifecycle.
 *
 * @requiresDependencyResolution test
 * @goal unpack-jangaroo-test-dependencies
 * @phase generate-test-resources
 */
public class JooGenerateTestResourcesMojo extends AbstractJooTestMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  private List remoteRepositories;

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/classes"
   */
  private File outputDirectory;

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/${project.artifactId}.js"
   */
  private File outputFileName;


  /**
   * @component
   */
  private MavenProjectBuilder mavenProjectBuilder;


  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skip) {
      try {
        if (isTestAvailable()) {
          testOutputDirectory.mkdir();
          getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
          unpack();
          copyMainJsAndClasses();
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
      } catch (ArchiverException e) {
        throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
      }
    } else {
      getLog().info("Skipping generation of test resources");
    }
  }

  /**
   * Copies the project's jooc javascript output to the <code>testOutputDirectory</code> since they have to be
   * accessible by the javascript execution environment.
   *
   * @throws IOException if copy fails
   */
  private void copyMainJsAndClasses() throws IOException {
    for(Resource r : testResources) {
      FileUtils.copyDirectoryStructureIfModified(new File(r.getDirectory()), testOutputDirectory);
    }
    if (outputFileName.exists()) {
      FileUtils.copyFileToDirectoryIfModified(outputFileName, testOutputDirectory);
    } else {
      getLog().info("Cannot copy " + outputFileName + " to " + testOutputDirectory + ". It does not exist.");
    }
    if (outputDirectory.exists()) {
      getLog().info("copy " + outputDirectory + " to " + new File(testOutputDirectory, "classes"));
      FileUtils.copyDirectoryStructureIfModified(outputDirectory, new File(testOutputDirectory, "classes"));
    } else {
      getLog().info("Cannot copy " + outputDirectory + " to " + testOutputDirectory + ". It does not exist.");
    }
  }


  private String getInternalId(Artifact art) {
    return art.getGroupId() + ":" + art.getArtifactId();
  }



  /**
   * Unpacks all jangaroo dependencies (transitively) to <code>testOutputDirectory</code>.
   *
   * @throws IOException       if file copy goes wrong
   * @throws ArchiverException if an archive is corrupt
   */
  public void unpack()
          throws IOException, ArchiverException {
    unarchiver.setOverwrite(false);
    unarchiver.setArchiveFilters(Collections.singletonList(
            new MetaInfArchiveFileFilter()));

    for (Artifact dependency : ((List<Artifact>) project.getTestArtifacts())) {
      getLog().debug("Dependency: " + getInternalId(dependency) + " type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        unarchiver.setDestFile(null);
        unarchiver.setDestDirectory(testOutputDirectory);
        unarchiver.setSourceFile(dependency.getFile());
        unarchiver.extract();
      }
    }
  }

  private static class MetaInfArchiveFileFilter implements ArchiveFileFilter {
    @Override
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return !entryName.startsWith("META-INF");
    }

  }
}
