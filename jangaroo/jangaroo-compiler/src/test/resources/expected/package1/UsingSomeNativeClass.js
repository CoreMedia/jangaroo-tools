define(["exports","as3-rt/AS3","native!package1.SomeNativeClass@acme/native","as3/package1/someOtherPackage/SomeNativeClass"], function($exports,AS3,package1$SomeNativeClass,package1$someOtherPackage$SomeNativeClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeNativeClass;

/**
 * This is an example of a class using a "native" class.
 * /
public class UsingSomeNativeClass {

  public var someNative:package1.SomeNativeClass =*/function someNative_(){this.someNative=( new package1$SomeNativeClass());}/*;
  public native function get someNative2():package1.SomeNativeClass;

  public*/ function UsingSomeNativeClass() {someNative_.call(this);
    new (package1$someOtherPackage$SomeNativeClass._||package1$someOtherPackage$SomeNativeClass._$get())();
    this.someNative.setBaz ( "foo");
    this.someNative2.setBaz ( "foo");
    var foo = this.getFoobar();
    var bar = this.getAnotherNativeAccessor();
  }/*

  [Accessor("getFoobar")]
  public*/ function get$someNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*

  [Accessor]
  public*/ function get$anotherNativeAccessor()/*:package1.SomeNativeClass*/ {
    return this.someNative;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "UsingSomeNativeClass",
      members: {
        constructor: UsingSomeNativeClass,
        getFoobar: get$someNativeAccessor,
        getAnotherNativeAccessor: get$anotherNativeAccessor
      }
    }));
  });
});
