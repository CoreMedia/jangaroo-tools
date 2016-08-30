package assignment{
public class IncorrectAssignmentNumber {

  [ArrayElementType("String")]
  private var anArrayOfStrings:Array = ["1"];

  public function IncorrectAssignmentNumber() {
  }

  public function make():void {
    var x:Number;
    // x IdeExpression, ExpressionType.as3Type=String
    x = []; // ArrayLiteral, ExpressionType.as3Type = Array
    x = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    x = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    x = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    x = anArrayOfStrings[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    // x = new Vector();
  }

  private static function getObject():Object {
    return "juhu";
  }

  private function thisIsAVoidFunction():void {
    // do nothing
  }
}
}