/*package package1 {
import package1.mxml.pkg.TestInterface;*/

Ext.define("package1.TestTypeCast", function(TestTypeCast) {/*public class TestTypeCast {

  public*/ function TestTypeCast$() {
  }/*

  public static*/ function testAsCast$static(p/* : Object*/)/* : TestTypeCast*/ {
    var r/* :Number*/ = 99.7;
    var n/* :Number*/ = Number("99.8");
    var i/* :int*/ = AS3.int_(r);
    var u/* :uint*/ = AS3.uint_(n); // coercion from Number to uint
    var b/* :Boolean*/ =AS3.is( p,  TestTypeCast);
    var notB/* :Boolean*/ = !AS3.is(p,  TestTypeCast);
    var castObjectToInterface/*: TestInterface*/ = AS3.cast(package1.mxml.pkg.TestInterface,{ foo: "FOO" });
    var castObjectToNonExt/*: SomeClass*/ = AS3.cast(package1.SomeClass,{ bar: "BAR" });
    var useAsWithDotExpr/*: String*/ = AS3.as(p,  TestTypeCast).toString();
    return AS3.as( p,  TestTypeCast);
  }/*

  public static*/ function testCastToUint$static(any/*:**/)/*:uint*/ {
    return AS3.uint_(any);
  }/*

  public static*/ function testCastToClassVar$static(clazz/*:Class*/, value/*:**/)/*:**/ {
    return AS3.cast(clazz,value);
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestTypeCast$,
      statics: {
        testAsCast: testAsCast$static,
        testCastToUint: testCastToUint$static,
        testCastToClassVar: testCastToClassVar$static
      },
      uses: [
        "AS3.int_",
        "AS3.uint_",
        "package1.SomeClass",
        "package1.mxml.pkg.TestInterface"
      ]
    };
});
