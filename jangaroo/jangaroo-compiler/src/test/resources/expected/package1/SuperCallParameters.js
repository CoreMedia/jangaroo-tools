Ext.define("package1.SuperCallParameters", function(SuperCallParameters) {/*package package1 {

public class SuperCallParameters extends ManyConstructorParameters {
  public*/ function SuperCallParameters$() {
    this.super$rE5W("bar", -1, -4.2, true, {}, []);
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
