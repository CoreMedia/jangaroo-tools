Ext.ns("AS3.package1");Ext.require([], function() {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public var*/AS3.package1. uninitializedPackageGlobal/*:SomeOtherClass*/=null;/*

}
*/});
Ext.ClassManager.triggerCreated("AS3.package1.uninitializedPackageGlobal");
