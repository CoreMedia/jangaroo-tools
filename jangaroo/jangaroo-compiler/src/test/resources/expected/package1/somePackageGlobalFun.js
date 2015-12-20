Ext.ns("AS3.package1");Ext.require(["AS3.package1.someOtherPackage.SomeOtherClass"], function() {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;*/AS3.package1.somePackageGlobalFun=/*

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new AS3.package1.someOtherPackage.SomeOtherClass();
}/*

}
*/});
Ext.ClassManager.triggerCreated("AS3.package1.somePackageGlobalFun");
