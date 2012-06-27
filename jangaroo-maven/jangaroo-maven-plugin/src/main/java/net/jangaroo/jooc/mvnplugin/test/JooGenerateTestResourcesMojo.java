package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.PackageApplicationMojo;
import net.jangaroo.jooc.mvnplugin.Types;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Prepares the Javascript Testenvironment including generation of the HTML page and decompression of jangaroo
 * dependencies. This plugin is executed in the <code>generate-test-resources</code> phase of the jangaroo lifecycle.
 *
 * @requiresDependencyResolution test
 * @goal unpack-jangaroo-test-dependencies
 * @phase test-compile
 */
@SuppressWarnings({"UnusedDeclaration"})
public class JooGenerateTestResourcesMojo extends PackageApplicationMojo {

  /**
   * Output directory into whose joo/classes sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getPackageSourceDirectory}.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Location of Jangaroo test resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getPackageSourceDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-test-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-test-output/"
   */
  private File testPackageSourceDirectory;

  /**
   * Output directory to assemble the test Web-app.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   */
  private File testOutputDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you enable it using the
   * "maven.test.skip" property, because maven.test.skip disables both running the tests and compiling the tests.
   * Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  private boolean skip;
  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  private boolean skipTests;
  /**
   * @parameter expression="${project.testResources}"
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private List<Resource> testResources;

  /**
   * Hexus Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;


  public File getPackageSourceDirectory() {
    return Types.JANGAROO_TYPE.equals(project.getPackaging()) ? outputDirectory : testPackageSourceDirectory;
  }

  protected boolean isTestAvailable() {
    return true; // TODO
  }

  public void unpack(File target)
    throws ArchiverException {

    unarchiver.setOverwrite(false);
    unarchiver.setArchiveFilters(Collections.singletonList(new MetaInfResourcesArchiveFilter()));
    Set<Artifact> dependencies = getArtifacts();

    for (Artifact dependency : dependencies) {
      getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      if (!dependency.isOptional() && "jar".equals(dependency.getType())) {
        getLog().debug("Unpacking jangaroo dependency [" + dependency.toString() + "]");
        unarchiver.setSourceFile(dependency.getFile());

        unpack(dependency, target);
      }
    }
  }

  public void unpack(Artifact artifact, File target)
    throws ArchiverException {
    unarchiver.setSourceFile(artifact.getFile());
    if (target.mkdirs()) {
      getLog().debug("created unarchiver target directory " + target);
    }
    unarchiver.setDestDirectory(target);
    unarchiver.setOverwrite(false);
    try {
      unarchiver.extract();
    }
    catch (Exception e) {
      throw new ArchiverException("Failed to extract javascript artifact to " + target, e);
    }

  }

  public void execute() throws MojoExecutionException {
    if (!skip && !skipTests) {
      try {
        if (isTestAvailable()) {
          getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
          unpack(testOutputDirectory);
          File webappDirectory = new File(testOutputDirectory, "META-INF/resources");
          createWebapp(webappDirectory);
          getLog().info("Copying output from " + outputDirectory.getAbsolutePath() + " to " + testOutputDirectory.getAbsolutePath() + "...");
          FileUtils.copyDirectoryStructureIfModified(outputDirectory, testOutputDirectory);
          for (Resource r : testResources) {
            File testResourceDirectory = new File(r.getDirectory());
            if (testResourceDirectory.exists()) {
              getLog().info("Copying resources from " + r.getDirectory() + " to " + webappDirectory.getAbsolutePath() + "...");
              FileUtils.copyDirectoryStructureIfModified(testResourceDirectory, webappDirectory);
            }
          }
        }
      } catch (ArchiverException e) {
        throw new MojoExecutionException("Cannot unpack jangaroo dependencies", e);
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot copy jangaroo files/ generate html test page", e);
      }
    } else {
      getLog().info("Skipping generation of test resources");
    }
  }

  @Override
  protected void writeThisJangarooModuleScript(File scriptDirectory, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    super.writeThisJangarooModuleScript(scriptDirectory, jangarooApplicationWriter, jangarooApplicationAllWriter);
    writeModule(scriptDirectory, project.getGroupId(), project.getArtifactId() + "-test", project.getVersion(),
      jangarooApplicationWriter, jangarooApplicationAllWriter);
  }

  private static class MetaInfResourcesArchiveFilter implements ArchiveFileFilter {
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return entryName.startsWith("META-INF/resources");
    }
  }
}
