package assignment{
public class CorrectDeclarationString {

  [ArrayElementType("String")]
  private var anArray:Array = ["juhu"];

  public function CorrectDeclarationString() {
  }
  public function make():void {
    var x:String;
    var x:String = "juhu";
    var x:String = new String();
    var x:String = getString();
    // TODO access of array without type?
    var x:String = anArray[0];
    // vector with type
  }

  private static function getString():String {
    return "juhu";
  }
}
}