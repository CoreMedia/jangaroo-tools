define(["exports","as3-rt/AS3","as3/package1/ConfigClass","as3/package1/someOtherPackage/SomeOtherClass","as3/joo/addEventListener","as3/package1/someOtherPackage/SomeEvent","as3/mx/resources/ResourceManager","bundle!Foo"], function($exports,AS3,ConfigClass,SomeOtherClass,addEventListener,SomeEvent,ResourceManager) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import mx.resources.ResourceManager;

[ResourceBundle("Foo")]
public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public*/ function SomeMxmlClass() {
    
    this.bar = "BAR!";
    this.num = 123;
    this.blub = {};
    this.blub.name = "Kuno";
    var $$1/*:Object*/ = {};
    $$1.name = "Joe";
    var $$2/*:Object*/ = {};
    $$2.name = "Felix";
    this.list = [$$1, $$2];
    var $$3/*:Object*/ = {};
    $$3.bla = 3;
    this.other = new (SomeOtherClass._||SomeOtherClass._$get())($$3);
    Super.call(this);
    addEventListener(this, 'click', (SomeEvent._||SomeEvent._$get()),AS3.bind( this,"___on_click1"));
    this.foo = "bar";
    this.number = 42;
    var $$4/*:Object*/ = {};
    $$4.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","bar");
    var $$5/*:Object*/ = {};
    $$5.bla = 1;
    var $$6/*:Object*/ = {};
    $$6.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","baz");
    var $$7/*:package1.ConfigClass*/ = new (ConfigClass._||ConfigClass._$get())();
    $$7.number = 12;
    var $$8/*:Object*/ = {};
    $$8.bla = 2;
    $$7.items = [new (SomeOtherClass._||SomeOtherClass._$get())($$8)];
    this.items = [new (SomeOtherClass._||SomeOtherClass._$get())($$4), new (SomeOtherClass._||SomeOtherClass._$get())($$5), new (SomeOtherClass._||SomeOtherClass._$get())($$6), $$7];
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

  public native function get blub():Object;

  /**
   * @private
   * /
  public native function set blub(value:Object):void;

  public native function get list():Array;

  /**
   * @private
   * /
  public native function set list(value:Array):void;

  public native function get other():package1.someOtherPackage.SomeOtherClass;

  /**
   * @private
   * /
  public native function set other(value:package1.someOtherPackage.SomeOtherClass):void;

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
