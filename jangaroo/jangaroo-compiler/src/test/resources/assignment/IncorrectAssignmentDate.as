package assignment{
public class IncorrectAssignmentDate {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectAssignmentDate() {
  }

  public function make():void {
    var x:Date;
    // x IdeExpression, ExpressionType.as3Type=String
    x = 1; // LiteralExpression, type=null, jooValue in Java Long
    x = []; // ArrayLiteral, ExpressionType.as3Type = Array
    x = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    x = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    x = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    x = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    x = "1"; // LiteralExpression, type=null, jooValue in Java String
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