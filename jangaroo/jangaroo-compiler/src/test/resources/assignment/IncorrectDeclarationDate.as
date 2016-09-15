package assignment{
public class IncorrectDeclarationDate {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectDeclarationDate() {
  }

  public function make():void {
    var x:Date = new Date();
    // x IdeExpression, ExpressionType.as3Type=String
    var x:Date = 1; // LiteralExpression, type=null, jooValue in Java Long
    var x:Date = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:Date = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var x:Date = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    var x:Date = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    var x:Date = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    var x:Date = "1"; // LiteralExpression, type=null, jooValue in Java String
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