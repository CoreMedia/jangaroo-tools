package package1 {

import joo.getOrCreatePackage;

import package1.someOtherPackage.SomeOtherClass;

/**
 * This is an example of a class using a "package global" variable.
 */
public class UsingSomePackageGlobalUntyped {

  public static function main():void {
    delete getOrCreatePackage("package1").somePackageGlobalConst;
    delete getOrCreatePackage("package1")["somePackageGlobalConst"];
    getOrCreatePackage("package1").somePackageGlobalConst = new SomeOtherClass();
    joo.getOrCreatePackage("package1").somePackageGlobalConst = new SomeOtherClass();
    getOrCreatePackage("package1").somePackageGlobalConst.doodle = "doo";
    getOrCreatePackage("package1")['somePackageGlobalConst'] = new SomeOtherClass();
    // and finally, a totally unnecessary untyped write to a *var*:
    getOrCreatePackage("package1")['somePackageGlobal'] = new SomeOtherClass();
  }

}
}