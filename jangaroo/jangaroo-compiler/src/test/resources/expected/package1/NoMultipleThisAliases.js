/*package package1{*/
Ext.define("package1.NoMultipleThisAliases", function(NoMultipleThisAliases) {/*public class NoMultipleThisAliases {
  public*/ function NoMultipleThisAliases$() {var _this=this;
    function foo1()/*:void*/ {
      _this.method$6kMi();
    }
    function foo2()/*:void*/ {
      _this.method$6kMi();
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
