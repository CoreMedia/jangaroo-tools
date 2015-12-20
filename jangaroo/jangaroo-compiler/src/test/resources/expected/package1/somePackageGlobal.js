Ext.ns("AS3.package1");Ext.require(["AS3.package1.someOtherPackage.SomeOtherClass"], function() {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public var*/AS3.package1. somePackageGlobal/*:SomeOtherClass*/
  = new AS3.package1.someOtherPackage.SomeOtherClass();/*

}
*/});
Ext.ClassManager.triggerCreated("AS3.package1.somePackageGlobal");
