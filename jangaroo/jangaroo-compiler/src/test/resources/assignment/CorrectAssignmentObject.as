package assignment{
public class CorrectAssignmentObject {

  [ArrayElementType("String")]
  private var anArrayOfNumbers:Array = ["1"];

  public function CorrectAssignmentObject() {
  }
  public function make():void {
    var x:Object;
    x = getSomething();
    x = []; // ArrayLiteral, ExpressionType.as3Type = Array
    x = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    x = getString; // IdeExpression, FunctionSignature.as3Type=Function
    x = getString(); // ApplyExpr, ExpressionType.as3Type.Object
    x = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    x = 1;
  }

  public static function getSomething():* {
    return "something";
  }

  private static function getString():* {
    return "juhu";
  }

  private function thisIsAVoidFunction():void {
    // do nothing
  }
}
}