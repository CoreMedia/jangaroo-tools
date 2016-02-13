package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PathConfigurer implements Configurer {

  static final String PATH = "path";
  static final String OUTPUT = "output";
  static final String CLASSPATH = "classpath";
  static final String OVERRIDES = "overrides";
  static final String RESOURCES = "resources";
  static final String APPS = "apps";

  public static final String BUILD = "build";
  public static final String DIR = "dir";

  SenchaConfiguration senchaConfiguration;

  public PathConfigurer(SenchaConfiguration senchaConfiguration) {
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    if (!senchaConfiguration.getType().equals(SenchaConfiguration.Type.WORKSPACE)) {
      config.put(OUTPUT, absolutePath(senchaConfiguration.getBuildDir()));
      config.put(CLASSPATH, absolutePath(SenchaUtils.SENCHA_RELATIVE_CLASS_PATH));
      config.put(OVERRIDES, absolutePath(SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH));
      List<Object> resources = new ArrayList<Object>();
      // no substitution! as this must be a relative path
      configureResourcesForPath(resources, SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH, true);
      config.put(RESOURCES, resources);
    } else {
      config.put(APPS, new ArrayList<Object>());
      Map<String, Object> output = new LinkedHashMap<String, Object>();
      output.put(DIR, absolutePath(senchaConfiguration.getBuildDir()));
      config.put(BUILD, output);
    }
  }

  private String absolutePath(String path) {
    return SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), path);
  }

  private void configureResourcesForPath(List<Object> resources, String path, boolean shared) {
    Map<String, Object> resource = new LinkedHashMap<String, Object>();
    resource.put(PATH, path);
    if (shared) {
      resource.put(OUTPUT, "shared");
    }
    resources.add(resource);
  }
}
