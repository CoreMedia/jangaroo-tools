Ext.define("package1.FieldInitializer", function(FieldInitializer) {/*package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object =*/function const3_(){Object.defineProperty(this,"const3$1",{value: {"foo": "bar"}});}/*;

  public*/ function foo()/*:String*/ {
    return this.const1$1 + this.const2$1 + this.const3$1;
  }/*
}*/function FieldInitializer$() {const3_.call(this);}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      const1$1: "foo",
      const2$1: "foo" + "bar",
      foo: foo,
      constructor: FieldInitializer$
    };
});
