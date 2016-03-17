package net.jangaroo.jooc.mvnplugin.sencha;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import net.jangaroo.jooc.mvnplugin.MavenSenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SenchaUtils {

  public static final String SEPARATOR = "/";

  public static final String SENCHA_BASE_PATH = "sencha";

  public static final String PACKAGE_EXTENSION = "pkg";

  public static final String LOCAL_PACKAGES_PATH = "/packages/local/";

  public static final String LOCAL_PACKAGE_PATH = LOCAL_PACKAGES_PATH + "package/";

  public static final String LOCAL_PACKAGE_BUILD_PATH = LOCAL_PACKAGE_PATH + "build/";

  public final static String APP_TARGET_DIRECTORY = "/app";

  /**
   * The name of the folder of the generated module inside the {@link #LOCAL_PACKAGE_PATH} folder.
   * Make sure that the name is not too long to avoid exceeding the max path length in windows.
   * The old path length relative to the target folder was 43 chars:
   * classes\META-INF\resources\joo\classes\com
   *
   * So to avoid compatiblity issues the max length for the path is:
   *
   * 43 - SENCHA_BASE_BATH.length - SENCHA_PACKAGES.length - SENCHA_PACKAGE_LOCAL.length
   *    - SENCHA_RELATIVE_CLASS_PATH.length - 4 (Separator)
   */
  public static final String SENCHA_RELATIVE_CLASS_PATH = "src";
  public static final String SENCHA_RELATIVE_OVERRIDES_PATH = "overrides";
  public static final String SENCHA_RELATIVE_RESOURCES_PATH = "resources";
  public static final String SENCHA_RELATIVE_SASS_PATH = "sass";
  public static final String SENCHA_RELATIVE_SASS_ETC_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "etc";
  public static final String SENCHA_RELATIVE_SASS_SRC_MIXINS_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "mixins";
  public static final String SENCHA_RELATIVE_SASS_SRC_INCLUDES_PATH = SENCHA_RELATIVE_SASS_PATH + SEPARATOR + "src";
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
  public static final String EDITOR_PLUGIN_RESOURCE_FILENAME = "editorPlugins.js";

  public static final Map<String, String> PLACEHOLDERS = ImmutableMap.of( // TODO data structure and location??
          Type.APP, "${app.dir}",
          Type.CODE, "${package.dir}",
          Type.THEME, "${package.dir}",
          Type.WORKSPACE, "${workspace.dir}"
  );

  private final static ObjectMapper objectMapper;
  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }

  private static final Pattern SENCHA_VERSION_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]+){0,3}$");

  public static String getSenchaPackageName(String groupId, String artifactId) {
    return groupId + "__" + artifactId;
  }

  public static String getSenchaVersionForMavenVersion(String version) {
    // Very simple matching for now, maybe needs some adjustment
    version = version.replace("-SNAPSHOT", "").replace("-", ".");
    if (SENCHA_VERSION_PATTERN.matcher(version).matches()) {
      return version;
    } else {
      return null;
    }
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
        return getSenchaPackageName(groupId, artifactId);
      }
    }
    throw new MojoExecutionException("Theme name references an artifact which is not added to dependencies");
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
   * @param packageType the Maven project's packaging type
   * @param relativePath the path relative to the sencha module
   * @return path prefixed with a placeholder and a separator to have an absolute path
   */
  public static String generateAbsolutePathUsingPlaceholder(String packageType, String relativePath) {
    // make sure only normal slashes are used and no backslashes (e.g. on windows systems)
    relativePath = relativePath.replace("\\", "/");
    String result = PLACEHOLDERS.get(packageType);
    if (!StringUtils.isEmpty(relativePath)
            && !relativePath.startsWith(SEPARATOR)) {
      result += SEPARATOR + relativePath;
    }
    return result;
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public static Path getRelativePathFromWorkspaceToWorkingDir(File workingDirectory) throws MojoExecutionException {
    File closestSenchaWorkspaceDir = findClosestSenchaWorkspaceDir(workingDirectory);
    if (null == closestSenchaWorkspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace above workingDirectory");
    }
    Path workspacePath = closestSenchaWorkspaceDir.toPath().normalize();
    Path workingDirectoryPath = workingDirectory.toPath().normalize();

    if (workspacePath.getRoot() == null || !workspacePath.getRoot().equals(workingDirectoryPath.getRoot())) {
      throw new MojoExecutionException("cannot find a relative path from workspace directory to working directory");
    }
    return workspacePath.relativize(workingDirectoryPath);
  }

  public static boolean isActualSenchaDependency(@Nonnull Dependency dependency,
                                                 @Nonnull MavenSenchaConfiguration senchaConfiguration) {
    String remotePackagesArtifact = senchaConfiguration.getRemotePackagesArtifact();
    final String dependencyId = dependency.getManagementKey();

    boolean isExcluded = Iterables.tryFind(senchaConfiguration.getExcludes(), new Predicate<String>() {
      @Override
      public boolean apply(@Nullable String input) {
        return input != null && dependencyId.startsWith(input);
      }
    }).isPresent();

    return !isExcluded
            && !dependencyId.startsWith(remotePackagesArtifact)
            && !dependency.getScope().equals(Artifact.SCOPE_PROVIDED)
            && !dependency.getScope().equals(Artifact.SCOPE_TEST);
  }

}
