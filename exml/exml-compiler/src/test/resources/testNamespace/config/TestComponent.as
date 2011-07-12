package testNamespace.config {

import ext.ComponentMgr;
import ext.config.panel;
import testPackage.TestComponent;

// Do not edit. This is an auto-generated class.

/**
 * This is a TestComponent with panel as baseclass.
 *
 * <p>
 * Do not instantiate this class! Instead, instantiate the associated
 * component class testPackage.TestComponent directly.
 * This class is only provided to document the config attributes
 * to use when building instances of the component class.
 * </p>
 *
 * @see testPackage.TestComponent
 */
[ExtConfig(target="testPackage.TestComponent")]
public class TestComponent extends ext.config.panel {
  /**
   * @private
   */
  public function TestComponent(config:Object = null) {
    throw new Error("do not instantiate the config class testNamespace.config.TestComponent; " +
      "instantiate the component class testPackage.TestComponent instead");
    super(config || {});
  }

  /**
   * Some Boolean property
   */
  public native function get propertyOne():Boolean;
  /**
   * @private
   */
  public native function set propertyOne(value:Boolean):void;
  /**
   * Some Number property
   */
  public native function get propertyTwo():Number;
  /**
   * @private
   */
  public native function set propertyTwo(value:Number):void;
  /**
   * Some String property
   */
  public native function get propertyThree():String;
  /**
   * @private
   */
  public native function set propertyThree(value:String):void;
}
}