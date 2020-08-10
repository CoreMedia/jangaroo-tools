/*package package2 {*/
Ext.define("package2.TestMixin", function(TestMixin) {/*public class TestMixin implements ITestMixin {

  public*/ function mix(thing/*:String*/)/*:String*/ {
    return "Mixed " + thing + "!";
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {mix: mix};
});
