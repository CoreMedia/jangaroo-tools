define("as3/package1/TestHelperClasses",["module","as3-rt/AS3"], function($module,AS3) { AS3.compilationUnit($module,function($primaryDeclaration){/*package package1 {

public class TestHelperClasses {

  public static const TEXT:String = "foo";
  public static*/ function getText$static()/*:String*/ {
    var thc/*:Helper*/ = new Helper$static("foo");
    var f/*:Function*/ =AS3.bind( thc,"getText");
    return f();
  }/*

  public static*/ function getConstantFromHelperClass$static()/*:String*/ {
    return Helper$static.CONST;
  }/*
}*/function Helper_(){/*

class Helper {

  internal static const CONST:String = "FOO";
  private var text:String =*/function text_(){this.text$1=( TestHelperClasses.TEXT);}/*;

  public*/ function Helper(text/*:String*/) {text_.call(this);
    this.text$1 = text;
  }/*

  public*/ function getText()/*:String*/ {
    var f/*:Function*/ =AS3.bind( this,"text_getter$1");
    f =AS3.bind( this,"text_getter$1");
    return f();
  }/*

  private*/ function text_getter()/*:String*/ {
    return this.text$1;
  }/*
}*/return AS3.class_({id:"as3/package1.TestHelperClasses.Helper"},{members: {constructor: Helper,getText: getText,text_getter$1: text_getter},staticMembers: {CONST: "FOO"}});}function TestHelperClasses() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {
      members: {constructor: TestHelperClasses},
      staticMembers: {
        TEXT: "foo",
        getText: getText$static,
        getConstantFromHelperClass: getConstantFromHelperClass$static
      }
    }));
    var Helper$static=Helper_();
  });
});
