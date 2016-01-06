package testNamespace.config {

import ext.config.action;

/**
 * This is a test action (not a component!).
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>testPackage.TestAction</code>.
 * It defines the EXML element <code>&lt;tc:testAction></code> with <code>xmlns:tc="exml:testNamespace.config"</code>.</p>
 *
 * @see testPackage.TestAction
 */
[ExtConfig(target="testPackage.TestAction")]
public class testAction extends ext.config.action {

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>testPackage.TestAction</code>.
   * </p>
   *
   * @see testPackage.TestAction
   */
  public function testAction(config:Object = null) {
    super(config || {});
  }

  /**
   * Some flag for the action.
   */
  public native function get active():Boolean;
  /**
   * @private
   */
  public native function set active(value:Boolean):void;
}
}
