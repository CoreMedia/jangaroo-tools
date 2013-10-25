define(["exports","as3-rt/AS3","as3/package1/somePackageGlobal","as3/package1/someOtherPackage/SomeOtherClass"], function($exports,AS3,somePackageGlobal,SomeOtherClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeOtherClass;

/**
 * This is an example of a class using a "package global" variable.
 * /
public class UsingSomePackageGlobal {

  public static*/ function main$static()/*:void*/ {
    somePackageGlobal._ = new SomeOtherClass._();
    var local/*:Object*/ = somePackageGlobal._ || {};
  }/*

}*/function UsingSomePackageGlobal() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "UsingSomePackageGlobal",
      members: {constructor: UsingSomePackageGlobal},
      staticMembers: {main: main$static}
    }));
  });
});
