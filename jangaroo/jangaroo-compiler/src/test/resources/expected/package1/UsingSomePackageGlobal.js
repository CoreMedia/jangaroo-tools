Ext.define("AS3.package1.UsingSomePackageGlobal", function(UsingSomePackageGlobal) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

/**
 * This is an example of a class using a "package global" variable.
 * /
public class UsingSomePackageGlobal {

  public static*/ function main$static()/*:void*/ {
    AS3.package1.somePackageGlobal = new AS3.package1.someOtherPackage.SomeOtherClass();
    var local/*:Object*/ = AS3.package1.somePackageGlobal || {};
    foo.somethingElse = null;
    var local2/*:**/ = foo.somethingElse || {};
  }/*

}*/function UsingSomePackageGlobal$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: UsingSomePackageGlobal$,
      statics: {main: main$static},
      uses: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.package1.somePackageGlobal"
      ]
    };
});
