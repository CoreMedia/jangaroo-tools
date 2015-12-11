Ext.define("package1.SuperCallParameters", function(SuperCallParameters) {/*package package1 {

public class SuperCallParameters extends ManyConstructorParameters {
  public*/ function SuperCallParameters$() {
    this.callParent(["bar", -1, -4.2, true, {}, []]);
  }/*

  override public*/ function isEmpty(str/*:String*/)/*:Boolean*/ {
    return this.callParent([str]);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ManyConstructorParameters",
      constructor: SuperCallParameters$,
      isEmpty: isEmpty,
      requires: ["package1.ManyConstructorParameters"]
    };
});
