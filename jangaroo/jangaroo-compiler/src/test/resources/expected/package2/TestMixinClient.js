Ext.define("package2.TestMixinClient", function(TestMixinClient) {/*package package2 {

public class TestMixinClient implements ITestMixin {

  public*/ function TestMixinClient$(thing/*:String*/) {
    this.mix(thing);
  }/*

  /** @inheritDoc * /
  public native function mix(thing:String):String;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      mixins: ["package2.TestMixin"],
      constructor: TestMixinClient$,
      requires: ["package2.TestMixin"]
    };
});
