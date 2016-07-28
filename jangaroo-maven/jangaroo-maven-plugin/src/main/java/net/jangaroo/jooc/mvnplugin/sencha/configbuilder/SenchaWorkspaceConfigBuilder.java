package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for Sencha workspace.json.
 */
public class SenchaWorkspaceConfigBuilder extends SenchaConfigBuilder<SenchaWorkspaceConfigBuilder> {

  private static final String APPS = "apps";
  private static final String PACKAGES = "packages";
  private static final String DIR = "dir";
  private static final String EXTRACT = "extract";

  public SenchaWorkspaceConfigBuilder apps(List<String> paths) {
    return nameValue(APPS, paths);
  }

  public SenchaWorkspaceConfigBuilder packagesDirs(List<String> paths) {
    Map<String, Object> packages = new LinkedHashMap<>();
    packages.put(DIR, paths);
    return nameValue(PACKAGES, packages);
  }

  public SenchaWorkspaceConfigBuilder packagesExtract(String path) {
    Map<String, Object> packages = new LinkedHashMap<>();
    packages.put(EXTRACT, path);
    return nameValue(PACKAGES, packages);
  }
}

