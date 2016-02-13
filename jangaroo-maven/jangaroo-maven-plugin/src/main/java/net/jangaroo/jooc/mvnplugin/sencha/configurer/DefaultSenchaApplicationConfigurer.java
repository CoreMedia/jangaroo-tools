package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class DefaultSenchaApplicationConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaApplicationConfigurer instance;

  static final String ID = "id";

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    super.configure(config);

    config.put(ID, UUID.randomUUID().toString());
  }

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.app.json");
  }

  public static DefaultSenchaApplicationConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaApplicationConfigurer();
    }
    return instance;
  }
}
