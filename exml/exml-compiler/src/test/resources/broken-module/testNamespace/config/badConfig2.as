package testNamespace.config {

import ext.type.panel;
import testPackage.TestComponent;

/**
 * This is a TestComponent with panel as baseclass.
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see testPackage.TestComponent
 */
[ExtConfig]
public class badConfig2 extends panel {
  /**
   * @see testPackage.TestComponent
   */
  public function badConfig2(config:Object = null) {
    super(config || {});
  }
}
}