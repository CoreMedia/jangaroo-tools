import { lazyConst } from "@jangaroo/runtime/AS3";
import SomeOtherClass from "./someOtherPackage/SomeOtherClass";


// This comment to vanish in API

/**
 * Some package-global documentation;
 */
const somePackageGlobalLazyConst:{readonly _: SomeOtherClass}
  =lazyConst(() => new SomeOtherClass());
export default somePackageGlobalLazyConst;
