define(["exports","runtime/AS3","classes/package1/someOtherPackage/SomeOtherClass"], function($exports, AS3,SomeOtherClass) { "use strict";AS3.global_($exports, function(){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new SomeOtherClass._();
} Object.defineProperty(this, "_", { value: somePackageGlobalFun });/*

}
*/
  });
});
