import int from '../AS3/int_';

class TestStatementStartingWithArrayLiteral {

  static sum1_2_3():int {
    var result = 0;
    [1, 2, 3].forEach((x:int):void => { result += x; });
    return result;
  }

}
export default TestStatementStartingWithArrayLiteral;
