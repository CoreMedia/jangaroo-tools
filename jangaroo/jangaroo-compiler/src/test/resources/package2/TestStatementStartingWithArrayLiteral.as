package package2 {
public class TestStatementStartingWithArrayLiteral {

  public static function sum1_2_3():int {
    var result:int = 0;
    [1, 2, 3].forEach(function(x:int):void { result += x; });
    return result;
  }

}
}