import SomeOtherClass from "./someOtherPackage/SomeOtherClass";
import somePackageGlobal from "./somePackageGlobal";
import somePackageGlobalConst from "./somePackageGlobalConst";


/**
 * This is an example of a class using a "package global" variable.
 */
class UsingSomePackageGlobalUntyped {

  static main():void {
    delete (somePackageGlobalConst as { _?: any })._;
    delete (somePackageGlobalConst as { _?: any })._;
    (somePackageGlobalConst as { _: typeof somePackageGlobalConst._ })._ = new SomeOtherClass();
    (somePackageGlobalConst as { _: typeof somePackageGlobalConst._ })._ = new SomeOtherClass();
    somePackageGlobalConst._.doodle = "doo";
    (somePackageGlobalConst as { _: typeof somePackageGlobalConst._ })._ = new SomeOtherClass();
    // and finally, a totally unnecessary untyped write to a *var*:
    somePackageGlobal._ = new SomeOtherClass();
  }

}
export default UsingSomePackageGlobalUntyped;
