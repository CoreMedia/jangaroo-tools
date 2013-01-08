define(["runtime/AS3"], function(AS3) { "use strict";return AS3.class_(function(){/*package package1{
public class NoMultipleThisAliases {
  public*/ function NoMultipleThisAliases() {var this$=this;
    function foo1()/*:void*/ {
      this$.method$1();
    }
    function foo2()/*:void*/ {
      this$.method$1();
    }
  }/*

  private*/ function method()/*:void*/ {}/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      package_: "package1",
      class_: "NoMultipleThisAliases",
      members: {
        constructor: NoMultipleThisAliases,
        method$1: method
      }
    };
  });
});
