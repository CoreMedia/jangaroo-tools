package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import java.util.Map;

/**
 * Builder for package.json.
 */
public class SenchaPackageConfigBuilder extends SenchaPackageOrAppConfigBuilder<SenchaPackageConfigBuilder> {

  static final String EXTEND = "extend";
  static final String RESOURCE_OUTPUT = "output";
  private static final String RESOURCE_OUTPUT_SHARED = "shared";

  private boolean shareResources;

  public SenchaPackageConfigBuilder extend(String theme) {
    return nameValue(EXTEND, theme);
  }

  /**
   * For "code" packages:
   * Affects the way resources are copied in universal apps as described in
   * "https://docs.sencha.com/cmd/6.x/resource_management.html".
   *
   * For "theme" packages:
   * Affects the way resources are copied in the resources-sandbox of the application as described in
   * "https://docs.sencha.com/cmd/6.x/resource_management.html".
   */
  public SenchaPackageConfigBuilder shareResources() {
    shareResources = true;
    return this;
  }

  @Override
  void fillResource(String path, Map<String, String> resource) {
    if (shareResources) {
      resource.put(RESOURCE_OUTPUT, RESOURCE_OUTPUT_SHARED);
    }
    super.fillResource(path, resource);
  }

}
