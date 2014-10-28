define("as3/package1/NoMultipleThisAliases",["module","as3-rt/AS3"], function($module,AS3) { AS3.compilationUnit($module,function($primaryDeclaration){/*package package1{
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
    $primaryDeclaration(AS3.class_($module, {members: {
      constructor: NoMultipleThisAliases,
      method$1: method
    }}));
  });
});
