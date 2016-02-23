package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.InputStream;
import java.util.Map;

public class DefaultSenchaThemePackageConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaThemePackageConfigurer instance;

  private static final String TYPE = "type";

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    super.configure(config);

    config.put(TYPE, "theme");
  }

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.package.json");
  }

  public static DefaultSenchaThemePackageConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaThemePackageConfigurer();
    }
    return instance;
  }

}
