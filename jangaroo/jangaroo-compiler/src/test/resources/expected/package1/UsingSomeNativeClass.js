/*package package1 {
import package1.someOtherPackage.SomeNativeClass;*/

/**
 * This is an example of a class using a "native" class.
 */
Ext.define("package1.UsingSomeNativeClass", function(UsingSomeNativeClass) {/*public class UsingSomeNativeClass {

  public var someNative:package1.SomeNativeClass =*/function someNative_(){this.someNative=( new SomeNativeClass());}/*;
  public var someOtherNative:SomeOtherNativeClass =*/function someOtherNative_(){this.someOtherNative=( new SomeOtherNativeClass());}/*;
  public native function get someNative2():package1.SomeNativeClass;

  public*/ function UsingSomeNativeClass$() {var _this=this;this.super$7Tfi();
    new package1.someOtherPackage.SomeNativeClass();
    this.someNative.baz = "foo";
    this.someNative2.baz = "foo";
    var local = function()/*:void*/ {
      var test/*:String*/ = _this.someNative2.baz;
    };
    var foo = this.someNativeAccessor;
    var bar = this.anotherNativeAccessor;
    this.someNative.delete();
  }/*

  public*/ function  get$someNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*

  public*/ function  get$anotherNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*

  public*/ function  get$monkey()/*:Boolean*/ {
    return false;
  }/*

  public*/ function  set$monkey(value/*:Boolean*/)/*:void*/ {
  }/*

  public native function abstractMethod(param1: String, param2: Number = 0):void;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: UsingSomeNativeClass$,
      super$7Tfi: function() {
        someNative_.call(this);
        someOtherNative_.call(this);
      },
      __accessors__: {
        someNativeAccessor: {get: get$someNativeAccessor},
        anotherNativeAccessor: {get: get$anotherNativeAccessor},
        monkey: {
          get: get$monkey,
          set: set$monkey
        }
      },
      uses: [
        "SomeOtherNativeClass",
        "package1.someOtherPackage.SomeNativeClass"
      ]
    };
});
