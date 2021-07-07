package package2 {

public class TestArrowFunctions {

  public function main():void {
    function fun1():Number {
      return 42;
    }

    // forward usage:
    var x:Number = fun2(42);
    function fun2(x:Number):Number {
      return compute(x);
    }

    (function(x:Number):Number {
      // dynamic, untyped this usage:
      return this.compute(x);
    }).call(this, 42);

    function unused():void {}

    // normal, "backwards" usage:
    var y:Number = fun1();
  }

  public function main1():String {
    return (function (): String {
      return "" + compute(42);
    })();
  }

  public function main2():String {
    return (function namedButUnused(): String {
      return "" + compute(42);
    })();
  }

  public function main3():String {
    return (function namedAndUsed(x: Number): String {
      return x === 0 ? "" : compute(x) + namedAndUsed(x - 1);
    })(3);
  }

  public function main4():Number {
    return fun3(42);

    function fun3(x:Number):Number {
      return (function():Number { return compute(x); })();
    }
  }

  public function main5():void {
    var fun4 = function() {
      function fun5():void {
        compute(42);
      }
    }
  }

  internal function compute(x:Number):Number {
    return x + 42;
  }
}
}