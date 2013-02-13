define(["exports","as3-rt/AS3","as3/package1/someOtherPackage/SomeOtherClass"], function($exports,AS3,SomeOtherClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ var somePackageGlobal/*:SomeOtherClass*/
  = new (SomeOtherClass._||SomeOtherClass._$get())(); $primaryDeclaration({value: somePackageGlobal,writable: true});/*

}
*/
  });
});
