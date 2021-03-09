package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object = {"foo": "bar"};

  [Bindable]
  public var myConfigOption:String = "baz";

  [Bindable]
  public var myConfigOption2:Object = { a: 123 };

  function setMyConfigOption(value:String):void {
    this["myConfigOption"] = value + "!";
  }

  private function setMyConfigOption2(value: Object) {
    this.myConfigOption2 = value;
  }

  public function foo():String {
    setMyConfigOption("direct set call");
    return this.const1 + const2 + const3;
  }
}
}