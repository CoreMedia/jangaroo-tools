package package1 {
import package1.someOtherPackage.SomeOtherClass;

[Uses("package1.someOtherPackage.SomeOtherClass")]
[ExcludeClass]
/**
 * Some package-global documentation;
 */
public var somePackageGlobal:package1.someOtherPackage.SomeOtherClass;
}