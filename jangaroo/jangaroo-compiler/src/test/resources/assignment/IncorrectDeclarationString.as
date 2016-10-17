package assignment{
public class IncorrectDeclarationString {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectDeclarationString() {
  }

  public function make():void {
    var x:String;
    // x IdeExpression, ExpressionType.as3Type=String
    var x:String = 1; // LiteralExpression, type=null, jooValue in Java Long
    var x:String = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:String = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var x:String = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    var x:String = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    var x:String = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
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