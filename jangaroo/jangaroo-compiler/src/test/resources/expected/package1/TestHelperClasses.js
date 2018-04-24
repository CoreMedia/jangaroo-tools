Ext.define("package1.TestHelperClasses", function(TestHelperClasses) {/*package package1 {

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
}

class Helper {

  internal static const CONST:String = "FOO";
  private var text:String =*/function text_(){this.text$K0sP=( TestHelperClasses.TEXT);}/*;

  public*/ function Helper$(text/*:String*/) {text_.call(this);
    this.text$K0sP = text;
  }/*

  public*/ function getText()/*:String*/ {
    var f/*:Function*/ =AS3.bind( this,"text_getter$K0sP");
    f =AS3.bind( this,"text_getter$K0sP");
    return f();
  }/*

  private*/ function text_getter()/*:String*/ {
    return this.text$K0sP;
  }/*
}*/var Helper$static = Ext.define(null, {constructor: Helper$,getText: getText,text_getter$K0sP: text_getter,statics: {CONST: "FOO"}});function TestHelperClasses$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestHelperClasses$,
      statics: {
        TEXT: "foo",
        getText: getText$static,
        getConstantFromHelperClass: getConstantFromHelperClass$static
      }
    };
});
