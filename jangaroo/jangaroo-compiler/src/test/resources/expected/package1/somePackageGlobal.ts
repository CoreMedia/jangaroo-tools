import SomeOtherClass from './someOtherPackage/SomeOtherClass';


// This comment to vanish in API

/**
 * Some package-global documentation;
 */
 var somePackageGlobal:{_: SomeOtherClass}
  ={_:  new SomeOtherClass()};
export default somePackageGlobal;
