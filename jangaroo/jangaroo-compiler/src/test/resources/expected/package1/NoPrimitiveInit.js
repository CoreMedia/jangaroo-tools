/*package package1 {
import package1.someOtherPackage.SomeOtherClass;*/

Ext.define("package1.NoPrimitiveInit", function(NoPrimitiveInit) {/*public class NoPrimitiveInit {
  public*/ function NoPrimitiveInit$() {
  }/*

  private*/ function method(i/*:int*/)/*:int*/ {
    return package1.someOtherPackage.SomeOtherClass.BLA + AS3.int_.MAX_VALUE;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: NoPrimitiveInit$,
      method$0PBp: method,
      uses: [
        "AS3.int_",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
