import SomeOtherClass from "./someOtherPackage/SomeOtherClass";


// This comment to vanish in API
/**
 * Some package-global documentation;
 */
function somePackageGlobalFun(flag:boolean):SomeOtherClass {
  return new SomeOtherClass();
}
export default somePackageGlobalFun;
