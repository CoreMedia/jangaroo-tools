define(["runtime/AS3","classes/package1/someOtherPackage/SomeOtherClass"], function(AS3,SomeOtherClass) { "use strict";return AS3.class_(function(){/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

public class ChainedConstants {
  public static const METHOD_TYPE_GET : String = "get";

  public static const DEFAULT_METHOD_TYPE : String =*/function DEFAULT_METHOD_TYPE_(){return( ChainedConstants.METHOD_TYPE_GET);}/*;

  public static const THE_METHOD_TYPE : String =*/function THE_METHOD_TYPE_(){return( ChainedConstants.METHOD_TYPE_GET);}/*;

  public static const ANOTHER_METHOD_TYPE : String =*/function ANOTHER_METHOD_TYPE_(){return( ChainedConstants.METHOD_TYPE_GET.substr(0));}/*;

  public static const THE_BLA : String =*/function THE_BLA_(){return( SomeOtherClass._.BLA);}/*;
}*/function ChainedConstants() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      package_: "package1",
      class_: "ChainedConstants",
      members: {constructor: ChainedConstants},
      staticMembers: {METHOD_TYPE_GET: "get"},
      staticCode: function() {
        Object.defineProperty(this, "DEFAULT_METHOD_TYPE", {value: DEFAULT_METHOD_TYPE_()});
        Object.defineProperty(this, "THE_METHOD_TYPE", {value: THE_METHOD_TYPE_()});
        Object.defineProperty(this, "ANOTHER_METHOD_TYPE", {value: ANOTHER_METHOD_TYPE_()});
        Object.defineProperty(this, "THE_BLA", {value: THE_BLA_()});
      }
    };
  });
});
