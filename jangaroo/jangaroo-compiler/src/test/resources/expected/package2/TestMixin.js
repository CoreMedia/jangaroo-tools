/*package package2 {*/

/**
 * Irrelevant implementation comment.
 */
Ext.define("package2.TestMixin", function(TestMixin) {/*public class TestMixin implements ITestMixin {

  /** @inheritDoc * /
  [Bindable]
  public*/ function get$foo()/*:Number*/ {
    return 42;
  }/*

  /** @private * /
  [Bindable]
  public*/ function set$foo(value/*:Number*/)/*:void*/ {
    // do nothing. 42 is perfect.
  }/*

  /**
   * @inheritDoc
   * /
  public*/ function mix(thing/*:String*/)/*:String*/ {
    return "Mixed " + thing + "!";
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      getFoo: get$foo,
      setFoo: set$foo,
      mix: mix,
      config: {foo: undefined}
    };
});
