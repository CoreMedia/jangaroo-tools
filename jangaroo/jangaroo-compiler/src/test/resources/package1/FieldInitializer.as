package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object = {"foo": "bar"};

  public function foo():String {
    return this.const1 + const2 + const3;
  }
}
}