package net.jangaroo.exml.model;

/**
 * An adapter from config class to EXML element of the new format.
 */
public class ConfigClassToNewExmlElementAdapter extends AbstractConfigClassToExmlElementAdapter {

  public ConfigClassToNewExmlElementAdapter(ConfigClass configClass) {
    super(configClass);
  }

  @Override
  public String getPackage() {
    return configClass.getTargetClassPackage();
  }

  @Override
  public String getTypeName() {
    return getName() + "_TYPE";
  }

  @Override
  public String getName() {
    return configClass.getTargetClassShortName();
  }

  @Override
  protected ExmlElement createAdapter(ConfigClass configClass) {
    return new ConfigClassToNewExmlElementAdapter(configClass);
  }

}
