package package1.someOtherPackage {
import ext.mixin.Observable;

[Event(name="clickClack", type="package1.someOtherPackage.SomeEvent")]
[ExtConfig]
public class SomeOtherClass extends Observable {
  public static const xtype:String = "someotherclass";
  public static const BLA:int = 0;
  private var _bla:int = BLA;

  [Bindable(event="bla_has_changed")]
  public function get bla():int {
    return bla;
  }

  [Bindable]
  public function set bla(value:int):void {
    this.bla = value;
  }

  public var doodle:String;

  public native function get blubbAccessor():String;

  [ExtConfig("blubb_accessor")]
  [Bindable]
  public native function set blubbAccessor(value:String):void;


  public native function get blubbConfig():String;

  [ExtConfig("blubb_config")]
  public native function set blubbConfig(value:String):void;

  [ExtConfig]
  public native function get type():String;

  public native function set type(value:String):void;

  [ExtConfig("__mixins__")]
  public native function set mixins(value:Array):void;

  public function SomeOtherClass(config:Object = null) {
  }
}
}