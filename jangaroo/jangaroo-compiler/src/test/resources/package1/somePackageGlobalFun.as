package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some package-global documentation;
 */
public function somePackageGlobalFun(flag:Boolean):SomeOtherClass {
  return new SomeOtherClass();
}

}