package package1.someOtherPackage {
import ext.mixin.Observable;

/**
 * @eventType package1.someOtherPackage.SomeEvent.CLICK_CLACK
 */
[Event(name="onClickClack", type="package1.someOtherPackage.SomeEvent")]
[ExtConfig]
public class SomeOtherClass extends Observable {
  public static const xtype:String = "someotherclass";
  public static const BLA:Number = 0;
  private var _bla:Number = BLA;

  [Bindable(event="bla_has_changed")]
  public function get bla():Number {
    return bla;
  }

  [Bindable]
  public function set bla(value:Number):void {
    this.bla = value;
  }

  public var doodle:String;

  public var blubb_accessor:String;

  public native function get blubbAccessor():String;

  [ExtConfig("blubb_accessor")]
  [Bindable]
  public native function set blubbAccessor(value:String):void;

  public var blubb_config: String;

  public native function get blubbConfig():String;

  [ExtConfig("blubb_config")]
  public native function set blubbConfig(value:String):void;

  [ExtConfig]
  public native function get type():String;

  public native function set type(value:String):void;

  [ExtConfig("__mixins__")]
  public native function set mixins(value:Array):void;

  public function SomeOtherClass(config:SomeOtherClass = null) {
  }
}
}