package assignment{
public class CorrectAssignmentString {

  [ArrayElementType("String")]
  private var anArray:Array = ["juhu"];

  public function CorrectAssignmentString() {
  }
  public function make():void {
    var x:String;
    x = "juhu";
    x = new String();
    x = getString();
    // TODO access of array without type?
    x = anArray[0];
    x = new String
    // vector with type
  }

  private static function getString():String {
    return "juhu";
  }
}
}