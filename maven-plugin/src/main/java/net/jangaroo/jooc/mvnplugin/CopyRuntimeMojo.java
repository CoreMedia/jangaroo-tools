package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Mojo to extract the Jangaroo runtime from net.jangaroo:jangaroo-compiler:runtime:zip during compilation phase.
 *
 * @goal copy-runtime
 * @phase compile
 */
public class CopyRuntimeMojo extends AbstractMojo {

  /**
   * @component
   * @required
   * @readonly
   */
  private org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;

  /**
   * @component
   * @required
   * @readonly
   */
  private org.apache.maven.artifact.resolver.ArtifactResolver resolver;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   * @readonly
   */
  private java.util.List remoteRepositories;

  /**
   * @parameter expression="${localRepository}"
   * @required
   * @readonly
   */
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${plugin.artifacts}"
   * @required
   * @readonly
   */
  private List<Artifact> pluginArtifacts;

  /**
   * By default, the plugin will take the Jangaroo runtime from its own jangaroo-compiler dependency.
   * If necessary, this parameter may be set to override which artifact will be used as the source for the
   * Jangaroo runtime.
   * @parameter alias="runtime"
   */
  private Runtime runtime;

  /**
   * Target directory for the extracted runtime.
   * @parameter expression="${project.build.directory}/js-classes"
   */
  private File outputDirectory;

  protected File getOutputDirectory() {
    return outputDirectory;
  }

  public void execute() throws MojoExecutionException, MojoFailureException {
    Artifact artifact = resolveRuntimeArtifact();
    extractZipArtifact(artifact);
  }

  private void extractZipArtifact(Artifact artifact) throws MojoExecutionException {
    File artifactFile = artifact.getFile();
    ZipFile runtimeArchive = null;
    try {
      runtimeArchive = new ZipFile(artifactFile, ZipFile.OPEN_READ);
      Enumeration<? extends ZipEntry> entries = runtimeArchive.entries();
      while (entries.hasMoreElements()) {
        ZipEntry zipEntry = entries.nextElement();

        if (!zipEntry.isDirectory()) {
          File outputDirectory = getOutputDirectory();
          extractZipEntry(runtimeArchive, zipEntry, outputDirectory);
        }
      }
    } catch (IOException e) {
      throw new MojoExecutionException(
        "Failed to open runtime artifact " + artifact + " at " + artifactFile.getAbsolutePath());
    } finally {
      try {
        if (runtimeArchive != null)
          runtimeArchive.close();
      } catch (IOException e) {
        // do nothing
      }
    }
  }

  private void extractZipEntry(ZipFile archive, ZipEntry entry, File outputDirectory) throws MojoExecutionException {
    String name = entry.getName();

    File outputFile = new File(outputDirectory, name);
    File targetDirectory = outputFile.getParentFile();

    String targetDirPath = targetDirectory.getAbsolutePath();
    String targetFilePath = outputFile.getAbsolutePath();

    // create output file directory
    if (!targetDirectory.exists()) {
      getLog().debug("Creating non-existent target directory " + targetDirPath);
      if (!targetDirectory.mkdirs()) {
        throw new MojoExecutionException("Failed to create output directory " + targetDirPath);
      }
    }

    try {

      BufferedInputStream input = new BufferedInputStream(archive.getInputStream(entry));
      try {

        OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
          copyStream(outputDirectory, name, targetFilePath, input, output);
        } finally {
          try {
            output.close();
          } catch (Exception e) {
            // do nothing
          }
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Failed to open output file " + targetFilePath, e);
      } finally {
        try {
          input.close();
        } catch (Exception e) {
          // do nothing
        }
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to zip entry stream " + entry, e);
    }
  }

  private void copyStream(File outputDirectory, String entryName, String targetPath,
                          InputStream input, OutputStream output) throws MojoExecutionException {

    getLog().debug(String.format("Extracting %s to %s", entryName, outputDirectory));
    try {
      int read;
      byte[] buffer = new byte[16 * 1024];
      while ((read = input.read(buffer)) != -1) {
        output.write(buffer, 0, read);
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Error while extracting runtime file to " + targetPath, e);
    }
  }

  private Artifact resolveRuntimeArtifact() throws MojoFailureException, MojoExecutionException {
    Artifact result = null;

    // Use runtime, if configured.
    if (runtime != null) {
      if (runtime.getVersion() == null)
        throw new MojoExecutionException("<runtime>/<version> is not configured");

      result = artifactFactory.createArtifactWithClassifier(
        runtime.getGroupId(), runtime.getArtifactId(), runtime.getVersion(),
        Runtime.TYPE_RUNTIME, runtime.getClassifier());
    }

    if (result == null) {
      for (Artifact pluginArtifact : pluginArtifacts) {
        getLog().debug("Inspecting pluginArtifact: " + pluginArtifact);
        if (Runtime.GROUP_ID_JANGAROO.equals(pluginArtifact.getGroupId()) &&
          Runtime.ARTIFACT_ID_JOOC.equals(pluginArtifact.getArtifactId()) &&
          Runtime.CLASSIFIER_RUNTIME.equals(pluginArtifact.getClassifier()) &&
          Runtime.TYPE_RUNTIME.equals(pluginArtifact.getType())) {

          getLog().debug("Selected Jangaroo runtime pluginArtifact: " + pluginArtifact);
          result = pluginArtifact;
          break;
        }
      }
    }

    if (result == null) {
      throw new MojoExecutionException(String.format(
        "Cannot find any version of the required runtime artifact among the plugin artifacts: %s:%s:%s:%s",
        Runtime.GROUP_ID_JANGAROO, Runtime.ARTIFACT_ID_JOOC, Runtime.CLASSIFIER_RUNTIME, Runtime.TYPE_RUNTIME));
    }

    getLog().debug("Using Jangaroo runtime artifact: " + result);

    try {
      resolver.resolve( result, remoteRepositories, localRepository );
    } catch (Exception e) {
      throw new MojoExecutionException("Cannot resolve runtime artifact", e);
    }

    return result;
  }
}
