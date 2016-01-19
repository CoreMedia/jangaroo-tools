Ext.define("AS3.package1.somePackageGlobal", function(somePackageGlobal) {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public var somePackageGlobal:SomeOtherClass
  =*/function somePackageGlobal_(){return( new AS3.package1.someOtherPackage.SomeOtherClass());}/*;

}

============================================== Jangaroo part ==============================================*/
    return {
      factory: somePackageGlobal_,
      requires: ["AS3.package1.someOtherPackage.SomeOtherClass"]
    };
});
