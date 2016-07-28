package package1 {

public class TestTypeCast {

  public function TestTypeCast() {
  }

  public static function testAsCast(p : Object) : TestTypeCast {
    var r :Number = 99.7;
    var n :Number = Number("99.8");
    var i :int = int(r);
    var b :Boolean = p is TestTypeCast;
    return p as TestTypeCast;
  }

  public static function testCastToUint(any:*):uint {
    return uint(any);
  }

  public static function testCastToClassVar(clazz:Class, value:*):* {
    return clazz(value);
  }

}
}
