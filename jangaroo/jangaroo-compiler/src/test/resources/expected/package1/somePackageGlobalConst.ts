import SomeOtherClass from "./someOtherPackage/SomeOtherClass";


// This comment to vanish in API
/**
 * Some package-global documentation;
 */
const somePackageGlobalConst:{readonly _: SomeOtherClass}
  ={_:  new SomeOtherClass()};
export default somePackageGlobalConst;
