/*package package2 {*/

Ext.define("package2.TestArrowFunctions", function(TestArrowFunctions) {/*public class TestArrowFunctions {

  public*/ function main()/*:void*/ {var _this=this;
    function fun1()/*:Number*/ {
      return 42;
    }

    // forward usage:
    var x/*:Number*/ = fun2(42);
    function fun2(x/*:Number*/)/*:Number*/ {
      return _this.compute(x);
    }

    (function(x/*:Number*/)/*:Number*/ {
      // dynamic, untyped this usage:
      return this.compute(x);
    }).call(this, 42);

    function unused()/*:void*/ {}

    // normal, "backwards" usage:
    var y/*:Number*/ = fun1();
  }/*

  public*/ function main1()/*:String*/ {var _this=this;
    return (function ()/*: String*/ {
      return "" + _this.compute(42);
    })();
  }/*

  public*/ function main2()/*:String*/ {var _this=this;
    return (function namedButUnused()/*: String*/ {
      return "" + _this.compute(42);
    })();
  }/*

  public*/ function main3()/*:String*/ {var _this=this;
    return (function namedAndUsed(x/*: Number*/)/*: String*/ {
      return x === 0 ? "" : _this.compute(x) + namedAndUsed(x - 1);
    })(3);
  }/*

  public*/ function main4()/*:Number*/ {var _this=this;
    return fun3(42);

    function fun3(x/*:Number*/)/*:Number*/ {
      return (function()/*:Number*/ { return _this.compute(x); })();
    }
  }/*

  internal*/ function compute(x/*:Number*/)/*:Number*/ {
    return x + 42;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      main: main,
      main1: main1,
      main2: main2,
      main3: main3,
      main4: main4,
      compute: compute
    };
});
