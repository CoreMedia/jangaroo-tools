package testNamespace.config {

import ext.Panel;
import ext.config.panel;
import testPackage.TestImpl2;

/**
 * This is a TestComponent with panel as baseclass.
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>testPackage.TestComponent</code>.
 * It defines the EXML element <code>&lt;tc:testComponent></code> with <code>xmlns:tc="exml:testNamespace.config"</code>.</p>
 * <p>Using this config class also takes care of registering the target class under the xtype
 * <code>"testNamespace.config.testComponent"</code> with Ext JS.</p>
 *
 * @see testPackage.TestComponent
 * @see ext.Panel
 */
[ExtConfig(target="testPackage.TestComponent", xtype)]
public class testComponent extends ext.config.panel {

  public static native function get xtype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>testPackage.TestComponent</code>.
   * Using this config class also takes care of registering the target class under the xtype
   * "testNamespace.config.testComponent" with Ext JS.
   * </p>
   *
   * @see testPackage.TestComponent
   */
  public function testComponent(config:Object = null) {
    super(config || {});
  }

  /**
   * Some Boolean property @see Boolean
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
  /**
   * Some Array property
   */
  public native function get propertyFive():Array;
  /**
   * @private
   */
  public native function set propertyFive(value:Array):void;
  /**
   * Some Panel
   */
  public native function get propertySix():Panel;
  /**
   * @private
   */
  public native function set propertySix(value:Panel):void;
  /**
   * Some other Panel
   */
  public native function get propertySeven():ext.Panel;
  /**
   * @private
   */
  public native function set propertySeven(value:ext.Panel):void;
  /**
   * Some type without default constructor
   */
  public native function get propertyEight():testPackage.TestImpl2;
  /**
   * @private
   */
  public native function set propertyEight(value:testPackage.TestImpl2):void;
  /**
   * Some object
   */
  public native function get propertyNine():Object;
  /**
   * @private
   */
  public native function set propertyNine(value:Object):void;
}
}
