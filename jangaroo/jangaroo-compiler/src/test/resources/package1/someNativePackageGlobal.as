package package1 {

import package1.someOtherPackage.SomeOtherClass;

// This comment to vanish in API
/**
 * Some native package-global documentation;
 */
[Native("foo.somethingElse")]
public var someNativePackageGlobal:SomeOtherClass;

}