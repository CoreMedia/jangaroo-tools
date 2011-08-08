package ext.config {

/**
 * An ActionScript class that is not a config class.
 */
public class NonConfig {
  /**
   * @see testPackage.TestComponent
   */
  public function NonConfig(config:Object = null) {
    super(config || {});
  }
}
}