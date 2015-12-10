Ext.require("package1.someOtherPackage.SomeOtherClass"], function($module,$exports,AS3,SomeOtherClass) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ var somePackageGlobal/*:SomeOtherClass*/
  = new SomeOtherClass._(); $primaryDeclaration({value: somePackageGlobal,writable: true});/*

}
*/
  });
});
