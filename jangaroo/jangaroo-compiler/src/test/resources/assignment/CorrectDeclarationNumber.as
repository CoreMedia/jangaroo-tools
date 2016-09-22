package assignment{
public class CorrectDeclarationNumber {
  public function CorrectDeclarationNumber() {
  }
  public static function make():void {
    var x:int = 7.5;
    var y:Number = 7.5;
    var z:int = 7;
    var n:int = getNumber();
  }

  private static function getNumber():Number {
    return 42;
  }
}
}