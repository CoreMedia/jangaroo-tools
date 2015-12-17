Ext.define("package1.UsingSomePackageGlobal", function(UsingSomePackageGlobal) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

/**
 * This is an example of a class using a "package global" variable.
 * /
public class UsingSomePackageGlobal {

  public static*/ function main$static()/*:void*/ {
    package1.somePackageGlobal = new package1.someOtherPackage.SomeOtherClass();
    var local/*:Object*/ = package1.somePackageGlobal || {};
    foo.somethingElse = null;
    var local2/*:**/ = foo.somethingElse || {};
  }/*

}*/function UsingSomePackageGlobal$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: UsingSomePackageGlobal$,
      statics: {main: main$static},
      requires: [
        "package1.somePackageGlobal",
        "package1.someOtherPackage.SomeOtherClass",
        "foo.somethingElse"
      ]
    };
});
