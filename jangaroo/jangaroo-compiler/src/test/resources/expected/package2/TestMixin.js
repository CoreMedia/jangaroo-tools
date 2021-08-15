/*package package2 {*/

/**
 * Irrelevant implementation comment.
 */
Ext.define("package2.TestMixin", function(TestMixin) {/*public class TestMixin extends Base implements ITestMixin {

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
}*/function TestMixin$() {this.super$6qGj();}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Base",
      getFoo: get$foo,
      setFoo: set$foo,
      mix: mix,
      super$6qGj: function() {
        Ext.Base.prototype.constructor.apply(this, arguments);
      },
      constructor: TestMixin$,
      config: {foo: undefined}
    };
});
