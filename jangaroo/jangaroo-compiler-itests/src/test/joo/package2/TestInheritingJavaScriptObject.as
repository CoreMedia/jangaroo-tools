package package2 {

public class TestInheritingJavaScriptObject extends TestJavaScriptObject {

  public function TestInheritingJavaScriptObject(foo:String, bar:int) {
    super(foo);
    this.bar = bar;
  }

  public native function get bar():int;

  public native function set bar(value:int);
}
}