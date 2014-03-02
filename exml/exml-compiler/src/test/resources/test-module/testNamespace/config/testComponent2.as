package testNamespace.config {

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
public class testComponent2 extends testComponent {
  /**
   * @see testPackage.TestComponent2
   */
  public function testComponent2(config:Object = null) {
    super(config || {});
  }

}
}
