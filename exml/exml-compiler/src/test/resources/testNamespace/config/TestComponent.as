package testNamespace.config {

import ext.ComponentMgr;
import ext.config.panel;
import testPackage.TestComponent;

//Do not edit. This is an auto-generated class.

/**
 * This is a TestComponent with panel as baseclass.
 *
 * This class serves as a typed config object for constructor of class testPackage.TestComponent.
 *
 * @see testPackage.TestComponent
 */
[ExtConfig(target="testPackage.TestComponent")]
public class TestComponent extends ext.config.panel {
  /**
   * @see testPackage.TestComponent
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
}
}