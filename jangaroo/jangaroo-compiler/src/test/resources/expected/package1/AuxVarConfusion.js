/*package package1 {*/

Ext.define("package1.AuxVarConfusion", function(AuxVarConfusion) {/*public class AuxVarConfusion {

  public*/ function doSomething()/*:void*/ {var $2;
    for/* each*/ (var $1 in $2= {foo:true}) {var i=$2[$1];
      new AuxVarConfusion();
    }
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {doSomething: doSomething};
});
