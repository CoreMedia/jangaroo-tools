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
[ExtConfig(target=1)]
public class BadConfig1 extends panel {
  /**
   * @see testPackage.TestComponent
   */
  public function BadConfig1(config:Object = null) {
    super(config || {});
  }
}
}