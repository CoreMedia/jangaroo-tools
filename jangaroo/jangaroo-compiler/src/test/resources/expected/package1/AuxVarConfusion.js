Ext.define("AS3.package1.AuxVarConfusion", function(AuxVarConfusion) {/*package package1 {

public class AuxVarConfusion {

  public*/ function doSomething()/*:void*/ {var $2;
    for/* each*/ (var $1 in $2= {foo:true}) {var i=$2[$1];
      new AuxVarConfusion();
    }
  }/*

}*/function AuxVarConfusion$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      doSomething: doSomething,
      constructor: AuxVarConfusion$
    };
});
