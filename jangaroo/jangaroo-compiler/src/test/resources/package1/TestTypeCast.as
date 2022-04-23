package package1 {
import package1.mxml.pkg.TestInterface;

public class TestTypeCast {

  public function TestTypeCast() {
  }

  public static function testAsCast(p : Object) : TestTypeCast {
    var r :Number = 99.7;
    var n :Number = Number("99.8");
    var i :int = int(r);
    var u :uint = n; // coercion from Number to uint
    var b :Boolean = p is TestTypeCast;
    var notB :Boolean = !(p is TestTypeCast);
    var castObjectToInterface: TestInterface = TestInterface({ foo: "FOO" });
    var castObjectToNonExt: SomeClass = SomeClass({ bar: "BAR" });
    var useAsWithDotExpr: String = (p as TestTypeCast).toString();
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
