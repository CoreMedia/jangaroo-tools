Ext.define("AS3.package2.TestMixin", function(TestMixin) {/*package package2 {
public class TestMixin implements ITestMixin {

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
