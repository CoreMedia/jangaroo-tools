import int from "../AS3/int_";


class TestArrayForIn {

  //@ts-expect-error 18022
  static readonly #ARRAY:Array<any> = [1, 2, 3];
  //@ts-expect-error 18022
  static #array:Array<any> = [1, 2, 3];

  static test():Array<any> {
    var a = [1, 2, 3];
    // test rewrite of Array for ... in with local variable
    for (var i1 in a) {
      TestArrayForIn.#doSomething(a[i1]);
    }
    // test rewrite of Array for each ... in with Array literal
    for (var e0 of [1, 2, 3] as int[]) {
      TestArrayForIn.#doSomething(e0);
    }
    // test rewrite of Array for each ... in with local variable
    for (var e1 of a as int[]) {
      TestArrayForIn.#doSomething(e1);
    }
    // test rewrite of Array for each ... in with field
    for (var e2a of TestArrayForIn.#array as int[]) {
      TestArrayForIn.#doSomething(e2a);
    }
    // test rewrite of Array for each ... in with field using explicit class
    for (var e2b of TestArrayForIn.#array as int[]) {
      TestArrayForIn.#doSomething(e2b);
    }
    // test rewrite of Array for each ... in with static const
    for (var e3 of TestArrayForIn.#ARRAY as int[]) {
      TestArrayForIn.#doSomething(e3);
    }
    var e4:int;
    // test rewrite of Array for each ... in with pre-declared loop variable
    for (e4 of a) {
      TestArrayForIn.#doSomething(e4);
    }
    // test rewrite of Array for ... in where the first argument is an lvalue
    var indexes = [];
    for (indexes[indexes.length] in a) {}
    TestArrayForIn.#doSomething(indexes);

    // test rewrite of Array for each ... in where the first argument is an lvalue
    var copy = [];
    for (copy[copy.length] of a) {}
    return copy;
  }

  //@ts-expect-error 18022
  static #doSomething(param:any):void {
    // do something...
  }
}
export default TestArrayForIn;
