package net.jangaroo.jooc.mvnplugin.sencha;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.util.Map;
import java.util.regex.Pattern;

public class SenchaUtils {

  public static final String SEPARATOR = "/";

  public static final String SENCHA_BASE_PATH = "sencha";
  public static final String SENCHA_PACKAGES_LOCAL = "local";
  public static final String SENCHA_PACKAGES_REMOTE = "remote";

  public static final String SENCHA_RELATIVE_CLASS_PATH = "src";
  public static final String SENCHA_RELATIVE_OVERRIDES_PATH = "overrides";
  public static final String SENCHA_RELATIVE_RESOURCES_PATH = "resources";

  public static final String SENCHA_DIRECTORYNAME = ".sencha";
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

  private static Pattern SENCHA_VERSION_PATTERN = Pattern.compile("^[0-9]+[\\.[0-9]+]{0,3}$");

  private static String getSenchaPackageName(String groupId, String artifactId) {
    return groupId + "__" + artifactId;
  }

  public static String getSenchaPackageNameForMavenProject(MavenProject project) {
    return getSenchaPackageName(project.getGroupId(), project.getArtifactId());
  }

  public static String getSenchaPackageNameForArtifact(Artifact artifact) {
    return getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId());
  }

  public static String getSenchaVersionForProject(MavenProject project) {
    String version = project.getVersion().replace("-SNAPSHOT", "");
    if (SENCHA_VERSION_PATTERN.matcher(version).matches()) {
      return version;
    } else {
      return "1.0.0";
    }
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

}
