Ext.define("package1.somePackageGlobalFun", function(somePackageGlobalFun) {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new package1.someOtherPackage.SomeOtherClass();
}/*

}

============================================== Jangaroo part ==============================================*/
    return {
      __factory__: function() {
        return somePackageGlobalFun;
      },
      requires: ["package1.someOtherPackage.SomeOtherClass"]
    };
});
