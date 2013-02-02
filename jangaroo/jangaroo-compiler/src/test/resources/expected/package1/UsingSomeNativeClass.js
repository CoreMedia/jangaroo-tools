define(["exports","runtime/AS3","native!package1.SomeNativeClass@acme/native"], function($exports,AS3,SomeNativeClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {

/**
 * This is an example of a class using a "native" class.
 * /
public class UsingSomeNativeClass {

  public var someNative:SomeNativeClass =*/function someNative_(){this.someNative=( new SomeNativeClass());}/*;

}*/function UsingSomeNativeClass() {someNative_.call(this);}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "UsingSomeNativeClass",
      members: {constructor: UsingSomeNativeClass}
    }));
  });
});
