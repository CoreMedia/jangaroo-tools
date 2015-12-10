Ext.define("package1.ChainedConstants", function(ChainedConstants) {/*package package1 {

import package1.someOtherPackage.SomeOtherClass;

public class ChainedConstants {
  public static const METHOD_TYPE_GET : String = "get";

  public static const DEFAULT_METHOD_TYPE : String =*/function DEFAULT_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"DEFAULT_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET});}/*;

  public static const THE_METHOD_TYPE : String =*/function THE_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"THE_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET});}/*;

  public static const ANOTHER_METHOD_TYPE : String =*/function ANOTHER_METHOD_TYPE$static_(){Object.defineProperty(ChainedConstants,"ANOTHER_METHOD_TYPE",{value: ChainedConstants.METHOD_TYPE_GET.substr(0)});}/*;

  public static const THE_BLA : String =*/function THE_BLA$static_(){Object.defineProperty(ChainedConstants,"THE_BLA",{value: package1.someOtherPackage.SomeOtherClass.BLA});}/*;
}*/function ChainedConstants$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: ChainedConstants$,
      statics: {METHOD_TYPE_GET: "get"}
    };
}, function() {
    DEFAULT_METHOD_TYPE$static_();
    THE_METHOD_TYPE$static_();
    ANOTHER_METHOD_TYPE$static_();
    THE_BLA$static_();
});
