package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import java.io.InputStream;

public class DefaultSenchaWorkspaceConfigurer extends AbstractJsonInputStreamConfigurer {

  private static DefaultSenchaWorkspaceConfigurer instance;

  @Override
  protected InputStream getInputStream() {
    return getClass().getResourceAsStream("default.workspace.json");
  }

  public static DefaultSenchaWorkspaceConfigurer getInstance() {
    if (null == instance) {
      instance = new DefaultSenchaWorkspaceConfigurer();
    }
    return instance;
  }

}
