/*package package1 {

// This comment to vanish in API
[Lazy]*/
/**
 * Some package-global documentation;
 */
Ext.define("package1.somePackageGlobalLazyConst", function(somePackageGlobalLazyConst) {/*public const somePackageGlobalLazyConst:Object =*/function somePackageGlobalLazyConst_(){return( {});}/*;

}

============================================== Jangaroo part ==============================================*/
    return {__lazyFactory__: somePackageGlobalLazyConst_};
});
