Ext.define("AS3.package1.Override", function(Override) {/*package package1 {

[Override]
public class Override extends OverrideBase {

  public*/ function Override$() {AS3.package1.OverrideBase.prototype.constructor.call(this);
    this.callParent();
    this["field1"] = "overriddenField1";
    this["field2"] = "overriddenField2";
  }/*

  override public*/ function baseFun()/*:String*/ {
    return "overriddenBaseFun";
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      override: "AS3.package1.OverrideBase",
      metadata: {"": ["Override"]},
      constructor: Override$,
      baseFun: baseFun,
      requires: ["AS3.package1.OverrideBase"]
    };
});
