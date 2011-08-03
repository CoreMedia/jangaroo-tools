package package2 {

import joo.JavaScriptObject;

public class TestJavaScriptObject extends JavaScriptObject {

  public function TestJavaScriptObject(foo:String) {
    this.foo = foo;
  }

  public function allProperties():String {
    var allProps:Array = [];
    for (var m:String in this) {
      allProps.push(m);
    }
    allProps.sort();
    return allProps.join(",");
  }

  public native function get foo():String;

  public native function set foo(value:String);
}
}