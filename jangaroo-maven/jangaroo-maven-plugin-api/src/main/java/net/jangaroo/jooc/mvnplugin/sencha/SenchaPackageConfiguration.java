package net.jangaroo.jooc.mvnplugin.sencha;

public interface SenchaPackageConfiguration extends SenchaConfiguration {


  /**
   * Only applies to "code" packages.
   *
   * Affects the way resources are copied in universal apps as described in
   * "https://docs.sencha.com/cmd/6.x/resource_management.html".
   *
   * For "theme" packages the behaviour cannot be changed and will always be "false".
   */
  boolean isShareResources();

  /**
   * Only applies to "theme" packages.
   *
   * Affects the way resources are copied in the resources-sandbox of the application as described in
   * "https://docs.sencha.com/cmd/6.x/resource_management.html".
   *
   * For "code" packages the behaviour cannot be changed and will always be "true".
   */
  boolean isIsolateResources();
  
}
