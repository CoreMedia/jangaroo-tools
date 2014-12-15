define("as3/package1/AuxVarConfusion",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {

public class AuxVarConfusion {

  public*/ function doSomething()/*:void*/ {var $2;
    for/* each*/ (var $1 in $2= {foo:true}) {var i=$2[$1];
      new AuxVarConfusion();
    }
  }/*

}*/function AuxVarConfusion() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      doSomething: doSomething,
      constructor: AuxVarConfusion
    }}));
  });
});
