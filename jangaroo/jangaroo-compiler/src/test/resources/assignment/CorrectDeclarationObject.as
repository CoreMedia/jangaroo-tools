package assignment{
public class CorrectDeclarationObject {

  [ArrayElementType("String")]
  private var anArrayOfNumbers:Array = ["1"];

  public function CorrectDeclarationObject() {
  }
  public function make():void {
    var x:Object;
    var x:Object = getSomething();
    var x:Object = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:Object = thisIsAVoidFunction(); // ExpressionType, ExpressionType.as3Type=void
    var x:Object = getString; // IdeExpression, FunctionSignature.as3Type=Function
    var x:Object = getString(); // ApplyExpr, ExpressionType.as3Type.Object
    var x:Object = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    var x:Object = 1;
  }

  public static function getSomething():* {
    return "something";
  }

  private static function getString():String {
    return "juhu";
  }

  private function thisIsAVoidFunction():void {
    // do nothing
  }
}
}