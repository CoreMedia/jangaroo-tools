package testNamespace.config {

import ext.ComponentMgr;
import ext.config.panel;
import testPackage.TestComponent;

/**
 * This is a TestComponent2 with panel as baseclass.
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see testPackage.TestComponent
 */
[ExtConfig(target="testPackage.TestComponent2", xtype)]
public class TestComponent2 extends TestComponent {
  /**
   * @see testPackage.TestComponent2
   */
  public function TestComponent(config:Object = null) {
    super(config || {});
  }

}
}
