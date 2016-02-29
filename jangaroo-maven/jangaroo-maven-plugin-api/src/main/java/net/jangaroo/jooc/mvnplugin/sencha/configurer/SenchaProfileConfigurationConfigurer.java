package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.SenchaProfileConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class SenchaProfileConfigurationConfigurer implements Configurer {

  static final String CSS = "css";
  static final String JS = "js";
  static final String PATH = "path";
  static final String BUNDLE = "bundle";
  static final String INCLUDE_IN_BUNDLE = "includeInBundle";

  private SenchaProfileConfiguration senchaProfileConfiguration;

  public SenchaProfileConfigurationConfigurer(SenchaProfileConfiguration senchaProfileConfiguration) {
    this.senchaProfileConfiguration = senchaProfileConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(CSS, getAdditionalResources(senchaProfileConfiguration.getAdditionalCssNonBundle(), senchaProfileConfiguration.getAdditionalCssBundle(), senchaProfileConfiguration.getAdditionalCssIncludeInBundle()));
    config.put(JS, getAdditionalResources(senchaProfileConfiguration.getAdditionalJsNonBundle(), senchaProfileConfiguration.getAdditionalJsBundle(), senchaProfileConfiguration.getAdditionalJsIncludeInBundle()));
  }

  private List<Object> getAdditionalResources(List<String> resourcesNonBundle, List<String> resourcesBundle, List<String> resourcesIncludeInBundle) {
    List<Object> resources = new ArrayList<Object>();

    if (null != resourcesNonBundle) {
      for (String resource : resourcesNonBundle) {
        resources.add(getResourceEntry(resource, false, false));
      }
    }
    if (null != resourcesBundle) {
      for (String resource : resourcesBundle) {
        resources.add(getResourceEntry(resource, true, false));
      }
    }
    if (null != resourcesIncludeInBundle) {
      for (String resource : resourcesIncludeInBundle) {
        resources.add(getResourceEntry(resource, false, true));
      }
    }

    return resources;
  }

  private Map<String, Object> getResourceEntry(String path, boolean bundle, boolean includeInBundle) {
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    result.put(PATH, path);
    result.put(BUNDLE, bundle);
    result.put(INCLUDE_IN_BUNDLE, includeInBundle);

    return result;
  }

}
