package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

/**
 * Builder for app.json.
 */
public class SenchaAppConfigBuilder extends SenchaPackageOrAppConfigBuilder<SenchaAppConfigBuilder> {

  private static final String ID = "id";
  private static final String LOCALES_LABEL = "locales";

  public SenchaAppConfigBuilder id(String id) {
    return nameValue(ID, id);
  }

  public SenchaAppConfigBuilder locale(String locale) {
    return addToList(locale, LOCALES_LABEL);
  }

}
