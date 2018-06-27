package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.plugin.MojoExecutionException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.regex.Pattern;

public final class FileHelper {

  private FileHelper() {
    // hiding constructor of utility class
  }

  public static void copyFiles(File srcDir, File targetDir) throws MojoExecutionException {
    if (srcDir.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, targetDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Copying sencha sources from %s to %s failed.", srcDir, targetDir ), e);
      }
    }
  }

  public static void copyDirectory(@Nonnull File srcDir, @Nonnull File targetDir) throws MojoExecutionException {
    if (srcDir.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(srcDir, targetDir);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Copying sencha sources from %s to %s failed.", srcDir, targetDir ), e);
      }
    }
  }

  public static void ensureDirectory(File dir) throws MojoExecutionException {
    if (!dir.exists() && !dir.mkdirs()) {
      throw new MojoExecutionException("could not create folder for directory " + dir);
    }
  }

  public static void copyFilesToDirectory(@Nonnull File source, @Nonnull File target, String matchPattern)
          throws MojoExecutionException {
    File[] files = source.listFiles();
    if (files == null || files.length == 0) {
      return;
    }
    Pattern pattern = null;
    if (matchPattern != null) {
      pattern = Pattern.compile(matchPattern);
    }
    for (File file: files) {
      if (file.isFile() && (pattern == null || pattern.matcher(file.getName()).matches())) {
        doCopyFile(file, target);
      }
    }
  }

  private static void doCopyFile(@Nonnull File source, @Nonnull File target) throws MojoExecutionException {
    try {
      Files.copy(source.toPath(),
              target.toPath().resolve(source.getName()),
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.COPY_ATTRIBUTES,
              LinkOption.NOFOLLOW_LINKS);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not copy files to janagaroo dir", e);
    }
  }

  public static void copyDirectories(@Nonnull File source, @Nonnull File target, Set<String> excludeDirectories)
          throws MojoExecutionException {
    File[] files = source.listFiles();
    if (files == null || files.length == 0) {
      return;
    }
    for (File file: files) {
      if (file.isDirectory() && (excludeDirectories == null || !excludeDirectories.contains(file.getName()))) {
        copyDirectory(file, target);
      }
    }
  }

  public static String relativize(@Nonnull Path base, @Nonnull String path) {
    Path normalizedPath = Paths.get(path).normalize();
    return FilenameUtils.separatorsToUnix(base.relativize(normalizedPath).toString());
  }

  public static String relativize(@Nonnull Path base, @Nonnull File path) {
    Path normalizedPath = path.toPath().normalize();
    return FilenameUtils.separatorsToUnix(base.relativize(normalizedPath).toString());
  }

  public static String relativize(@Nonnull File base, @Nonnull File path) {
    return relativize(base.toPath(), path);
  }

  public static void createSymbolicLink(Path link, Path target) throws IOException {
    try {
      Files.createSymbolicLink(link, target);
    } catch (IOException e) {
      // TODO: can we convince the security manager to allow LinkPermission("symbolic")?
      if (SystemUtils.IS_OS_WINDOWS) {
        // fall back to command line execution:
        // in recent Windows 10 versions, this seems to work even without the user holding the "Create Symbolic Link" right.
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(link.toFile().getParentFile());
        CommandLine mkLinkCommand = new CommandLine("CMD");
        mkLinkCommand.addArgument("/C");
        mkLinkCommand.addArgument("MKLINK");
        mkLinkCommand.addArgument("/D");
        mkLinkCommand.addArgument(link.getFileName().toString());
        mkLinkCommand.addArgument(target.toString());
        executor.execute(mkLinkCommand);
      } else {
        throw e;
      }
    }
  }
}
