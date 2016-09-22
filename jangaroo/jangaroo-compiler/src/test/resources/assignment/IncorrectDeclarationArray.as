package assignment{
public class IncorrectDeclarationArray {

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


  public function IncorrectDeclarationArray() {
  }
  public function make():void {
    var a1:Array = 1; // LiteralExpression, type=null, jooValue in Java Long
    var a2:Array = "juhu"; // ArrayLiteral, ExpressionType.as3Type = Array
    var a3:Array = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var a4:Array = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    var a5:Array = getObject(); // ApplyExpr, ExpressionType.as3Type.Object
    var a6:Array = anArrayOfNumbers[0];

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