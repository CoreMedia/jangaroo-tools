joo.classLoader.prepare("package package1",/* {
import package1.someOtherPackage.SomeOtherClass;


    import foo.Bar;*/
  "public class SomeMxmlClass extends package1.ConfigClass",2,function($$private){var $$bound=joo.boundMethod,$1=package1;return[ 
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
    this.addEventListener('click', $$bound(this,"_on_click1"));
    this.foo = "bar";
    this.items = [this._create2$2(), this._create3$2()];
    this.number = 42;
    this.bar = "BAR!";
    this.num = 123;
  },

  "public function _on_click1",function _on_click1(event/*:package1.ConfigClass*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  },

  "private function _create2",function _create2()/*:package1.someOtherPackage.SomeOtherClass*/ {
    var temp/*:package1.someOtherPackage.SomeOtherClass*/ = new package1.someOtherPackage.SomeOtherClass();
    temp.bla = 42;
    return temp;
  },

  "private function _create3",function _create3()/*:package1.someOtherPackage.SomeOtherClass*/ {
    var temp/*:package1.someOtherPackage.SomeOtherClass*/ = new package1.someOtherPackage.SomeOtherClass();
    temp.bla = 1;
    return temp;
  },
undefined];},[],["package1.ConfigClass","package1.someOtherPackage.SomeOtherClass"], "0.8.0", "2.0.4-SNAPSHOT"
);