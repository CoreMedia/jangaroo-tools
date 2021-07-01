

class TestArrowFunctions {

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

  compute(x:number):number {
    return x + 42;
  }
}
export default TestArrowFunctions;
