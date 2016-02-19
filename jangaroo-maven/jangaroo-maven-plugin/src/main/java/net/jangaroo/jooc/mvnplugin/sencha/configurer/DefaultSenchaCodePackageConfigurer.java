package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.InputStream;
import java.util.Map;

public class DefaultSenchaCodePackageConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaCodePackageConfigurer instance;

  private static final String TYPE = "type";

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    super.configure(config);

    config.put(TYPE, "code");
  }

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.package.json");
  }

  public static DefaultSenchaCodePackageConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaCodePackageConfigurer();
    }
    return instance;
  }

}
