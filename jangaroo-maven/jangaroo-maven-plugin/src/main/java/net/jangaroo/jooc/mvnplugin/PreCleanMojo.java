package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.apprunner.util.Junctions;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

@Mojo(name = "pre-clean", defaultPhase = LifecyclePhase.PRE_CLEAN, threadSafe = true)
public class PreCleanMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
  private File targetDirectory;

  /**
   * Scan the whole Maven target directory for hard directory links ("junctions") and remove them the soft way,
   * i.e. without removing their target directory.
   */
  @Override
  public void execute() throws MojoFailureException {
    if (SystemUtils.IS_OS_WINDOWS) {
      Queue<Path> toScan = new LinkedList<>();
      Queue<Path> toRemove = new LinkedList<>();
      if (targetDirectory.exists() && targetDirectory.isDirectory()) {
        toScan.add(targetDirectory.toPath());
      }
      while (!toScan.isEmpty()) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(toScan.poll())) {
          for (Path path : directoryStream) {
            if (Files.isDirectory(path)) {
              (Junctions.isJunction(path) ? toRemove : toScan).add(path);
            }
          }
        } catch (IOException ex) {
          throw new MojoFailureException("Unable to enumerate target directory " + targetDirectory.getPath(), ex);
        }
      }
      getLog().info(String.format("Removing %d directory junctions...", toRemove.size()));
      for (Path path : toRemove) {
        try {
          getLog().debug("  Removing directory junction " + path);
          Files.deleteIfExists(path);
        } catch (IOException e) {
          throw new MojoFailureException("Unable to remove directory junction " + path, e);
        }
      }
    }
  }

}
