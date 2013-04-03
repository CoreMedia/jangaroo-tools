package package1 {
import package1.someOtherPackage.SomeNativeClass;

/**
 * This is an example of a class using a "native" class.
 */
public class UsingSomeNativeClass {

  public var someNative:package1.SomeNativeClass = new package1.SomeNativeClass();

  public function UsingSomeNativeClass() {
    new package1.someOtherPackage.SomeNativeClass();
    someNative.baz = "foo";
    var foo = this.someNativeAccessor;
    var bar = this.anotherNativeAccessor;
  }

  [Accessor("getFoobar")]
  public function get someNativeAccessor():package1.SomeNativeClass {
    return someNative;
  }

  [Accessor]
  public function get anotherNativeAccessor():package1.SomeNativeClass {
    return someNative;
  }
}
}