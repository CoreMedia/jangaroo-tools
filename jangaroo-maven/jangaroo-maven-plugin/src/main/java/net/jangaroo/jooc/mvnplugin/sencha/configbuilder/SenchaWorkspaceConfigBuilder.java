package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder for Sencha workspace.json.
 */
public class SenchaWorkspaceConfigBuilder extends SenchaConfigBuilder<SenchaWorkspaceConfigBuilder> {

  private static final String APPS = "apps";
  private static final String PACKAGES = "packages";
  private static final String DIR = "dir";
  private static final String EXTRACT = "extract";

  public SenchaWorkspaceConfigBuilder app(String path) {
    return addToList(path, APPS);
  }

  public SenchaWorkspaceConfigBuilder packagesDir(String path) {
    return addToList(path, PACKAGES, DIR);
  }

  public SenchaWorkspaceConfigBuilder packagesExtract(String path) {
    @SuppressWarnings("unchecked")
    Map<String, Object> packages = (Map<String, Object>) config.get(PACKAGES);
    if (packages == null) {
      packages = new LinkedHashMap<>();
    }
    packages.put(EXTRACT, path);
    return nameValue(PACKAGES, packages);
  }
}

