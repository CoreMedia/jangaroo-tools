/*package package2 {
import ext.Ext;*/

Ext.define("package2.TestExtApply", function(TestExtApply) {/*public class TestExtApply {*/
  function TestExtApply$(config/*: TestExtApply*/) {
    Ext.apply(config, this);
    Ext.apply({ a: "target" }, { a: "apply" }, { a: "defaults"});
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestExtApply$,
      uses: ["Ext"]
    };
});
