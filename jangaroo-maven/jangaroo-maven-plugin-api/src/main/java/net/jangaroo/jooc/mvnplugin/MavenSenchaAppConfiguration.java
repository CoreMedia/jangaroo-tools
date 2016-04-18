package net.jangaroo.jooc.mvnplugin;

import java.util.List;

public interface MavenSenchaAppConfiguration extends MavenSenchaConfiguration {

  /**
   * Example:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApllication&lt;/applicationClass>
   * </pre>
   * @return the full qualified name of the application class of the Sencha app
   */
  String getApplicationClass();

  /**
   * @return the list of supported locales, with the default locale as the first item in the returned list
   */
  List<String> getLocales();

}
