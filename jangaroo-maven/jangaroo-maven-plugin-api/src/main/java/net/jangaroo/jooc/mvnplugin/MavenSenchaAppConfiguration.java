package net.jangaroo.jooc.mvnplugin;

public interface MavenSenchaAppConfiguration extends MavenSenchaConfiguration {

  /**
   * Example:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApllication&lt;/applicationClass>
   * </pre>
   * @return the full qualified name of the application class of the Sencha app
   */
  String getApplicationClass();
  
}
