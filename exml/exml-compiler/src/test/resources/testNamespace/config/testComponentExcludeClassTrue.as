package testNamespace.config {

import ext.config.panel;
import testPackage.TestComponentExcludeClassTrue;

// Do not edit. This is an auto-generated class.

/**
 * This is a TestComponent with excludeClass.
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>testPackage.TestComponentExcludeClassTrue</code>.
 * It defines the EXML element <code>&lt;tc:testComponentExcludeClassTrue></code> with <code>xmlns:tc="exml:testNamespace.config"</code>.</p>
 * <p>Using this config class also takes care of registering the target class under the xtype
 * <code>"testNamespace.config.testComponentExcludeClassTrue"</code> with Ext JS.</p>
 *
 * @see testPackage.TestComponentExcludeClassTrue
 */
[ExtConfig(target="testPackage.TestComponentExcludeClassTrue", xtype)]
[ExcludeClass]
public dynamic class testComponentExcludeClassTrue extends ext.config.panel {

  public static native function get xtype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>testPackage.TestComponentExcludeClassTrue</code>.
   * Using this config class also takes care of registering the target class under the xtype
   * "testNamespace.config.testComponentExcludeClassTrue" with Ext JS.
   * </p>
   *
   * @see testPackage.TestComponentExcludeClassTrue
   */
  public function testComponentExcludeClassTrue(config:Object = null) {
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
