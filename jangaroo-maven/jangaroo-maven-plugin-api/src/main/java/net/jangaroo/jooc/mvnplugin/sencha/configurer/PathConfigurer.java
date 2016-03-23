package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PathConfigurer implements Configurer {

  static final String OUTPUT = "output";

  static final String CLASSPATH = "classpath";
  static final String OVERRIDES = "overrides";
  static final String RESOURCES = "resources";
  static final String RESOURCES_PATH = "path";
  static final String SASS = "sass";
  static final String SASS_NAMESPACE = "namespace";
  static final String SASS_ETC = "etc";
  static final String SASS_VAR = "var";
  static final String SASS_SRC = "src";

  private SenchaConfiguration senchaConfiguration;

  public PathConfigurer(SenchaConfiguration senchaConfiguration) {
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {

    boolean sassFromSrc = senchaConfiguration.isScssFromSrc();

    // TODO check if this is actually correct for apps - where can we keep the defaults
    config.put(OUTPUT, absolutePath(SenchaUtils.SENCHA_RELATIVE_BUILD_PATH, false));
    config.put(CLASSPATH, absolutePath(SenchaUtils.SENCHA_RELATIVE_CLASS_PATH, false));
    config.put(OVERRIDES, absolutePath(SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH, false));
    List<Object> resources = new ArrayList<Object>();
    // no substitution! as this must be a relative path
    // only apps should use "shared" flag
    configureResourcesForPath(resources, SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH, Type.APP.equals(senchaConfiguration.getType()));
    config.put(RESOURCES, resources);

    Map<String, Object> sass = new HashMap<String, Object>();
    sass.put(SASS_NAMESPACE, "");
    sass.put(SASS_ETC, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_ETC_PATH + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_SASS_ETC_IMPORTS, sassFromSrc));
    sass.put(SASS_VAR, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_VAR_PATH, sassFromSrc));
    sass.put(SASS_SRC, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_SRC_MIXINS_PATH, sassFromSrc) + "," + absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_SRC_INCLUDES_PATH, sassFromSrc));
    config.put(SASS, sass);

  }

  private void configureResourcesForPath(List<Object> resources, String path, boolean shared) {
    Map<String, Object> resource = new LinkedHashMap<String, Object>();
    resource.put(RESOURCES_PATH, path);
    if (shared) {
      resource.put(OUTPUT, "shared");
    }
    resources.add(resource);
  }

  private String absolutePath(String path, boolean fromSrc) {
    String prefix = "";
    if (fromSrc) {
      prefix = getRelativePathFromModuleToSrc() + SenchaUtils.SENCHA_BASE_PATH + SenchaUtils.SEPARATOR;
    }
    return SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), prefix + path);
  }

  private String getRelativePathFromModuleToSrc() {
    // TODO: determine real path!
    String pathDown = ".." + SenchaUtils.SEPARATOR;
    if (Type.APP.equals(senchaConfiguration.getType())) {
      return StringUtils.repeat(pathDown, 2);
    }
    if (Type.CODE.equals(senchaConfiguration.getType())
            || Type.THEME.equals(senchaConfiguration.getType())) {
      return StringUtils.repeat(pathDown, 4);
    }
    return "";
  }
}
