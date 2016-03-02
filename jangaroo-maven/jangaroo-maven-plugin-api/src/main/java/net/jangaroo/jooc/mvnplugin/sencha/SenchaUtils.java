package net.jangaroo.jooc.mvnplugin.sencha;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.Types;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SenchaUtils {

  public static final String SEPARATOR = "/";

  public static final String SENCHA_BASE_PATH = "sencha";
  public static final String SENCHA_PACKAGES = "packages";
  public static final String SENCHA_PACKAGES_LOCAL = "local";

  /**
   * The name of the folder of the generated module inside the {@link SenchaUtils#SENCHA_PACKAGES_LOCAL} folder.
   * Make sure that the name is not too long to avoid exceeding the max path length in windows.
   * The old path length relative to the target folder was 43 chars:
   * classes\META-INF\resources\joo\classes\com
   *
   * So to avoid compatiblity issues the max length for the path is:
   *
   * 43 - SENCHA_BASE_BATH.length - SENCHA_PACKAGES.length - SENCHA_PACKAGE_LOCAL.length
   *    - SENCHA_RELATIVE_CLASS_PATH.length - 4 (Separator)
   */
  public static final String LOCAL_PACKAGE_PATH = "package";

  public static final String SENCHA_RELATIVE_BUILD_PATH = "build";
  public static final String SENCHA_RELATIVE_CLASS_PATH = "src";
  public static final String SENCHA_RELATIVE_OVERRIDES_PATH = "overrides";
  public static final String SENCHA_RELATIVE_RESOURCES_PATH = "resources";
  public static final String SENCHA_RELATIVE_SASS_PATH = "sass";
  public static final String SENCHA_RELATIVE_SASS_ETC_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "etc";
  public static final String SENCHA_RELATIVE_SASS_SRC_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "src";
  public static final String SENCHA_RELATIVE_SASS_VAR_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "var";
  public static final String SENCHA_RELATIVE_CLASSIC_PATH = "classic";
  public static final String SENCHA_RELATIVE_MODERN_PATH = "modern";
  public static final String SENCHA_RELATIVE_PRODUCTION_PATH = "production";
  public static final String SENCHA_RELATIVE_TESTING_PATH = "testing";
  public static final String SENCHA_RELATIVE_DEVELOPMENT_PATH = "development";

  public static final String SENCHA_DIRECTORYNAME = ".sencha";
  public static final String SENCHA_CFG = "sencha.cfg";

  public static final String SENCHA_WORKSPACE_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + "workspace" + SEPARATOR + SENCHA_CFG;
  public static final String SENCHA_PACKAGE_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + "package" + SEPARATOR + SENCHA_CFG;
  public static final String SENCHA_APP_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + "app" + SEPARATOR + SENCHA_CFG;

  public static final String SENCHA_WORKSPACE_FILENAME = "workspace.json";
  public static final String SENCHA_PACKAGE_FILENAME = "package.json";
  public static final String SENCHA_APP_FILENAME = "app.json";
  public static final String SENCHA_PKG_EXTENSION = ".pkg";
  public static final String SENCHA_SASS_ETC_IMPORTS = "imports.scss";

  public static final Map<SenchaConfiguration.Type, String> PLACEHOLDERS = ImmutableMap.of(
          SenchaConfiguration.Type.APP, "${app.dir}",
          SenchaConfiguration.Type.CODE, "${package.dir}",
          SenchaConfiguration.Type.THEME, "${package.dir}",
          SenchaConfiguration.Type.WORKSPACE, "${workspace.dir}"
  );

  private static final String MAVEN_DEPENDENCY_SCOPE_TEST = "test";
  private static final String MAVEN_DEPENDENCY_SCOPE_PROVIDED = "provided";

  private static ObjectMapper objectMapper;

  private static final Pattern SENCHA_VERSION_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]+){0,3}$");

  private static String getSenchaPackageName(String groupId, String artifactId) {
    return groupId + "__" + artifactId;
  }

  public static String getSenchaPackageNameForMavenProject(MavenProject project) {
    return getSenchaPackageName(project.getGroupId(), project.getArtifactId());
  }

  public static String getSenchaPackageNameForArtifact(Artifact artifact) {
    return getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId());
  }

  private static String getSenchaVersionForMavenVersion(String version) {
    // Very simple matching for now, maybe needs some adjustment
    version = version.replace("-SNAPSHOT", "").replace("-", ".");
    if (SENCHA_VERSION_PATTERN.matcher(version).matches()) {
      return version;
    } else {
      return null;
    }
  }

  public static String getSenchaVersionForProject(MavenProject project) {
    return getSenchaVersionForMavenVersion(project.getVersion());

  }

  public static String getSenchaVersionForArtifact(Artifact artifact) {
    return getSenchaVersionForMavenVersion(artifact.getVersion());
  }

  public static String getSenchaPackageNameForTheme(String theme, MavenProject project) throws MojoExecutionException {
    String[] groupIdAndArtifactId = theme.split(":", 2);
    if (groupIdAndArtifactId.length < 2) {
      return theme;
    }
    // verify that provided artifact is under project dependencies
    String groupId = groupIdAndArtifactId[0];
    String artifactId = groupIdAndArtifactId[1];
    Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      if (groupId.equals(artifact.getGroupId())
              && artifactId.equals(artifact.getArtifactId())) {
        if (isSenchaPackageArtifact(artifact)) {
          return getSenchaPackageName(groupId, artifactId);
        }
        throw new MojoExecutionException("Theme name references to an artifact that contains no sencha package");
      }
    }
    throw new MojoExecutionException("Theme name references to an artifact which is not added to dependencies");
  }

  public static File findClosestSenchaWorkspaceDir(File dir) {
    File result;
    try {
      result = dir.getCanonicalFile();
    } catch (IOException e) {
      return null; //NOSONAR
    }
    while (null != result) {
      String[] list = result.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return SenchaUtils.SENCHA_WORKSPACE_FILENAME.equals(name);
        }
      });
      if (null != list
              && list.length > 0) {
        break;
      }
      result = result.getParentFile();
    }
    return result;
  }

  /**
   * Generates an absolute path to the module dir for the given relative path using a placeholder.
   *
   * @param type the sencha type
   * @param relativePath the path relative to the sencha module
   * @return path prefixed with a placeholder and a separator to have an absolute path
   */
  public static String generateAbsolutePathUsingPlaceholder(SenchaConfiguration.Type type, String relativePath) {
    String result = PLACEHOLDERS.get(type);
    if (!StringUtils.isEmpty(relativePath)
            && !relativePath.startsWith(SEPARATOR)) {
      result += SEPARATOR + relativePath;
    }
    return result;
  }

  public static ObjectMapper getObjectMapper() {
    if (null == objectMapper) {
      objectMapper = new ObjectMapper();
      objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
    return objectMapper;
  }

  public static Path getRelativePathFromWorkspaceToWorkingDir(File workingDirectory) throws MojoExecutionException {
    File closestSenchaWorkspaceDir = findClosestSenchaWorkspaceDir(workingDirectory);
    if (null == closestSenchaWorkspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace above workingDirectory");
    }
    Path workspacePath;
    try {
      workspacePath = closestSenchaWorkspaceDir.toPath().toRealPath();
    } catch (IOException e) {
      throw new MojoExecutionException("could not find path for sencha workspace", e);
    }
    Path workingDirectoryPath;
    try {
      workingDirectoryPath = workingDirectory.toPath().toRealPath();
    } catch (IOException e) {
      throw new MojoExecutionException("could not find path for working directory", e);
    }

    if (workspacePath.getRoot() == null || !workspacePath.getRoot().equals(workingDirectoryPath.getRoot())) {
      throw new MojoExecutionException("cannot find a relative path from workspace directory to working directory");
    }
    return workspacePath.relativize(workingDirectoryPath);
  }

  public static void extractRemotePackagesForProject(MavenProject project, String targetDirectory) throws MojoExecutionException {
    @SuppressWarnings("unchecked") Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
    for (Artifact artifact : dependencyArtifacts) {

      if (!isSenchaPackageArtifact(artifact)) {
        continue;
      }
      if ("net.jangaroo".equals(artifact.getGroupId())
              && "ext-js".equals(artifact.getArtifactId())) {
        // ext-js is handled differently
        continue;
      }

      if (!Types.JAVASCRIPT_EXTENSION.equals(artifact.getType())) {
        continue;
      }

      String senchaPackageName = SenchaUtils.getSenchaPackageNameForArtifact(artifact);
      String remotePackageFolderName = senchaPackageName + "__" + SenchaUtils.getSenchaVersionForArtifact(artifact);
      File remotePackageFolder = new File(targetDirectory + SEPARATOR + remotePackageFolderName);

      try {
        ZipFile zipFile = new ZipFile(artifact.getFile());
        ZipEntry packageJson = zipFile.getEntry(SENCHA_BASE_PATH + SEPARATOR + SENCHA_PACKAGE_FILENAME);
        // skip if no package json is found
        if (null == packageJson) {
          continue;
        }
        zipFile.close();

        // make sure folder ist cleaned up
        if (remotePackageFolder.exists()) {
          try {
            FileUtils.deleteDirectory(remotePackageFolder);
          } catch (IOException e) {
            throw new MojoExecutionException("could not remove old extracted package folder");
          }
        }
        if (!remotePackageFolder.exists() && !remotePackageFolder.mkdirs()) {
          throw new MojoExecutionException("could not create folder for remote package " + remotePackageFolder);
        }

        extractZipToDirectory(artifact.getFile(), remotePackageFolder, SENCHA_BASE_PATH + SEPARATOR);
      } catch (IOException e) {
        throw new MojoExecutionException("could not read from artifact file", e);
      }
    }
  }

  public static void extractZipToDirectory(File zip, File directory, String startFrom) throws MojoExecutionException {
    try {
      java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zip);
      Enumeration<?> enu = zipFile.entries();
      while (enu.hasMoreElements()) {
        java.util.zip.ZipEntry zipEntry = (java.util.zip.ZipEntry) enu.nextElement();

        String name = zipEntry.getName();

        if (name.equals(startFrom) || !name.startsWith(startFrom)) {
          continue;
        }

        name = name.substring(startFrom.length());

        // Do we need to create a directory ?
        File file = new File(directory.getAbsolutePath() + SEPARATOR + name);
        if (name.endsWith(SEPARATOR)) {
          file.mkdirs();
          continue;
        }

        File parent = file.getParentFile();
        if (parent != null) {
          parent.mkdirs();
        }

        // Extract the file
        InputStream is = zipFile.getInputStream(zipEntry);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = is.read(bytes)) >= 0) {
          fos.write(bytes, 0, length);
        }
        is.close();
        fos.close();

      }
      zipFile.close();
    } catch (IOException e) {
      throw new MojoExecutionException("could not unpack zip file", e);
    }
  }

  public static void extractZipToDirectory(File zip, File directory) throws MojoExecutionException {
    extractZipToDirectory(zip, directory, "");
  }

  public static boolean isSenchaPackageArtifact(Artifact artifact) throws MojoExecutionException {
    boolean result = false;

    if ("net.jangaroo".equals(artifact.getGroupId())
            && "ext-js".equals(artifact.getArtifactId())) {
      // ext-js is handled differently
      return false;
    }
    if (Types.JAVASCRIPT_EXTENSION.equals(artifact.getType())
            && !MAVEN_DEPENDENCY_SCOPE_TEST.equalsIgnoreCase(artifact.getScope())
            && !MAVEN_DEPENDENCY_SCOPE_PROVIDED.equalsIgnoreCase(artifact.getScope())) {
      if (null != artifact.getFile()) {
        try {
          ZipFile zipFile = new ZipFile(artifact.getFile());
          ZipEntry zipEntry = zipFile.getEntry("sencha/" + SenchaUtils.SENCHA_PACKAGE_FILENAME);
          if (zipEntry != null) {
            result = true;
          }
        } catch (IOException e) {
          throw new MojoExecutionException("could not open artifact jar", e);
        }
      }
    }

    return result;
  }
}
