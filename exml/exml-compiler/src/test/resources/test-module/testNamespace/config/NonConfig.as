package testNamespace.config {

import ext.type.panel;
import testPackage.TestComponent;

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