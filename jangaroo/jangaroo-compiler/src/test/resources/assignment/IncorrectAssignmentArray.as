package assignment{
public class IncorrectAssignmentArray {

  [ArrayElementType("Array")]
  private var nestedArray:Array = [[]];

  [ArrayElementType("String")]
  private var stringArray:Array = [[]];

  [ArrayElementType("assignment.Square")]
  private var squareArray:Array = [new Square()];

  [ArrayElementType("assignment.Rectangle")]
  private var rectangleArray:Array = [new Rectangle()];

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  // TODO  what about vectors
  // private var aVector:Vector

  [ArrayElementType("String")]
  private var anArrayOfStrings:Array = ["1"];


  public function IncorrectAssignmentArray() {
  }
  public function make():void {
    var x:Array;

    x = 1; // LiteralExpression, type=null, jooValue in Java Long
    x = "juhu"; // ArrayLiteral, ExpressionType.as3Type = Array
    x = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    x = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    x = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    x = anArrayOfNumbers[0];

    stringArray = nestedArray;
    nestedArray = stringArray;
    squareArray = rectangleArray;
    // x = aVector.
  }

  private static function getObject():Object {
    return "juhu";
  }

  private function thisIsAVoidFunction():void {
    // do nothing
  }
}
}