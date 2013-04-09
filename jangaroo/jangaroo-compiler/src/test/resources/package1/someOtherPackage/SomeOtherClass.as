package package1.someOtherPackage {
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

  public function SomeOtherClass(config:Object = null) {
  }
}
}