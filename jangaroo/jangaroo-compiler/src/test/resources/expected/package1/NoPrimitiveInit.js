Ext.define("AS3.package1.NoPrimitiveInit", function(NoPrimitiveInit) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

public class NoPrimitiveInit {
  public*/ function NoPrimitiveInit$() {
  }/*

  private*/ function method(i/*:int*/)/*:int*/ {
    return AS3.package1.someOtherPackage.SomeOtherClass.BLA + AS3.int.MAX_VALUE;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: NoPrimitiveInit$,
      method$1: method,
      requires: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.int"
      ]
    };
});
