package assignment{
public class CorrectAssignmentArray {

  [ArrayElementType("Array")]
  private var nestedArray:Array = [[]];

  [ArrayElementType("String")]
  private var stringArray:Array = [""];

  [ArrayElementType("assignment.Square")]
  private var squareArray:Array = [new Square()];

  [ArrayElementType("assignment.Rectangle")]
  private var rectangleArray:Array = [new Rectangle()];

  // TODO  what about vectors
  // private var aVector:Vector

  [ArrayElementType("String")]
  private var anArrayOfStrings:Array = ["1"];

  private var arrayOfAnything = [];


  private var anArray:Array = [];
  private var anArrayWithWildCard:* = [];

  public function CorrectAssignmentArray() {
  }
  public function make():void {
    var x:Array;
    x = [];
    x = getArray();
    x = getSomething();
    x = anArray;
    x = anArrayWithWildCard;
    x = nestedArray[0];

    rectangleArray = squareArray;
  }

  private static function getArray():Array {
    return [];
  }

  private static function getSomething():* {
    return [];
  }
}
}