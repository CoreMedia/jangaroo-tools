define("as3/package1/ChainedConstants",["exports","as3-rt/AS3","as3/package1/someOtherPackage/SomeOtherClass"], function($exports,AS3,SomeOtherClass) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

public class ChainedConstants {
  public static const METHOD_TYPE_GET : String = "get";

  public static const DEFAULT_METHOD_TYPE : String =*/function DEFAULT_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"DEFAULT_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET});}/*;

  public static const THE_METHOD_TYPE : String =*/function THE_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"THE_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET});}/*;

  public static const ANOTHER_METHOD_TYPE : String =*/function ANOTHER_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"ANOTHER_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET.substr(0)});}/*;

  public static const THE_BLA : String =*/function THE_BLA$static_(){Object.defineProperty(ChainedConstants,"THE_BLA",{value: SomeOtherClass._.BLA});}/*;
}*/function ChainedConstants() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "ChainedConstants",
      members: {constructor: ChainedConstants},
      staticMembers: {METHOD_TYPE_GET: "get"}
    }));
    DEFAULT_METHOD_TYPE$static_();
    THE_METHOD_TYPE$static_();
    ANOTHER_METHOD_TYPE$static_();
    THE_BLA$static_();
  });
});
