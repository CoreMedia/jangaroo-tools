package net.jangaroo.jooc.mvnplugin.sencha;

public interface SenchaConfiguration extends SenchaProfileConfiguration {

  /**
   * Specifies the Sencha type generated by the plugin.
   */
  String getType();

  /**
   * The toolkit to use
   */
  String getToolkit();

  /**
   * Defines the theme package to be extended or to be used by the app or packages.
   *
   * When a ":" is found in the provided {@link String} it is assumed that a  maven dependency is used as theme and
   * converted properly.
   */
  String getTheme();

  /**
   * Production only configuration
   */
  SenchaProfileConfiguration getProduction();

  /**
   * Testing only configuration.
   */
  SenchaProfileConfiguration getTesting();

  /**
   * Development only configuration
   */
  SenchaProfileConfiguration getDevelopment();

  /**
   * Defines the Ext Framework directory of the workspace
   */
  String getExtFrameworkDir();

  /**
   * Packages directory relative to baseDir
   */
  String getPackagesDir();

  void setExtFrameworkDir(String extFrameworkDir);

  void setPackagesDir(String packagesDir);

}