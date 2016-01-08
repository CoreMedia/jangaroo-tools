Ext.define("AS3.package1.FieldInitializer", function(FieldInitializer) {/*package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object =*/function const3_(){this.const3$1=( {"foo": "bar"});}/*;

  [Bindable]
  public var myConfigOption:String = "baz";

  [Bindable]
  public var myConfigOption2:Object =*/function myConfigOption2_(){this.myConfigOption2=( { a: 123 });}/*;

  public*/ function foo()/*:String*/ {
    return this.const1$1 + this.const2$1 + this.const3$1;
  }/*
}*/function FieldInitializer$() {const3_.call(this);myConfigOption2_.call(this);}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      const1$1: "foo",
      const2$1: "foo" + "bar",
      foo: foo,
      constructor: FieldInitializer$,
      config: {
        myConfigOption: "baz",
        myConfigOption2: undefined
      }
    };
});
