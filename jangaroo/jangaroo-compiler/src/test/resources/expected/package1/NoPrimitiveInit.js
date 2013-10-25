define(["exports","as3-rt/AS3","as3/package1/someOtherPackage/SomeOtherClass","as3/int"], function($exports,AS3,SomeOtherClass,int_) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

public class NoPrimitiveInit {
  public*/ function NoPrimitiveInit() {
  }/*

  private*/ function method(i/*:int*/)/*:int*/ {
    return SomeOtherClass._.BLA + int_._.MAX_VALUE;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "NoPrimitiveInit",
      members: {
        constructor: NoPrimitiveInit,
        method$1: method
      }
    }));
  });
});
