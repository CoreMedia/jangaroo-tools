joo.classLoader.prepare("package package1",/* {

import package1.someOtherPackage.SomeOtherClass;*/

// This comment to vanish in API
/**
 * Some package-global documentation;
 */
"public function somePackageGlobalFun",0,function(){var $1=package1.someOtherPackage;return function somePackageGlobalFun(flag/*:Boolean*/)/*:SomeOtherClass*/ {
  return new $1.SomeOtherClass();
}
;},[],["package1.someOtherPackage.SomeOtherClass"], "0.8.0", "0.9.16-SNAPSHOT"

);