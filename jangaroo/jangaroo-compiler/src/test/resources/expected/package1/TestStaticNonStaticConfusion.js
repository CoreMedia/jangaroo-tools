/*package package1 {*/
Ext.define("package1.TestStaticNonStaticConfusion", function(TestStaticNonStaticConfusion) {/*public class TestStaticNonStaticConfusion {

  public*/ function TestStaticNonStaticConfusion$() {
    this.foo$8Jhk();
    this.foo$8Jhk();
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
      foo$8Jhk: foo,
      statics: {foo: foo$static}
    };
});
