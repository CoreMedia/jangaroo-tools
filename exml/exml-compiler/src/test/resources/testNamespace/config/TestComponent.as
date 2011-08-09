package testNamespace.config {

import ext.ComponentMgr;
import ext.config.panel;
import testPackage.TestComponent;

// Do not edit. This is an auto-generated class.

/**
 * This is a TestComponent with panel as baseclass.
 *
 * <p>
 * <b>Do not instantiate this class!</b> Instead, instantiate the associated
 * component class testPackage.TestComponent directly.
 * This class is only provided to document the config attributes
 * to use when building instances of the component class.
 * </p>
 *
 * @see testPackage.TestComponent
 */
[ExtConfig(target="testPackage.TestComponent", xtype)]
public dynamic class TestComponent extends ext.config.panel {

  public static native function get xtype():String;

  /**
   * @private
   */
  public function TestComponent(config:Object = null) {
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
  /**
   * Some &#42; property
   */
  public native function get propertyFour():*;
  /**
   * @private
   */
  public native function set propertyFour(value:*):void;
}
}
