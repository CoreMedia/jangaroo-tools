define(["runtime/AS3","classes/package1/someOtherPackage/SomeOtherClass"], function(AS3,SomeOtherClass) { "use strict";return AS3.global_(function(){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ var somePackageGlobal/*:SomeOtherClass*/
  = new SomeOtherClass._(); Object.defineProperty(this, "_", {value: somePackageGlobal,writable: true});/*

}
*/
  });
});
