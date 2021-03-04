package package1 {
import package1.someOtherPackage.SomeNativeClass;

/**
 * This is an example of a class using a "native" class.
 */
public class UsingSomeNativeClass {

  public var someNative:package1.SomeNativeClass = new package1.SomeNativeClass();
  public var someOtherNative:SomeOtherNativeClass = new SomeOtherNativeClass();
  public native function get someNative2():package1.SomeNativeClass;

  public function UsingSomeNativeClass() {
    new package1.someOtherPackage.SomeNativeClass();
    someNative.baz = "foo";
    someNative2.baz = "foo";
    var local = function():void {
      var test:String = someNative2.baz;
    };
    var foo = this.someNativeAccessor;
    var bar = this.anotherNativeAccessor;
  }

  public function get someNativeAccessor():package1.SomeNativeClass {
    return someNative;
  }

  public function get anotherNativeAccessor():package1.SomeNativeClass {
    return someNative;
  }

  public function get monkey():Boolean {
    return false;
  }

  public function set monkey(value:Boolean):void {
  }
}
}