Ext.define("AS3.package1.TestLocalVariableDoesNotShadeClass", function(TestLocalVariableDoesNotShadeClass) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package2.TestStaticAccess;

public class TestLocalVariableDoesNotShadeClass {
  public*/ function TestLocalVariableDoesNotShadeClass$() {
    var SomeOtherClass = AS3.package1.someOtherPackage.SomeOtherClass.BLA;
  }/*

  public*/ function getClass()/*:String*/ {
    var package2/*:Object*/ = { TestStaticAccess: "bar" };
    return typeof AS3.package2.TestStaticAccess;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestLocalVariableDoesNotShadeClass$,
      getClass: getClass,
      requires: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.package2.TestStaticAccess"
      ]
    };
});
