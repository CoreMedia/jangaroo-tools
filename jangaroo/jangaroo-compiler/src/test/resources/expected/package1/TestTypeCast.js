/*package package1 {*/

Ext.define("package1.TestTypeCast", function(TestTypeCast) {/*public class TestTypeCast {

  public*/ function TestTypeCast$() {
  }/*

  public static*/ function testAsCast$static(p/* : Object*/)/* : TestTypeCast*/ {
    var r/* :Number*/ = 99.7;
    var n/* :Number*/ = Number("99.8");
    var i/* :int*/ = AS3.int_(r);
    var b/* :Boolean*/ =AS3.is( p,  TestTypeCast);
    return AS3.as( p,  TestTypeCast);
  }/*

  public static*/ function testCastToUint$static(any/*:**/)/*:uint*/ {
    return AS3.int_(any);
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
        "Number"
      ]
    };
});
