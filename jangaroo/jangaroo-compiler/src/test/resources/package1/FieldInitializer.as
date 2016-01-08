package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object = {"foo": "bar"};

  [Bindable]
  public var myConfigOption:String = "baz";

  [Bindable]
  public var myConfigOption2:Object = { a: 123 };

  public function foo():String {
    return this.const1 + const2 + const3;
  }
}
}