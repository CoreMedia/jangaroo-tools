/*package package1 {
import package1.someOtherPackage.SomeOtherClass;*/

/**
 * This is an example of a class using a "package global" variable.
 */
Ext.define("package1.UsingSomePackageGlobal", function(UsingSomePackageGlobal) {/*public class UsingSomePackageGlobal {

  public static*/ function main$static()/*:void*/ {
    package1.somePackageGlobal = new package1.someOtherPackage.SomeOtherClass();
    var local/*:Object*/ = package1.somePackageGlobal || {};
    foo.somethingElse = null;
    var local2/*:**/ = foo.somethingElse || {};
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {
      statics: {main: main$static},
      uses: [
        "package1.someOtherPackage.SomeOtherClass",
        "package1.somePackageGlobal"
      ]
    };
});
