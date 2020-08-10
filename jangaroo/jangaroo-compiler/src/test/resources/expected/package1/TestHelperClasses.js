/*package package1 {*/

Ext.define("package1.TestHelperClasses", function(TestHelperClasses) {/*public class TestHelperClasses {

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

  public*/ function Helper$(text/*:String*/) {this.super$K0sP();
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
}*/var Helper$static = Ext.define(null, {constructor: Helper$,super$K0sP: function() {text_.call(this);},getText: getText,text_getter$K0sP: text_getter,statics: {CONST: "FOO"}});/*
}

============================================== Jangaroo part ==============================================*/
    return {statics: {
      TEXT: "foo",
      getText: getText$static,
      getConstantFromHelperClass: getConstantFromHelperClass$static
    }};
});
