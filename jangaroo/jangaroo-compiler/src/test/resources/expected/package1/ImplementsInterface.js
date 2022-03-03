/*package package1{
import ext.Panel;*/

Ext.define("package1.ImplementsInterface", function(ImplementsInterface) {/*public final class ImplementsInterface implements Interface {

  /**
   * Field with ASDoc.
   * Second line.
   * /
  public var foo;
  
  /**
   * Annotated field with ASDoc.
   * /
  [Bar]
  public var bar:Vector.<Vector.<Panel> >;

  public*/ function ImplementsInterface$() {
    // nothing really
  }/*

  public*/ function doSomething()/*:String*/ {
    this.bar = new Vector$object/*.<Vector.<Panel> >*/();
    var panels/*:Vector.<Panel>*/ = new Vector$object/*.<Panel>*/;
    panels.push(new Ext.Panel({}));
    this.bar.push(panels);
    AS3.setBindable(this.bar[0][0],"title" , "Gotcha!");
  }/*

  public*/ function  get$property()/*:String*/ {
    return "prefix" + this.foo;
  }/*

  public*/ function  set$property(value/*:String*/)/*:void*/ {
    this.foo = value.substr("prefix".length);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      mixins: ["package1.Interface"],
      metadata: {bar: ["Bar"]},
      foo: undefined,
      bar: null,
      constructor: ImplementsInterface$,
      doSomething: doSomething,
      __accessors__: {property: {
        get: get$property,
        set: set$property
      }},
      requires: ["package1.Interface"],
      uses: ["Ext.Panel"]
    };
});
