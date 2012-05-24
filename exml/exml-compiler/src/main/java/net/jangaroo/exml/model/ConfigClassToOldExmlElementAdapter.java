package net.jangaroo.exml.model;

/**
 * An adapter from config class to EXML element of the old format.
 */
public class ConfigClassToOldExmlElementAdapter extends AbstractConfigClassToExmlElementAdapter {

  public ConfigClassToOldExmlElementAdapter(ConfigClass configClass) {
    super(configClass);
  }

  @Override
  public String getPackage() {
    return configClass.getPackageName();
  }

  @Override
  public String getTypeName() {
    return configClass.getFullName();
  }

  @Override
  public String getName() {
    return configClass.getName();
  }

  @Override
  protected ExmlElement createAdapter(ConfigClass configClass) {
    return new ConfigClassToOldExmlElementAdapter(configClass);
  }

}
