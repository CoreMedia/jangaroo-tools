package assignment{
public class IncorrectDeclarationFunction {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectDeclarationFunction() {
  }
  public function make():void {
    var x:Function;
    // x IdeExpression, ExpressionType.as3Type=String
    var x:Function = 2;
    var x:Function = "2";
    var x:Function = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    var x:Function = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:Function = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var x:Function = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
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