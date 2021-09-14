/*package package1 {

import joo.getOrCreatePackage;

import package1.someOtherPackage.SomeOtherClass;*/

/**
 * This is an example of a class using a "package global" variable.
 */
Ext.define("package1.UsingSomePackageGlobalUntyped", function(UsingSomePackageGlobalUntyped) {/*public class UsingSomePackageGlobalUntyped {

  public static*/ function main$static()/*:void*/ {
    delete joo.getOrCreatePackage("package1").somePackageGlobalConst;
    delete joo.getOrCreatePackage("package1")["somePackageGlobalConst"];
    joo.getOrCreatePackage("package1").somePackageGlobalConst = new package1.someOtherPackage.SomeOtherClass();
    joo.getOrCreatePackage("package1").somePackageGlobalConst = new package1.someOtherPackage.SomeOtherClass();
    joo.getOrCreatePackage("package1").somePackageGlobalConst.doodle = "doo";
    joo.getOrCreatePackage("package1")['somePackageGlobalConst'] = new package1.someOtherPackage.SomeOtherClass();
    // and finally, a totally unnecessary untyped write to a *var*:
    joo.getOrCreatePackage("package1")['somePackageGlobal'] = new package1.someOtherPackage.SomeOtherClass();
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {
      statics: {main: main$static},
      uses: [
        "package1.someOtherPackage.SomeOtherClass",
        "package1.somePackageGlobal",
        "package1.somePackageGlobalConst"
      ]
    };
});
