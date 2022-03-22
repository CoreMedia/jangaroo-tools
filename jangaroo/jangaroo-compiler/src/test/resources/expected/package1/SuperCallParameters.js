/*package package1 {*/

Ext.define("package1.SuperCallParameters", function(SuperCallParameters) {/*public class SuperCallParameters extends ManyConstructorParameters {
  public*/ function SuperCallParameters$() {var _this=this;/*
    const*/var foo/*: Boolean*/ = this.isEmpty("");
    var bar/*: String*/ = "BAR";
    function innerUsingThis()/*: Boolean*/ {
      return _this.isEmpty("");
    }
    function innerDynamic()/*: Boolean*/ {
      return this.isEmpty("");
    }
    this.super$rE5W("bar", -1, -4.2, true, {}, []);
    if (foo) {
      package1.ManyConstructorParameters.prototype.isEmpty.call(this,"FOO");
    }
  }/*

  override public*/ function isEmpty(str/*:String*/)/*:Boolean*/ {
    return package1.ManyConstructorParameters.prototype.isEmpty.call(this,str);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ManyConstructorParameters",
      constructor: SuperCallParameters$,
      super$rE5W: function() {
        package1.ManyConstructorParameters.prototype.constructor.apply(this, arguments);
      },
      isEmpty: isEmpty,
      requires: ["package1.ManyConstructorParameters"]
    };
});
