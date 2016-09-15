package assignment{
public class CorrectAssignmentBoolean {

  [ArrayElementType("String")]
  private var anArrayOfNumbers:Array = ["1"];

  public function CorrectAssignmentBoolean() {
  }
  public function make():void {
    var x:Boolean;
    x = getSomething();
    x = []; // ArrayLiteral, ExpressionType.as3Type = Array
    x = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    x = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    x = 1;
  }

  public static function getSomething():* {
    return "something";
  }

  private static function getObject():Object {
    return "juhu";
  }
}
}