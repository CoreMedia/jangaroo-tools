package testPackage {

[PublicApi]
[Rename("testPackage.PropertiesTest_properties")]
/**
 * Some class comment in a properties file.
 * @see PropertiesTest_properties#INSTANCE
 */
public class PropertiesTest_properties {
  /**
   * Singleton for the current user Locale's instance of ResourceBundle "PropertiesTest".
   * @see PropertiesTest_properties
   */
  public static const INSTANCE:testPackage.PropertiesTest_properties;

  /**
   * Documentation for 'key'.
   */
  public var key:String;

  /**
   * Documentation for 'key2'.
   */
  public var key2:String;

  public var key3:String;

  public function PropertiesTest_properties() {
    super();
  }
}
}