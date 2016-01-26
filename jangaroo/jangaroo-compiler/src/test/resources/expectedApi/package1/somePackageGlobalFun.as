package package1 {
import package1.someOtherPackage.SomeOtherClass;

[Uses ("package1.someOtherPackage.SomeOtherClass")]
/**
 * Some package-global documentation;
 */
public native function somePackageGlobalFun(flag:Boolean):SomeOtherClass;
}