/*package package1{*/
Ext.define("package1.WithStaticReference", function(WithStaticReference) {/*public class WithStaticReference {
  public static const BLA = "bla";
  public*/ function WithStaticReference$() {
    var bla = WithStaticReference.BLA;AS3.is(
    bla,  WithStaticReference);
    this.make2$K2pq();
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
    return {
      constructor: WithStaticReference$,
      make2$K2pq: make2,
      statics: {
        BLA: "bla",
        make: make$static
      }
    };
});
