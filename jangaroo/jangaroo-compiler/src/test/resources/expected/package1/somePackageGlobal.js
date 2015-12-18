Ext.ns("package1");Ext.require(["package1.someOtherPackage.SomeOtherClass"], function() {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public var*/package1. somePackageGlobal/*:SomeOtherClass*/
  = new package1.someOtherPackage.SomeOtherClass();/*

}
*/});
Ext.ClassManager.triggerCreated("package1.somePackageGlobal");
