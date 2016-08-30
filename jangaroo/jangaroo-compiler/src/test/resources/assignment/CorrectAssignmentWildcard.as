package assignment{
public class CorrectAssignmentWildcard {
  public function CorrectAssignmentWildcard() {
  }
  public static function make():void {
    var x:* = getSomething();
  }

  public static function getSomething():String {
    return "something";
  }
}
}