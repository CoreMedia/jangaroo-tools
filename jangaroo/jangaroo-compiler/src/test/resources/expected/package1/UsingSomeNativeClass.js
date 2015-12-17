Ext.define("package1.UsingSomeNativeClass", function(UsingSomeNativeClass) {/*package package1 {
import package1.someOtherPackage.SomeNativeClass;

/**
 * This is an example of a class using a "native" class.
 * /
public class UsingSomeNativeClass {

  public var someNative:package1.SomeNativeClass =*/function someNative_(){this.someNative=( new SomeNativeClass());}/*;
  public var someOtherNative:SomeOtherNativeClass =*/function someOtherNative_(){this.someOtherNative=( new SomeOtherNativeClass());}/*;
  public native function get someNative2():package1.SomeNativeClass;

  public*/ function UsingSomeNativeClass$() {var this$=this;someNative_.call(this);someOtherNative_.call(this);
    new package1.someOtherPackage.SomeNativeClass();
    this.someNative.setBaz ( "foo");
    this.someNative2.setBaz ( "foo");
    var local = function()/*:void*/ {
      var test/*:String*/ = this$.someNative2.getBaz();
    };
    var foo = this.getFoobar();
    var bar = this.getAnotherNativeAccessor();
  }/*

  [Accessor("getFoobar")]
  public*/ function get$someNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*

  [Accessor]
  public*/ function get$anotherNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*

  [Accessor]
  public*/ function get$monkey()/*:Boolean*/ {
    return false;
  }/*

  [Accessor]
  public*/ function set$monkey(value/*:Boolean*/)/*:void*/ {
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: UsingSomeNativeClass$,
      getFoobar: get$someNativeAccessor,
      getAnotherNativeAccessor: get$anotherNativeAccessor,
      isMonkey: get$monkey,
      setMonkey: set$monkey,
      requires: [
        "SomeNativeClass",
        "SomeOtherNativeClass",
        "package1.someOtherPackage.SomeNativeClass"
      ]
    };
});
