define("as3/package1/TestStaticNonStaticConfusion",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
public class TestStaticNonStaticConfusion {

  public*/ function TestStaticNonStaticConfusion() {
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
    $primaryDeclaration(AS3.class_($module, {
      members: {
        constructor: TestStaticNonStaticConfusion,
        foo$1: foo
      },
      staticMembers: {foo: foo$static}
    }));
  });
});
