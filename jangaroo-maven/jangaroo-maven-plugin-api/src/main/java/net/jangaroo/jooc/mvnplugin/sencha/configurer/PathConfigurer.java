package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

public class PathConfigurer implements Configurer {

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

    Map<String, Object> sass = new HashMap<>();
    sass.put(SASS_NAMESPACE, "");
    sass.put(SASS_ETC, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_ETC_PATH + SenchaUtils.SEPARATOR + SenchaUtils.SENCHA_SASS_ETC_IMPORTS, sassFromSrc));
    sass.put(SASS_VAR, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_VAR_PATH, sassFromSrc));
    sass.put(SASS_SRC, absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_SRC_MIXINS_PATH, sassFromSrc) + "," + absolutePath(SenchaUtils.SENCHA_RELATIVE_SASS_SRC_INCLUDES_PATH, sassFromSrc));
    config.put(SASS, sass);

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
