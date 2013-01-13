define(["exports","runtime/AS3"], function($exports,AS3) { "use strict"; AS3.class_($exports, function(){/*package package1 {

public class AuxVarConfusion {

  public*/ function doSomething()/*:void*/ {var $2;
    for/* each*/ (var $1 in $2= {foo:true}) {var i=$2[$1];
      new AuxVarConfusion();
    }
  }/*

}*/function AuxVarConfusion() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      package_: "package1",
      class_: "AuxVarConfusion",
      members: {
        doSomething: doSomething,
        constructor: AuxVarConfusion
      }
    };
  });
});
