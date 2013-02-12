package package1 {
import package1.someOtherPackage.SomeOtherClass;

/**
 * This is an example of a class using a "package global" variable.
 */
public class UsingSomePackageGlobal {

  public static function main():void {
    somePackageGlobal = new SomeOtherClass();
    var local:Object = somePackageGlobal || {};
  }

}
}