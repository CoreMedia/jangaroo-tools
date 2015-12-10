Ext.define("package1.NoPrimitiveInit", function(NoPrimitiveInit) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

public class NoPrimitiveInit {
  public*/ function NoPrimitiveInit$() {
  }/*

  private*/ function method(i/*:int*/)/*:int*/ {
    return package1.someOtherPackage.SomeOtherClass.BLA + int.MAX_VALUE;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: NoPrimitiveInit$,
      method$1: method
    };
});
