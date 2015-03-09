define("as3/package1/FieldInitializer",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
public class FieldInitializer {
  private const const1:String = "foo";
  private const const2:Object = "foo" + "bar";
  private const const3:Object =*/function const3_(){Object.defineProperty(this,"const3$1",{value: {"foo": "bar"}});}/*;

  public*/ function foo()/*:String*/ {
    return this.const1$1 + this.const2$1 + this.const3$1;
  }/*
}*/function FieldInitializer() {const3_.call(this);}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      const1$1: "foo",
      const2$1: "foo" + "bar",
      foo: foo,
      constructor: FieldInitializer
    }}));
  });
});
