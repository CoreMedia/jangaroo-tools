Ext.define("package2.TestMixin", function(TestMixin) {/*package package2 {
public class TestMixin implements ITestMixin {

  public*/ function mix(thing/*:String*/)/*:String*/ {
    return "Mixed " + thing + "!";
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {mix: mix};
});
