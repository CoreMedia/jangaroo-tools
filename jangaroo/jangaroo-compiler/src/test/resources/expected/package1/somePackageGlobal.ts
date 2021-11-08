import { lazyVar } from "@jangaroo/runtime";
import SomeOtherClass from "./someOtherPackage/SomeOtherClass";


// This comment to vanish in API

/**
 * Some package-global documentation;
 */
const somePackageGlobal:{_: SomeOtherClass}
  =lazyVar(() => new SomeOtherClass());
export default somePackageGlobal;
