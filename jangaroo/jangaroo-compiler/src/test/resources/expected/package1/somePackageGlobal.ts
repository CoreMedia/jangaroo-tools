import { lazyVar } from "@jangaroo/runtime/AS3";
import SomeOtherClass from "./someOtherPackage/SomeOtherClass";


// This comment to vanish in API

/**
 * Some package-global documentation;
 */
const somePackageGlobal:{_: SomeOtherClass}
  =lazyVar(() => new SomeOtherClass());
export default somePackageGlobal;
