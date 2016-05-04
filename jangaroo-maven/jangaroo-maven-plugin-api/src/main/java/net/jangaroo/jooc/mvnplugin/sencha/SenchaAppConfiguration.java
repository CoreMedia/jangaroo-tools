package net.jangaroo.jooc.mvnplugin.sencha;

import java.util.List;

public interface SenchaAppConfiguration extends SenchaConfiguration {

  /**
   * Example:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApplication&lt;/applicationClass>
   * </pre>
   * @return the full qualified name of the application class of the Sencha app
   */
  String getApplicationClass();

  /**
   * @return the list of supported locales, with the default locale as the first item in the returned list
   */
  List<String> getLocales();


}
