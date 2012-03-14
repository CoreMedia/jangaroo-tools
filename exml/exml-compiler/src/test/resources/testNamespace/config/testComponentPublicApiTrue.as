package testNamespace.config {

import ext.config.panel;

// Do not edit. This is an auto-generated class.

/**
 * This is a TestComponent with publicApi.
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>testPackage.TestComponentPublicApiTrue</code>.
 * It defines the EXML element <code>&lt;tc:testComponentPublicApiTrue></code> with <code>xmlns:tc="exml:testNamespace.config"</code>.</p>
 * <p>Using this config class also takes care of registering the target class under the xtype
 * <code>"testNamespace.config.testComponentPublicApiTrue"</code> with Ext JS.</p>
 *
 * @see testPackage.TestComponentPublicApiTrue
 */
[ExtConfig(target="testPackage.TestComponentPublicApiTrue", xtype)]
[PublicApi]
public dynamic class testComponentPublicApiTrue extends ext.config.panel {

  public static native function get xtype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>testPackage.TestComponentPublicApiTrue</code>.
   * Using this config class also takes care of registering the target class under the xtype
   * "testNamespace.config.testComponentPublicApiTrue" with Ext JS.
   * </p>
   *
   * @see testPackage.TestComponentPublicApiTrue
   */
  public function testComponentPublicApiTrue(config:Object = null) {
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
