package package1.someOtherPackage {

[Event(name="clack", type="package1.someOtherPackage.SomeEvent")]
public class SomeOtherClass {
  public static const BLA:int = 0;
  private var _bla:int = BLA;

  [Accessor("get_bla")]
  [Bindable(event="bla_has_changed")]
  public function get bla():int {
    return bla;
  }

  [Accessor("set_bla")]
  public function set bla(value:int):void {
    this.bla = value;
  }

  public var doodle:String;

  public native function get blubbAccessor():String;

  [ConstructorParameter("blubb_accessor")]
  [Accessor]
  public native function set blubbAccessor(value:String):void;


  public native function get blubbConfig():String;

  [ConstructorParameter("blubb_config")]
  public native function set blubbConfig(value:String):void;

  [ConstructorParameter(value="someOtherType")]
  public native function get type():String;

  public native function set type(value:String):void;

  public function SomeOtherClass(config:Object = null) {
  }
}
}