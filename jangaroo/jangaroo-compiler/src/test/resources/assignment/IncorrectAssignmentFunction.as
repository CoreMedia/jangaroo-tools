package assignment{
public class IncorrectAssignmentFunction {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectAssignmentFunction() {
  }
  public function make():void {
    var x:Function;
    // x IdeExpression, ExpressionType.as3Type=String
    x = 2;
    x = "2";
    x = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    x = []; // ArrayLiteral, ExpressionType.as3Type = Array
    x = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    x = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
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