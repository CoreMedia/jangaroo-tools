package net.jangaroo.jooc.mvnplugin.sencha;

public interface SenchaAppConfiguration extends SenchaConfiguration {

  /**
   * Example:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApplication&lt;/applicationClass>
   * </pre>
   * @return the full qualified name of the application class of the Sencha app
   */
  String getApplicationClass();
  
}
