define("as3/package1/TestLocalVariableDoesNotShadeClass",["module","exports","as3-rt/AS3","as3/package1/someOtherPackage/SomeOtherClass","as3/package2/TestStaticAccess"], function($module,$exports,AS3,SomeOtherClass,TestStaticAccess) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package2.TestStaticAccess;

public class TestLocalVariableDoesNotShadeClass {
  public*/ function TestLocalVariableDoesNotShadeClass() {
    var SomeOtherClass = package1.someOtherPackage.SomeOtherClass.BLA;
  }/*

  public*/ function getClass()/*:String*/ {
    var package2/*:Object*/ = { TestStaticAccess: "bar" };
    return typeof TestStaticAccess._;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      constructor: TestLocalVariableDoesNotShadeClass,
      getClass: getClass
    }}));
  });
});
