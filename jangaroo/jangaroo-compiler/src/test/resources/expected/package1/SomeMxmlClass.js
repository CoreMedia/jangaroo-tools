define(["exports","as3-rt/AS3","as3/package1/ConfigClass","as3/joo/addEventListener","as3/package1/someOtherPackage/SomeEvent","as3/package1/someOtherPackage/SomeOtherClass","as3/mx/resources/ResourceManager","bundle!Foo"], function($exports,AS3,ConfigClass,addEventListener,SomeEvent,SomeOtherClass,ResourceManager) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import package1.someOtherPackage.SomeOtherClass;
import mx.resources.ResourceManager;

[ResourceBundle("Foo")]
public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public*/ function SomeMxmlClass() {
    Super.call(this);
    this.bar = "BAR!";
    this.num = 123;
    addEventListener(this, 'click', (SomeEvent._||SomeEvent._$get()), AS3.bind(this,"___on_click1"));
    this.foo = "bar";
    this.number = 42;
    var $$2/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())({id: "foo"});
    $$2.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","bar");
    var $$3/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())();
    $$3.bla = 1;
    var $$4/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())();
    $$4.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","baz");
    var $$5/*:package1.ConfigClass*/ = new (ConfigClass._||ConfigClass._$get())();
    $$5.number = 12;
    var $$6/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())();
    $$6.bla = 2;
    $$5.items = [$$6];
    this.items = [$$2, $$3, $$4, $$5];
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
