Ext.define("AS3.package1.SuperCallParameters", function(SuperCallParameters) {/*package package1 {

public class SuperCallParameters extends ManyConstructorParameters {
  public*/ function SuperCallParameters$() {
    AS3.package1.ManyConstructorParameters.prototype.constructor.call(this,"bar", -1, -4.2, true, {}, []);
  }/*

  override public*/ function isEmpty(str/*:String*/)/*:Boolean*/ {
    return AS3.package1.ManyConstructorParameters.prototype.isEmpty.call(this,str);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ManyConstructorParameters",
      constructor: SuperCallParameters$,
      isEmpty: isEmpty,
      requires: ["AS3.package1.ManyConstructorParameters"]
    };
});
