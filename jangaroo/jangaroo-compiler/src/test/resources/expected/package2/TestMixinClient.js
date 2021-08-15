/*package package2 {*/

Ext.define("package2.TestMixinClient", function(TestMixinClient) {/*public class TestMixinClient extends Base implements ITestMixin {

  [ExtConfig]
  public var thing: String;

  public*/ function TestMixinClient$(config/*: TestMixinClient = null*/) {if(arguments.length<=0)config=null;this.super$Y8vO();
    this.mix(config.thing);
  }/*

  /** @inheritDoc * /
  [Bindable]
  public native function get foo():Number;

  /** @private * /
  [Bindable]
  public native function set foo(value:Number):void;

  /** @inheritDoc * /
  public native function mix(thing:String):String;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Base",
      mixins: ["package2.TestMixin"],
      thing: null,
      constructor: TestMixinClient$,
      super$Y8vO: function() {
        Ext.Base.prototype.constructor.apply(this, arguments);
      },
      requires: ["package2.TestMixin"]
    };
});
