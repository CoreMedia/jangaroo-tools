define(["runtime/AS3"], function(AS3) { "use strict";return AS3.class_(function(){/*package package1{
public class WithStaticReference {
  public static const BLA = "bla";
  public*/ function WithStaticReference() {
    var bla = WithStaticReference.BLA;AS3.is(
    bla,  WithStaticReference);
    this.make2$1();
  }/*
  public static*/ function make()/*:void*/ {
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
    return {
      package_: "package1",
      class_: "WithStaticReference",
      members: {
        constructor: WithStaticReference,
        make2$1: make2
      },
      staticMembers: {
        BLA: "bla",
        make: make
      }
    };
  });
});
