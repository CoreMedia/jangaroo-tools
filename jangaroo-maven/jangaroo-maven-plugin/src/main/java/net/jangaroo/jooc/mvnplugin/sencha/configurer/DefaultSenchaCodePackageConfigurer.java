package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import java.io.InputStream;

public class DefaultSenchaCodePackageConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaCodePackageConfigurer instance;

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.code.package.json");
  }

  public static DefaultSenchaCodePackageConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaCodePackageConfigurer();
    }
    return instance;
  }

}
