define(["exports","as3-rt/AS3","as3/package1/ConfigClass","as3/joo/addEventListener","as3/package1/someOtherPackage/SomeEvent","as3/mx/resources/ResourceManager","as3/package1/someOtherPackage/SomeOtherClass","bundle!Foo"], function($exports,AS3,ConfigClass,addEventListener,SomeEvent,ResourceManager,SomeOtherClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import package1.someOtherPackage.SomeOtherClass;
import mx.resources.ResourceManager;

[ResourceBundle("Foo")]
public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public*/ function SomeMxmlClass() {
    
    this.bar = "BAR!";
    this.num = 123;
    Super.call(this);
    addEventListener(this, 'click', (SomeEvent._||SomeEvent._$get()), AS3.bind(this,"___on_click1"));
    this.foo = "bar";
    this.number = 42;
    var _$$1/*:Object*/ = {};
    _$$1.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","bar");
    var $$1/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())(_$$1);
    var _$$2/*:Object*/ = {};
    _$$2.bla = 1;
    var $$2/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())(_$$2);
    var _$$3/*:Object*/ = {};
    _$$3.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","baz");
    var $$3/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())(_$$3);
    var $$4/*:package1.ConfigClass*/ = new (ConfigClass._||ConfigClass._$get())();
    $$4.number = 12;
    var _$$5/*:Object*/ = {};
    _$$5.bla = 2;
    var $$5/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())(_$$5);
    $$4.items = [$$5];
    this.items = [$$1, $$2, $$3, $$4];
  }/*

  public native function get bar():String;

  /**
   * @private
   * /
  public native function set bar(value:String):void;

  public native function get num():int;

  /**
   * @private
   * /
  public native function set num(value:int):void;

  public*/ function ___on_click1(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    var Super=(ConfigClass._||ConfigClass._$get());
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      metadata: {ResourceBundle: {$value: "Foo"}},
      class_: "SomeMxmlClass",
      extends_: Super,
      members: {
        constructor: SomeMxmlClass,
        ___on_click1: ___on_click1
      }
    }));
  });
});
