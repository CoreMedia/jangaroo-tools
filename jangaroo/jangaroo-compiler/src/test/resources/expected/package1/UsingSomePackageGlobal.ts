import SomeOtherClass from "./someOtherPackage/SomeOtherClass";
import somePackageGlobal from "./somePackageGlobal";


/**
 * This is an example of a class using a "package global" variable.
 */
class UsingSomePackageGlobal {

  static main():void {
    somePackageGlobal._ = new SomeOtherClass();
    var local = somePackageGlobal._ || {};
    foo.somethingElse = null;
    var local2:any = foo.somethingElse || {};
  }

}
export default UsingSomePackageGlobal;
