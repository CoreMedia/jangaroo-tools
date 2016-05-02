package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

/**
 * Builder for app.json.
 */
public class SenchaAppConfigBuilder extends SenchaPackageOrAppConfigBuilder<SenchaAppConfigBuilder> {

  private static final String ID = "id";

  public SenchaAppConfigBuilder id(String id) {
    return nameValue(ID, id);
  }
}
