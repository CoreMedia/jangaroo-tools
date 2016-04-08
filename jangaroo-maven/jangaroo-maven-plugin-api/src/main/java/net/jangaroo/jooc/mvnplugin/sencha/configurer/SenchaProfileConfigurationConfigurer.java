package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.sencha.EditorPluginDescriptor;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class SenchaProfileConfigurationConfigurer implements Configurer {

  static final String CSS = "css";
  static final String JS = "js";
  static final String PATH = "path";
  static final String BUNDLE = "bundle";
  static final String INCLUDE_IN_BUNDLE = "includeInBundle";
  static final String BUILD_OUT_CSS_PATH = "${build.out.css.path}";
  static final String EXCLUDE = "exclude";
  static final String FASHION = "fashion";

  private SenchaProfileConfiguration senchaProfileConfiguration;

  public SenchaProfileConfigurationConfigurer(SenchaProfileConfiguration senchaProfileConfiguration) {
    this.senchaProfileConfiguration = senchaProfileConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    @SuppressWarnings("unchecked") // the css attribute is statically defined as a list of objects in the default.app.json
    List<Object> cssFromDefault = (List<Object>)config.get(CSS);
    List<Object> additionalCss = new ArrayList<>();
    if (cssFromDefault != null) {
      additionalCss.addAll(cssFromDefault);
    }
    additionalCss.addAll(getAdditionalResources(
            senchaProfileConfiguration.getAdditionalCssNonBundle(),
            senchaProfileConfiguration.getAdditionalCssIncludeInBundle()));
    if (!additionalCss.isEmpty()) {
      config.put(CSS, additionalCss);
    }

    @SuppressWarnings("unchecked") // the js attribute is statically defined as a list of objects in the default.app.json
    List<Object> jsFromDefault = (List<Object>)config.get(JS);
    List<Object> additionalJs = new ArrayList<>();
    if (jsFromDefault != null) {
      additionalJs.addAll(jsFromDefault);
    }

    additionalJs.addAll(getAdditionalResources(
            senchaProfileConfiguration.getAdditionalJsNonBundle(),
            senchaProfileConfiguration.getAdditionalJsIncludeInBundle()));
    List<? extends EditorPluginDescriptor> editorPlugins = senchaProfileConfiguration.getEditorPlugins();
    if (null != editorPlugins && !editorPlugins.isEmpty()) {
      String profileFolder = "";
      if (null != senchaProfileConfiguration.getProfileName()) {
        profileFolder = senchaProfileConfiguration.getProfileName() + SenchaUtils.SEPARATOR;
      }
      additionalJs.add(getResourceEntry(SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH + SenchaUtils.SEPARATOR + profileFolder + SenchaUtils.EDITOR_PLUGIN_RESOURCE_FILENAME, false, true));
    }
    if (!additionalJs.isEmpty()) {
      config.put(JS, additionalJs);
    }
  }

  private List<Object> getAdditionalResources(List<String> resourcesNonBundle, List<String> resourcesIncludeInBundle) {
    List<Object> resources = new ArrayList<>();

    if (null != resourcesNonBundle) {
      for (String resource : resourcesNonBundle) {
        resources.add(getResourceEntry(resource, false, false));
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
    Map<String, Object> result = new LinkedHashMap<>();

    result.put(PATH, path);
    result.put(BUNDLE, bundle);
    result.put(INCLUDE_IN_BUNDLE, includeInBundle);
    // special case for ant variable specifying the generated css (should not be added if Sencha fashion is active)
    if (BUILD_OUT_CSS_PATH.equals(path)) {
      result.put(EXCLUDE, Collections.singletonList(FASHION));
    }

    return result;
  }

}
