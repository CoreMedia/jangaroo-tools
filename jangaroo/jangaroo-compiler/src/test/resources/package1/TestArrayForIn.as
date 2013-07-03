package package1 {

public class TestArrayForIn {

  private static const ARRAY:Array = [1, 2, 3];
  private static var array:Array = [1, 2, 3];

  public static function test():Array {
    var a:Array = [1, 2, 3];
    // test rewrite of Array for ... in with local variable
    for (var i1:String in a) {
      doSomething(a[i1]);
    }
    // test rewrite of Array for each ... in with Array literal
    for each (var e0:int in [1, 2, 3]) {
      doSomething(e0);
    }
    // test rewrite of Array for each ... in with local variable
    for each (var e1:int in a) {
      doSomething(e1);
    }
    // test rewrite of Array for each ... in with field
    for each (var e2a:int in array) {
      doSomething(e2a);
    }
    // test rewrite of Array for each ... in with field using explicit class
    for each (var e2b:int in TestArrayForIn.array) {
      doSomething(e2b);
    }
    // test rewrite of Array for each ... in with static const
    for each (var e3:int in ARRAY) {
      doSomething(e3);
    }
    var e4:int;
    // test rewrite of Array for each ... in with pre-declared loop variable
    for each (e4 in a) {
      doSomething(e4);
    }
    // test rewrite of Array for ... in where the first argument is an lvalue
    var indexes:Array = [];
    for (indexes[indexes.length] in a) {}
    doSomething(indexes);

    // test rewrite of Array for each ... in where the first argument is an lvalue
    var copy:Array = [];
    for each (copy[copy.length] in a) {}
    return copy;
  }

  private static function doSomething(param:*):void {
    // do something...
  }
}
}