/*package package2 {*/
Ext.define("package2.TestMixin", function(TestMixin) {/*public class TestMixin implements ITestMixin {

  public*/ function mix(thing/*:String*/)/*:String*/ {
    return "Mixed " + thing + "!";
  }/*
}*/function TestMixin$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      mix: mix,
      constructor: TestMixin$
    };
});
