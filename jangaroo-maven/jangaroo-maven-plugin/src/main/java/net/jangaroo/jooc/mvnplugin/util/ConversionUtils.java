package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.config.SearchAndReplace;
import net.jangaroo.jooc.mvnplugin.SearchAndReplaceConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConversionUtils {

  private static final String NPM_PACKAGE_NAME = "Joo-Npm-Package-Name";
  private static final String NPM_PACKAGE_VERSION = "Joo-Npm-Package-Version";

  private ConversionUtils() {
    // hiding constructor
  }

  public static List<SearchAndReplace> getSearchAndReplace(List<SearchAndReplaceConfiguration> searchAndReplaceConfigurations) {
    return searchAndReplaceConfigurations.stream()
            .map(config -> new SearchAndReplace(Pattern.compile(config.getSearch()), config.getReplace()))
            .collect(Collectors.toList());
  }

  public static String getNpmPackageName(String groupId, String artifactId, List<SearchAndReplace> npmPackageNameReplacers) {
    String npmPackageName = SenchaUtils.getSenchaPackageName(groupId, artifactId);
    // can be removed as soon as all jangaroo artifacts have a npm package name in their manifest
    if (groupId != null && groupId.startsWith("net.jangaroo")) {
      if (Objects.equals(artifactId, "jangaroo-runtime")) {
        return "@jangaroo/runtime";
      }
      if (Objects.equals(artifactId, "ext-as")) {
        return "@jangaroo/ext-ts";
      }
      return "@jangaroo/" + artifactId;
    }
    for (SearchAndReplace searchAndReplace : npmPackageNameReplacers) {
      Matcher matcher = searchAndReplace.search.matcher(npmPackageName);
      if (matcher.matches()) {
        return matcher.replaceAll(searchAndReplace.replace);
      }
    }
    return npmPackageName;
  }

  public static String getNpmPackageVersion(String version) {
    String[] versionParts = version.split("-", 2);
    List<String> majorMinorPatch = new ArrayList<>(Arrays.asList(versionParts[0].split("[.]")));
    if (majorMinorPatch.size() > 3) {
      majorMinorPatch = majorMinorPatch.subList(0, 2);
    } else {
      while (majorMinorPatch.size() < 3) {
        majorMinorPatch.add("0");
      }
    }
    versionParts[0] = StringUtils.join(majorMinorPatch.toArray(), ".");
    return StringUtils.join(versionParts, "-");
  }

  public static Map<String, String> getManifestEntries(String npmPackageName, String npmPackageVersion) {
    Map<String, String> manifestEntries = new LinkedHashMap<>();
    manifestEntries.put(NPM_PACKAGE_NAME, npmPackageName);
    manifestEntries.put(NPM_PACKAGE_VERSION, npmPackageVersion);
    return manifestEntries;
  }

  public static NpmPackageMetadata getNpmPackageMetadataFromManifestEntries(Map<String, String> manifestEntries) {
    String packageName = manifestEntries.get(NPM_PACKAGE_NAME);
    String packageVersion = manifestEntries.get(NPM_PACKAGE_VERSION);
    return packageName != null && packageVersion != null ? new NpmPackageMetadata(packageName, packageVersion) : null;
  }

  public static class NpmPackageMetadata {
    public final String name;
    public final String version;

    private NpmPackageMetadata(String name, String version) {
      this.name = name;
      this.version = version;
    }
  }
}
