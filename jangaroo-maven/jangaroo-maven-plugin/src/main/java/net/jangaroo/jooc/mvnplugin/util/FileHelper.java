package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.AbstractArchiver;
import org.codehaus.plexus.archiver.ArchivedFileSet;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.FileSet;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.util.AbstractFileSet;
import org.codehaus.plexus.archiver.util.DefaultFileSet;
import org.codehaus.plexus.archiver.zip.PlexusArchiverZipFileResourceCollection;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResourceCollection;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.APPS_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.EXT_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.PACKAGES_DIRECTORY_NAME;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_TEMPLATE_ARTIFACT_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.SEPARATOR;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static org.codehaus.plexus.archiver.util.DefaultArchivedFileSet.archivedFileSet;
import static org.codehaus.plexus.archiver.util.DefaultFileSet.fileSet;

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
    if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
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
    if (!SystemUtils.IS_OS_WINDOWS) {
      Files.createSymbolicLink(link, target);
    } else {
      // In Windows, users need special privileges to create symlinks. But there are less general symlinks that
      // only work within one volume (which is fine for our purpose) called junctions (for directories).
      // Creating directory junctions works without additional privileges. Unfortunately, the current implementation
      // of Files.createSymbolicLink() under Windows uses symlinks.
      // Thus, for windows, we always use mklink to create directory junctions (/J) in favor of symlinks (/D).
      // fall back to command line execution:
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(link.toFile().getParentFile());
      // prevent command line tool output from appearing in Maven log:
      executor.setStreamHandler(new PumpStreamHandler(null));

      CommandLine mkLinkCommand = new CommandLine("CMD");
      mkLinkCommand.addArgument("/C");
      mkLinkCommand.addArgument("MKLINK");
      mkLinkCommand.addArgument("/J");
      mkLinkCommand.addArgument(link.getFileName().toString());
      mkLinkCommand.addArgument(target.toString());
      try {
        executor.execute(mkLinkCommand);
      } catch (IOException e) {
        throw new IOException("Error while invoking " + mkLinkCommand.toString(), e);
      }
    }
  }

  public static void createAppOrAppOverlayJar(MavenSession session,
                                              JarArchiver archiver,
                                              Map<String, String> manifestEntries,
                                              ArtifactHandlerManager artifactHandlerManager) throws MojoExecutionException {
    FileHelper.createAppOrAppOverlayJar(session, archiver, manifestEntries, artifactHandlerManager, null);
  }

  public static void createAppOrAppOverlayJar(MavenSession session,
                                              JarArchiver archiver,
                                              Map<String, String> manifestEntries,
                                              ArtifactHandlerManager artifactHandlerManager,
                                              String senchaAppBuild) throws MojoExecutionException {
    FileHelper.createAppOrAppOverlayJar(session, archiver, manifestEntries, artifactHandlerManager, senchaAppBuild, false);
  }

  public static void createPluginZip(MavenSession session,
                                     JarArchiver archiver,
                                     ArtifactHandlerManager artifactHandlerManager) throws MojoExecutionException {
    // TODO: transform plugins?
    FileHelper.createAppOrAppOverlayJar(session, archiver, null, artifactHandlerManager, null, true);
  }

  private static void createAppOrAppOverlayJar(MavenSession session,
                                               JarArchiver archiver,
                                               Map<String, String> manifestEntries,
                                               ArtifactHandlerManager artifactHandlerManager,
                                               String senchaAppBuild,
                                               boolean zipMode) throws MojoExecutionException {

    MavenProject project = session.getCurrentProject();
    File appDir = new File(project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY);

    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + (zipMode ? ".zip" : ".jar"));

    if (senchaAppBuild == null || SenchaUtils.DEVELOPMENT_PROFILE.equals(senchaAppBuild)) {
      // add the Jangaroo compiler resources to the resulting JAR
      DefaultFileSet fileSet = fileSet(appDir);
      if (!zipMode) {
        fileSet = fileSet.prefixed(MavenPluginHelper.META_INF_RESOURCES);
      }
      fileSet.setExcludes(new String[]{
              "**/build/temp/**",
              "**/" + PACKAGES_DIRECTORY_NAME + SEPARATOR + getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_APP_TEMPLATE_ARTIFACT_ID) + "/**",
              PACKAGES_DIRECTORY_NAME + SEPARATOR + getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID) + "/**",
              "**/*-timestamp"
      });
      fileSet.setIncludingEmptyDirectories(false);
      addFileSetFollowingSymLinks(archiver, fileSet);
    }
    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);
    mavenArchiver.configureReproducible(project.getProperties().getProperty("project.build.outputTimestamp"));
    try {
      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
      if (manifestEntries != null) {
        archive.addManifestEntries(manifestEntries);
      }
      mavenArchiver.createArchive(session, project, archive);
    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }
    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar"/"zip" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(zipMode ? Type.ZIP_EXTENSION : Type.JAR_EXTENSION));
  }

  public static void createAppsJar(MavenSession session,
                                   JarArchiver archiver,
                                   Map<String, String> manifestEntries,
                                   ArtifactHandlerManager artifactHandlerManager,
                                   String senchaAppBuild,
                                   File appsDir,
                                   Map<String, List<File>> appNamesToDirsOrJars,
                                   String rootAppName) throws MojoExecutionException {
    if (senchaAppBuild != null && !SenchaUtils.DEVELOPMENT_PROFILE.equals(senchaAppBuild)) {
      // really important?
      throw new MojoExecutionException("Apps jar is only supported for developer mode");
    }
    MavenProject project = session.getCurrentProject();
    appsDir = appsDir != null ? appsDir : new File(project.getBuild().getDirectory() + SenchaUtils.APPS_TARGET_DIRECTORY);

    File jarFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");

    // add the Jangaroo compiler resources to the resulting JAR first, no files will be overridden afterwards
    DefaultFileSet fileSet = fileSet(appsDir).prefixed(MavenPluginHelper.META_INF_RESOURCES);
    fileSet.setIncludingEmptyDirectories(false);
    addFileSetFollowingSymLinks(archiver, fileSet);

    appNamesToDirsOrJars.forEach((appName, appDirsOrJars) -> {
      appDirsOrJars.forEach(appDirOrJar -> {
        // add the Jangaroo compiler resources to the resulting JAR
        boolean isRootApp = appName.equals(rootAppName);
        String appPath = isRootApp ? "" : APPS_DIRECTORY_NAME + SEPARATOR + appName + SEPARATOR;
        boolean isDirectory = appDirOrJar.isDirectory();
        // while FileSet#setPrefix determines a prefix inside the generated archive, the
        // sourcePrefix is meant as a prefix for the input files
        String sourcePrefix = isDirectory ? "" : MavenPluginHelper.META_INF_RESOURCES;

        AbstractFileSet<?> appFileSet = isDirectory ? fileSet(appDirOrJar) : archivedFileSet(appDirOrJar);
        appFileSet.prefixed(MavenPluginHelper.META_INF_RESOURCES + appPath);

        appFileSet.setExcludes(new String[]{
                "**/build/temp/**",
                "**/*-timestamp",
                sourcePrefix + EXT_DIRECTORY_NAME + SEPARATOR + "**",
                sourcePrefix + PACKAGES_DIRECTORY_NAME + SEPARATOR + "**",
        });
        appFileSet.setIncludingEmptyDirectories(false);
        if (isDirectory) {
          addFileSetFollowingSymLinks(archiver, (FileSet) appFileSet);
        } else {
          addArchivedFileSetRemovingPrefixFromSource(archiver, (ArchivedFileSet) appFileSet, MavenPluginHelper.META_INF_RESOURCES);
        }

        // add the Jangaroo compiler resources to the resulting JAR
        AbstractFileSet<?> packagesFileSet = isDirectory ? fileSet(appDirOrJar) : archivedFileSet(appDirOrJar);
        packagesFileSet.prefixed(MavenPluginHelper.META_INF_RESOURCES);

        packagesFileSet.setIncludes(new String[]{
                sourcePrefix + EXT_DIRECTORY_NAME + SEPARATOR + "**",
                sourcePrefix + PACKAGES_DIRECTORY_NAME + SEPARATOR + "**",
        });
        packagesFileSet.setExcludes(new String[]{
                sourcePrefix + PACKAGES_DIRECTORY_NAME + SEPARATOR + getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID) + "/**",
        });
        packagesFileSet.setIncludingEmptyDirectories(false);
        if (isDirectory) {
          addFileSetFollowingSymLinks(archiver, (FileSet) packagesFileSet);
        } else {
          addArchivedFileSetRemovingPrefixFromSource(archiver, (ArchivedFileSet) packagesFileSet, MavenPluginHelper.META_INF_RESOURCES);
        }
      });
    });

    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.configureReproducible(project.getProperties().getProperty("project.build.outputTimestamp"));
    mavenArchiver.setOutputFile(jarFile);
    try {
      MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
      archive.setManifestFile(MavenPluginHelper.createDefaultManifest(project));
      if (manifestEntries != null) {
        archive.addManifestEntries(manifestEntries);
      }
      mavenArchiver.createArchive(session, project, archive);
    } catch (Exception e) { // NOSONAR
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }
    Artifact mainArtifact = project.getArtifact();
    mainArtifact.setFile(jarFile);
    // workaround for MNG-1682: force maven to install artifact using the "jar" handler
    mainArtifact.setArtifactHandler(artifactHandlerManager.getArtifactHandler(Type.JAR_EXTENSION));
  }

  /**
   * Method copied + slightly adapted from AbstractArchiver because of symlink handling (see below).
   */
  private static void addFileSetFollowingSymLinks(@Nonnull AbstractArchiver archiver, @Nonnull final FileSet fileSet )
          throws ArchiverException
  {
    final File directory = fileSet.getDirectory();

    // The PlexusIoFileResourceCollection contains platform-specific File.separatorChar which
    // is an interesting cause of grief, see PLXCOMP-192
    final PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();

    // ALWAYS follow symlinks
    collection.setFollowingSymLinks(true);

    collection.setIncludes( fileSet.getIncludes() );
    collection.setExcludes( fileSet.getExcludes() );
    collection.setBaseDir( directory );
    collection.setFileSelectors( fileSet.getFileSelectors() );
    collection.setIncludingEmptyDirectories( fileSet.isIncludingEmptyDirectories() );
    collection.setPrefix( fileSet.getPrefix() );
    collection.setCaseSensitive( fileSet.isCaseSensitive() );
    collection.setUsingDefaultExcludes( fileSet.isUsingDefaultExcludes() );
    collection.setStreamTransformer( fileSet.getStreamTransformer() );

    if ( archiver.getOverrideDirectoryMode() > -1 || archiver.getOverrideFileMode() > -1 )
    {
      collection.setOverrideAttributes( -1, null, -1, null, archiver.getOverrideFileMode(), archiver.getOverrideDirectoryMode() );
    }

    if ( archiver.getDefaultDirectoryMode() > -1 || archiver.getDefaultFileMode() > -1 )
    {
      collection.setDefaultAttributes( -1, null, -1, null, archiver.getDefaultFileMode(), archiver.getDefaultDirectoryMode() );
    }

    archiver.addResources( collection );
  }

  private static void addArchivedFileSetRemovingPrefixFromSource(@Nonnull AbstractArchiver archiver, @Nonnull final ArchivedFileSet fileSet, String prefix )
          throws ArchiverException
  {
    final File archive = fileSet.getArchive();

    final PlexusArchiverZipFileResourceCollection collection = new PlexusArchiverZipFileResourceCollection();

    collection.setFile(archive);
    collection.setIncludes( fileSet.getIncludes() );
    collection.setExcludes( fileSet.getExcludes() );
    collection.setIncludingEmptyDirectories( fileSet.isIncludingEmptyDirectories() );
    collection.setPrefix( fileSet.getPrefix() );
    collection.setCaseSensitive( fileSet.isCaseSensitive() );
    collection.setUsingDefaultExcludes( fileSet.isUsingDefaultExcludes() );
    collection.setStreamTransformer( fileSet.getStreamTransformer() );

    FileSelector[] fileSelectors = fileSet.getFileSelectors();
    List<FileSelector> fileSelectorsList = fileSelectors == null ? new ArrayList<>() : Arrays.asList(fileSelectors);
    fileSelectorsList.add(0,
            fileInfo -> !prefix.equals(fileInfo.getName()) && fileInfo.getName().startsWith(prefix)
    );
    // as soon as an own fileSelector is used the include/exclude list is no longer applied
    if (fileSet.getIncludes() != null || fileSet.getExcludes() != null) {
      IncludeExcludeFileSelector includeExcludeFileSelector = new IncludeExcludeFileSelector();
      includeExcludeFileSelector.setIncludes(fileSet.getIncludes());
      includeExcludeFileSelector.setExcludes(fileSet.getExcludes());
      fileSelectorsList.add(includeExcludeFileSelector);
    }
    collection.setFileSelectors(fileSelectorsList.toArray(new FileSelector[]{}));
    collection.setFileMappers(new FileMapper[]{
            s -> s.replaceFirst("^" + prefix, "")
    });

    archiver.addResources( collection );
  }
}
