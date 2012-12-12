define(["exports","runtime/AS3","classes/package1/ConfigClass","classes/joo/addEventListener","classes/package1/someOtherPackage/SomeEvent","classes/package1/someOtherPackage/SomeOtherClass"], function($exports,AS3,ConfigClass,addEventListener,SomeEvent,SomeOtherClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import package1.someOtherPackage.SomeOtherClass;

public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public native function get bar():String;

  /**
   * 
  @param value @private
   * /
  public native function set bar(value:String):void;

  public native function get num():int;

  /**
   * 
  @param value @private
   * /
  public native function set num(value:int):void;

  public*/ function SomeMxmlClass() {
     Super.call(this);
    this.bar = "BAR!";
    this.num = 123;
    addEventListener._(this, 'click', SomeEvent._, AS3.bind(this,"___on_click1"));
    this.foo = "bar";
    var $$2/*:package1.someOtherPackage.SomeOtherClass*/ = new SomeOtherClass._();
    $$2.bla = 42;
    var $$3/*:package1.someOtherPackage.SomeOtherClass*/ = new SomeOtherClass._();
    $$3.bla = 1;
    this.items = [$$2, $$3];
    this.number = 42;
  }/*

  public*/ function ___on_click1(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    var Super=ConfigClass._;
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "SomeMxmlClass",
      extends_: Super,
      members: {
        constructor: SomeMxmlClass,
        ___on_click1: ___on_click1
      }
    }));
  });
});
