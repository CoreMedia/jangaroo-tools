package package1 {

public class TestHelperClasses {

  public static const TEXT:String = "foo";
  public static function getText():String {
    var thc:Helper = new Helper("foo");
    var f:Function = thc.getText;
    return f();
  }

}
}

import package1.TestHelperClasses;

class Helper {

  private var text:String = TestHelperClasses.TEXT;

  public function Helper(text:String) {
    this.text = text;
  }

  public function getText():String {
    var f:Function = text_getter;
    f = this.text_getter;
    return f();
  }

  private function text_getter():String {
    return this.text;
  }
}