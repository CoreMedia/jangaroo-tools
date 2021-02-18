package testPackage {

/**
 * some comment
 */
[PublicApi]
[Native(require)]
public class PropertiesTest_properties {
  /**
   * Singleton for the current user Locale's instance of ResourceBundle "PropertiesTest".
   * @see PropertiesTest_properties
   */
  public static const INSTANCE:testPackage.PropertiesTest_properties;

  public native function get key():String;

  public native function get key2():String;

  public native function get key3():String;
}
}