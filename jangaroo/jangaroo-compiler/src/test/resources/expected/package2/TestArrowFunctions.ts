import int from "../AS3/int_";


class TestArrowFunctions {

  #myField: AnyFunction = (this$ => function():string {
    this$.main(); // outer this access
    return this.untyped; // own this access
  })(this);

  main():void {const this$=this;
    function fun1():number {
      return 42;
    }

    // forward usage:
    var x = fun2(42);
    function fun2(x:number):number {
      return this$.compute(x);
    }

    (function(x:number):number {
      // dynamic, untyped this usage:
      return this.compute(x);
    }).call(this, 42);

    function unused():void {}

    // normal, "backwards" usage:
    var y = fun1();
  }

  main1():string {
    return ((): string => 
       "" + this.compute(42)
    )();
  }

  main2():string {
    return ((): string => 
       "" + this.compute(42)
    )();
  }

  main3():string {const this$=this;
    return (function namedAndUsed(x: number): string {
      return x === 0 ? "" : this$.compute(x) + namedAndUsed(x - 1);
    })(3);
  }

  main4():number {const this$=this;
    return fun3(42);

    function fun3(x:number):number {
      return (():number =>   this$.compute(x) )();
    }
  }

  main5():void {const this$=this;
    var fun4 = () => {
      function fun5():void {
        this$.compute(42);
      }
    };
  }

  main6():void {
    var fun5 = () => {
      for (var i = 0; i < 42; ++i) {
        this.compute(42);
      }
    };
  }

  compute(x:number):number {
    return x + 42;
  }
}
export default TestArrowFunctions;
