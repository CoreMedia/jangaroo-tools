package assignment{
public class IncorrectDeclarationNumber {

  [ArrayElementType("String")]
  private var anArrayOfStrings:Array = ["1"];

  public function IncorrectDeclarationNumber() {
  }

  public function make():void {
    var x:Number;
    // x IdeExpression, ExpressionType.as3Type=String
    var x:Number = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:Number = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var x:Number = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    var x:Number = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    var x:Number = anArrayOfStrings[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
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