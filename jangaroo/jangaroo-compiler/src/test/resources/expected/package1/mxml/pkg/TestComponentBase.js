/*package package1.mxml.pkg {*/


Ext.define("package1.mxml.pkg.TestComponentBase", function(TestComponentBase) {/*public class TestComponentBase implements TestInterface {

  public static const DEFAULT:String = "_DEFAULT_";

  public var emptyText:String;
  public var letters:Array;

  private var property_1:String;
  private var property_2:int;

  public*/ function TestComponentBase$(config/*:TestComponent = null*/) {if(arguments.length<=0)config=null;
    this.property_1$00xv = config.property_1 + "_HI";
    this.property_2$00xv = config.property_2 || 0;
  }/*

  private var component:Object;

  public*/ function init(component/*:Object*/)/*:void*/ {
    this.component$00xv = component;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      mixins: ["package1.mxml.pkg.TestInterface"],
      emptyText: null,
      letters: null,
      property_1$00xv: null,
      property_2$00xv: 0,
      constructor: TestComponentBase$,
      component$00xv: null,
      init: init,
      statics: {DEFAULT: "_DEFAULT_"},
      requires: ["package1.mxml.pkg.TestInterface"]
    };
});
