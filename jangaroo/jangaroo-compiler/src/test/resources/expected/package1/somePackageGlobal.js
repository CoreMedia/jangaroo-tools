define("as3/package1/somePackageGlobal",["module","as3-rt/AS3","as3/package1/someOtherPackage/SomeOtherClass"], function($module,AS3,SomeOtherClass) { AS3.compilationUnit($module,function($primaryDeclaration){/*package package1 {

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
