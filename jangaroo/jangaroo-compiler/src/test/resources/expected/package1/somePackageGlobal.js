/*package package1 {

import package1.someOtherPackage.SomeOtherClass;*/

// This comment to vanish in API
/**
 * Some package-global documentation;
 */
Ext.define("package1.somePackageGlobal", function(somePackageGlobal) {/*public var somePackageGlobal:SomeOtherClass
  =*/function somePackageGlobal_(){return( new package1.someOtherPackage.SomeOtherClass());}/*;

}

============================================== Jangaroo part ==============================================*/
    return {
      __factory__: somePackageGlobal_,
      requires: ["package1.someOtherPackage.SomeOtherClass"]
    };
});
