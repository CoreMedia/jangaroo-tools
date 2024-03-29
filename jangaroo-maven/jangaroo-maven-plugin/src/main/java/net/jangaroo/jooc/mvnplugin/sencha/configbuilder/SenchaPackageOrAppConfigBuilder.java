package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for builders for package.json or app.json.
 */
public class SenchaPackageOrAppConfigBuilder<T extends SenchaPackageOrAppConfigBuilder> extends SenchaConfigBuilder<T> {

  public static final String CSS = "css";
  public static final String JS = "js";
  public static final String SASS = "sass";

  private static final String NAME = "name";
  private static final String VERSION = "version";
  private static final String NAMESPACE = "namespace";
  private static final String CREATOR = "creator";
  private static final String SUMMARY = "summary";
  protected static final String TYPE = "type";
  private static final String TOOLKIT = "toolkit";
  private static final String THEME = "theme";
  private static final String REQUIRES = "requires";
  private static final String RESOURCES = "resources";
  private static final String RESOURCE_PATH = "path";

  static final String PATH = "path";
  static final String BUNDLE = "bundle";
  static final String INCLUDE_IN_BUNDLE = "includeInBundle";
  static final String BUILD_OUT_CSS_PATH = "${build.out.css.path}";
  static final String EXCLUDE = "exclude";
  static final String FASHION = "fashion";

  /**
   * Specifies the Sencha package or app name.
   */
  public T name(String name) {
    return nameValue(NAME, name);
  }

  /**
   * Specifies the Sencha package or app version.
   */
  public T version(String version) {
    return nameValue(VERSION, version);
  }

  public T namespace(String namespace) {
    return nameValue(NAMESPACE, namespace);
  }

  public T sassNamespace(String sassNamespace) {
    return addToSass(NAMESPACE, sassNamespace);
  }

  /**
   * Specifies the Sencha package or app version.
   */
  public T creator(String creator) {
    return nameValue(CREATOR, creator);
  }

  /**
   * Specifies the Sencha package or app version.
   */
  public T summary(String summary) {
    return nameValue(SUMMARY, summary);
  }

  /**
   * Specifies the Sencha type generated by the plugin.
   */
  public T type(String type) {
    return nameValue(TYPE, type);
  }

  /**
   * Specifies the toolkit to use.
   */
  public T toolkit(String toolkit) {
    return nameValue(TOOLKIT, toolkit);
  }

  /**
   * Defines the theme package to be extended or to be used by the app or packages.
   *
   * When a ":" is found in the provided {@link String} it is assumed that a  maven dependency is used as theme and
   * converted properly.
   */
  public T theme(String theme) {
    return nameValue(THEME, theme);
  }

  public T require(String require) {
    return addToList(ImmutableMap.of(NAME, require), REQUIRES);
  }
    /**
     * Build-profile-specific configuration.
     */
  public T profile(String profile, Map<String, Object> configuration) {
    return nameValue(profile, configuration);
  }

  public T resource(String path) {
    Map<String, String> resource = new LinkedHashMap<>();
    fillResource(path, resource);
    return addToList(resource, RESOURCES);
  }

  void fillResource(String path, Map<String, String> resource) {
    resource.put(RESOURCE_PATH, path);
  }

  public T css(String path, boolean bundle, boolean includeInBundle) {
    return cssOrJs(CSS, path, bundle, includeInBundle);
  }

  public T js(String path, boolean bundle, boolean includeInBundle) {
    return cssOrJs(JS, path, bundle, includeInBundle);
  }

  public T cssOrJs(String cssOrJs, String path, boolean bundle, boolean includeInBundle) {
    return addToList(getResourceEntry(path, bundle, includeInBundle), cssOrJs);
  }

  public T addToSass(String property, String value) {
    return nameValue(SASS, ImmutableMap.of(property, value));
  }

  private static Map<String, Object> getResourceEntry(String path, boolean bundle, boolean includeInBundle) {
    Map<String, Object> result = new LinkedHashMap<>();

    result.put(PATH, path);
    result.put(BUNDLE, bundle);
    result.put(INCLUDE_IN_BUNDLE, includeInBundle);
    // special case for ant variable specifying the generated css (should not be added if Sencha fashion is active)
    if (BUILD_OUT_CSS_PATH.equals(path)) {
      result.put(EXCLUDE, Collections.singletonList(FASHION));
    }

    return Collections.unmodifiableMap(result);
  }

}
