package net.jangaroo.jooc.mvnplugin.sencha;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

public class SenchaUtils {

  public static final String SEPARATOR = "/";

  public static final String SENCHA_BASE_PATH = "sencha";
  public static final String SENCHA_PACKAGES = "packages";
  public static final String SENCHA_PACKAGES_LOCAL = "local";
  public static final String SENCHA_PACKAGES_REMOTE = "remote";

  public static final String SENCHA_RELATIVE_CLASS_PATH = "src";
  public static final String SENCHA_RELATIVE_OVERRIDES_PATH = "overrides";
  public static final String SENCHA_RELATIVE_RESOURCES_PATH = "resources";

  public static final String SENCHA_DIRECTORYNAME = ".sencha";
  public static final String SENCHA_CFG = "sencha.cfg";

  public static final String SENCHA_WORKSPACE_CONFIG = SENCHA_DIRECTORYNAME + File.separator + "workspace" + File.separator + SENCHA_CFG;
  public static final String SENCHA_PACKAGE_CONFIG = SENCHA_DIRECTORYNAME + File.separator + "package" + File.separator + SENCHA_CFG;
  public static final String SENCHA_APP_CONFIG = SENCHA_DIRECTORYNAME + File.separator + "app" + File.separator + SENCHA_CFG;

  public static final String SENCHA_WORKSPACE_FILENAME = "workspace.json";
  public static final String SENCHA_PACKAGE_FILENAME = "package.json";
  public static final String SENCHA_APP_FILENAME = "app.json";

  public static final Map<SenchaConfiguration.Type, String> PLACEHOLDERS = ImmutableMap.of(
          SenchaConfiguration.Type.APP, "${app.dir}",
          SenchaConfiguration.Type.CODE, "${package.dir}",
          SenchaConfiguration.Type.THEME, "${package.dir}",
          SenchaConfiguration.Type.WORKSPACE, "${workspace.dir}"
  );

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

  public static File findClosestSenchaWorkspaceDir(File dir) {
    File result = dir;
    while (null != result) {
      String[] list = result.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return SenchaUtils.SENCHA_WORKSPACE_FILENAME.equals(name);
        }
      });
      if (list.length > 0) {
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
}
