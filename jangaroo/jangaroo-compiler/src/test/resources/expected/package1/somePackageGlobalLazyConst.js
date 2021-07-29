/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
[Lazy]*/
/**
 * Some package-global documentation;
 */
Ext.define("package1.somePackageGlobalLazyConst", function(somePackageGlobalLazyConst) {/*public const somePackageGlobalLazyConst:SomeOtherClass
  =*/function somePackageGlobalLazyConst_(){return( new package1.someOtherPackage.SomeOtherClass());}/*;

}

============================================== Jangaroo part ==============================================*/
    return {
      __lazyFactory__: somePackageGlobalLazyConst_,
      requires: ["package1.someOtherPackage.SomeOtherClass"]
    };
});
