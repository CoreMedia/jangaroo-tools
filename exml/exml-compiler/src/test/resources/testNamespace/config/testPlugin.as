package testNamespace.config {

import ext.ComponentMgr;
import ext.config.plugin;
import testPackage.TestPlugin;

// Do not edit. This is an auto-generated class.

/**
 * This is a test plugin (not a component!).
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>testPackage.TestPlugin</code>.
 * Using this config class also takes care of registering the target class under the ptype
 * "testNamespace.config.testPlugin" with Ext JS.</p>
 *
 * @see testPackage.TestPlugin
 */
[ExtConfig(target="testPackage.TestPlugin", ptype)]
public dynamic class testPlugin extends ext.config.plugin {

  public static native function get ptype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>testPackage.TestPlugin</code>.
   * Using this config class also takes care of registering the target class under the ptype
   * "testNamespace.config.testPlugin" with Ext JS.</p>
   * </p>
   *
   * @see testPackage.TestPlugin
   */
  public function testPlugin(config:Object = null) {
    super(config || {});
  }

}
}
