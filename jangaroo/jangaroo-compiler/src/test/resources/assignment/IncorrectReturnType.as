package assignment{
import package1.SomeClass;
import package1.mxml.AnotherInterface;

public class IncorrectReturnType {

  [ArrayElementType("Number")]
  private var anArrayOfNumbers:Array = [1];

  public function IncorrectReturnType() {
  }

  private function thisIsAVoidFunction():void {
    return "this is not allowed!";
  }

  private function anotherVoidFunction():void {
    return anArrayOfNumbers[2];
  }

  private static function returnSomeType(a:String):AnotherInterface {
    return new SomeClass();
  }

  private static function returnString():String {
    return 1;
  }

  private static function innerFunction():void {
    function foo():Number {
      return "nope";
    }
  }
}
}