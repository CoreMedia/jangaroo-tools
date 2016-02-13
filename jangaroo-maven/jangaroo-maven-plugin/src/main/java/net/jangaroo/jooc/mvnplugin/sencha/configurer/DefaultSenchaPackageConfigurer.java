package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import java.io.InputStream;

public class DefaultSenchaPackageConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaPackageConfigurer instance;

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.package.json");
  }

  public static DefaultSenchaPackageConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaPackageConfigurer();
    }
    return instance;
  }

}
