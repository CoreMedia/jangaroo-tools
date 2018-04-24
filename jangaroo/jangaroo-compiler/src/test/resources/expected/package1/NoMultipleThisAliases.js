Ext.define("package1.NoMultipleThisAliases", function(NoMultipleThisAliases) {/*package package1{
public class NoMultipleThisAliases {
  public*/ function NoMultipleThisAliases$() {var this$=this;
    function foo1()/*:void*/ {
      this$.method$6kMi();
    }
    function foo2()/*:void*/ {
      this$.method$6kMi();
    }
  }/*

  private*/ function method()/*:void*/ {}/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: NoMultipleThisAliases$,
      method$6kMi: method
    };
});
