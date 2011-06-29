package testNamespace.config {

import ext.ComponentMgr;
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
public class BadConfig2 extends panel {
  /**
   * @see testPackage.TestComponent
   */
  public function BadConfig2(config:Object = null) {
    super(config || {});
  }
}
}