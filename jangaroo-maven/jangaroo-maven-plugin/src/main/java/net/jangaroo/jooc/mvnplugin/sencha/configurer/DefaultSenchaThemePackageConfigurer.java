package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import java.io.InputStream;

public class DefaultSenchaThemePackageConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaThemePackageConfigurer instance;

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.theme.package.json");
  }

  public static DefaultSenchaThemePackageConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaThemePackageConfigurer();
    }
    return instance;
  }

}
