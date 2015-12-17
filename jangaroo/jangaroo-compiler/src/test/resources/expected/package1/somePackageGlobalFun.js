Ext.ns("package1");Ext.require(["package1.someOtherPackage.SomeOtherClass"], function() {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;*/package1.somePackageGlobalFun=/*

// This comment to vanish in API
/**
 * Some package-global documentation;
 * /
public*/ function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new package1.someOtherPackage.SomeOtherClass();
}/*

}
*/});
