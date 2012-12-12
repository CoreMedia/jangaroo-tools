joo.classLoader.prepare("package package1",/* {
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import package1.someOtherPackage.SomeOtherClass;*/

"public class SomeMxmlClass extends package1.ConfigClass",2,function($$private){var $$bound=joo.boundMethod,$1=package1;return[function(){joo.classLoader.init(package1.someOtherPackage.SomeEvent);}, /*
    import foo.Bar;*/
  
  "public native function get bar"/*():String*/,

  /**
   * 
  @param value @private
   */
  "public native function set bar"/*(value:String):void*/,

  "public native function get num"/*():int*/,

  /**
   * 
  @param value @private
   */
  "public native function set num"/*(value:int):void*/,

  "public function SomeMxmlClass",function SomeMxmlClass() {
     $1.ConfigClass.call(this);
    this.bar = "BAR!";
    this.num = 123;
    joo.addEventListener(this, 'click', package1.someOtherPackage.SomeEvent, $$bound(this,"___on_click1"));
    this.foo = "bar";
    var $$2/*:package1.someOtherPackage.SomeOtherClass*/ = new package1.someOtherPackage.SomeOtherClass();
    $$2.bla = 42;
    var $$3/*:package1.someOtherPackage.SomeOtherClass*/ = new package1.someOtherPackage.SomeOtherClass();
    $$3.bla = 1;
    this.items = [$$2, $$3];
    this.number = 42;
  },

  "public function ___on_click1",function ___on_click1(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  },
undefined];},[],["package1.ConfigClass","joo.addEventListener","package1.someOtherPackage.SomeEvent","package1.someOtherPackage.SomeOtherClass"], "0.8.0", "2.0.4-SNAPSHOT"
);