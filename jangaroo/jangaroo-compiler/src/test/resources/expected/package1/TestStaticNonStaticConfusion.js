Ext.define("AS3.package1.TestStaticNonStaticConfusion", function(TestStaticNonStaticConfusion) {/*package package1 {
public class TestStaticNonStaticConfusion {

  public*/ function TestStaticNonStaticConfusion$() {
    this.foo$1();
    this.foo$1();
    TestStaticNonStaticConfusion.foo();
  }/*

  public static*/ function foo$static()/*:String*/ {
    return "static foo";
  }/*

  private*/ function foo()/*:String*/ {
    return "foo";
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestStaticNonStaticConfusion$,
      foo$1: foo,
      statics: {foo: foo$static}
    };
});
