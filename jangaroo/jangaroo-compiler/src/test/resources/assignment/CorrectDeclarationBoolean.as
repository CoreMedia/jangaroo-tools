package assignment{
public class CorrectDeclarationBoolean {

  [ArrayElementType("String")]
  private var anArrayOfNumbers:Array = ["1"];

  public function CorrectDeclarationBoolean() {
  }
  public function make():void {
    var x:Boolean;
    var x:Boolean = getSomething();
    var x:Boolean = []; // ArrayLiteral, ExpressionType.as3Type = Array
    var x:Boolean = getObject; // IdeExpression, FunctionSignature.as3Type=Function
    var x:Boolean = anArrayOfNumbers[0]; // ArrayIndexExpr, ExpressionType.type.as3Type=Number
    var x:Boolean = 1;
  }

  public static function getSomething():* {
    return "something";
  }

  private static function getObject():Object {
    return "juhu";
  }
}
}