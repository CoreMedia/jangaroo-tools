import { lazyConst } from "@jangaroo/runtime";


// This comment to vanish in API

/**
 * Some package-global documentation;
 */
const somePackageGlobalLazyConst:{readonly _: any} =lazyConst(() => ({}));
export default somePackageGlobalLazyConst;
