/*package package1 {*/
Ext.define("package1.FieldInitializer", function(FieldInitializer) {/*public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object =*/function const3_(){this.const3$ZOoB=( {"foo": "bar"});}/*;

  [Bindable]
  public var myConfigOption:String = "baz";

  [Bindable]
  public var myConfigOption2:Object =*/function myConfigOption2_(){this.myConfigOption2=( { a: 123 });}/*;*/

  function setMyConfigOption(value/*:String*/)/*:void*/ {
    this["myConfigOption"] = value + "!";
  }/*

  private*/ function setMyConfigOption2(value/*: Object*/) {
    this.myConfigOption2 = value;
  }/*

  public*/ function foo()/*:String*/ {
    this.setMyConfigOption("direct set call");
    return this.const1$ZOoB + this.const2$ZOoB + this.const3$ZOoB;
  }/*
}*/function FieldInitializer$() {this.super$ZOoB();}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      const1$ZOoB: "foo",
      const2$ZOoB: "foo" + "bar",
      setMyConfigOption: setMyConfigOption,
      setMyConfigOption2$ZOoB: setMyConfigOption2,
      foo: foo,
      super$ZOoB: function() {
        const3_.call(this);
        myConfigOption2_.call(this);
      },
      constructor: FieldInitializer$,
      config: {
        myConfigOption: "baz",
        myConfigOption2: undefined
      }
    };
});
