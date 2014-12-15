define("as3/package1/WithStaticReference",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1{
public class WithStaticReference {
  public static const BLA = "bla";
  public*/ function WithStaticReference() {
    var bla = WithStaticReference.BLA;AS3.is(
    bla,  WithStaticReference);
    this.make2$1();
  }/*
  public static*/ function make$static()/*:void*/ {
    var bla = WithStaticReference.BLA;AS3.is(
    bla,  WithStaticReference);
    new WithStaticReference();
  }/*
  private*/ function make2()/*:void*/ {
    var bla = WithStaticReference.BLA;AS3.is(
    bla,  WithStaticReference);
    new WithStaticReference();
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {
      members: {
        constructor: WithStaticReference,
        make2$1: make2
      },
      staticMembers: {
        BLA: "bla",
        make: make$static
      }
    }));
  });
});
