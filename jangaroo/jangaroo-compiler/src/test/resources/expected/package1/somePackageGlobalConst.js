/*package package1 {

import ext.mixin.IObservable;

import package1.someOtherPackage.SomeOtherClass;*/

// This comment to vanish in API
/**
 * Some package-global documentation;
 */
Ext.define("package1.somePackageGlobalConst", function(somePackageGlobalConst) {/*public const somePackageGlobalConst:IObservable
  =*/function somePackageGlobalConst_(){return( new package1.someOtherPackage.SomeOtherClass());}/*;

}

============================================== Jangaroo part ==============================================*/
    return {
      __factory__: somePackageGlobalConst_,
      requires: ["package1.someOtherPackage.SomeOtherClass"]
    };
});
