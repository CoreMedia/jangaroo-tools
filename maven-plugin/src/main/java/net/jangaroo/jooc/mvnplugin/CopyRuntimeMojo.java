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
public class CopyRuntimeMojo extends AbstractRuntimeMojo {

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
}
