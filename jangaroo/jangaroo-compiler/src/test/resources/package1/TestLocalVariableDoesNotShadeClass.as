package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package2.TestStaticAccess;

public class TestLocalVariableDoesNotShadeClass {
  public function TestLocalVariableDoesNotShadeClass() {
    var SomeOtherClass = package1.someOtherPackage.SomeOtherClass.BLA;
  }

  public function getClass():String {
    var package2:Object = { TestStaticAccess: "bar" };
    return typeof package2.TestStaticAccess;
  }
}
}