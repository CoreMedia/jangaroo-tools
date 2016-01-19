Ext.define("AS3.package1.somePackageGlobalFun", function(somePackageGlobalFun) {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new AS3.package1.someOtherPackage.SomeOtherClass();
}/*

}

============================================== Jangaroo part ==============================================*/
    return {
      factory: function() {
        return somePackageGlobalFun;
      },
      uses: ["AS3.package1.someOtherPackage.SomeOtherClass"]
    };
});
