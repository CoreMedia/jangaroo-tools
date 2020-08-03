/*package package1 {

import package1.someOtherPackage.SomeOtherClass;*/

Ext.define("package1.ChainedConstants", function(ChainedConstants) {/*public class ChainedConstants {
  public static const METHOD_TYPE_GET : String = "get";

  public static const DEFAULT_METHOD_TYPE : String =*/function DEFAULT_METHOD_TYPE$static_(){ChainedConstants.DEFAULT_METHOD_TYPE=( ChainedConstants.METHOD_TYPE_GET);}/*;

  public static const THE_METHOD_TYPE : String =*/function THE_METHOD_TYPE$static_(){ChainedConstants.THE_METHOD_TYPE=( ChainedConstants.METHOD_TYPE_GET);}/*;

  public static const ANOTHER_METHOD_TYPE : String =*/function ANOTHER_METHOD_TYPE$static_(){ChainedConstants.ANOTHER_METHOD_TYPE=( ChainedConstants.METHOD_TYPE_GET.substr(0, 1));}/*;

  public static const THE_BLA : int =*/function THE_BLA$static_(){ChainedConstants.THE_BLA=( package1.someOtherPackage.SomeOtherClass.BLA);}/*;
}*/function ChainedConstants$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: ChainedConstants$,
      statics: {
        METHOD_TYPE_GET: "get",
        DEFAULT_METHOD_TYPE: undefined,
        THE_METHOD_TYPE: undefined,
        ANOTHER_METHOD_TYPE: undefined,
        THE_BLA: undefined,
        __initStatics__: function() {
          DEFAULT_METHOD_TYPE$static_();
          THE_METHOD_TYPE$static_();
          ANOTHER_METHOD_TYPE$static_();
          THE_BLA$static_();
        }
      },
      uses: ["package1.someOtherPackage.SomeOtherClass"]
    };
});
