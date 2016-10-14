package assignment{
public class CorrectAssignmentRectangle {

  private var anArray:Array = [new Square()];

  [ArrayElementType("assignment.Square")]
  private var anArrayOfSquares:Array = [new Square()];

  [ArrayElementType("assignment.Rectangle")]
  private var anArrayOfRectangles:Array = [new Rectangle()];

  public function CorrectAssignmentRectangle() {
  }
  public function make():void {
    var x:Rectangle = new Rectangle();
    x = new Square();
    x = getSquare();
    x = getRectangle();
    x = getSomething();
    // TODO access of array without type?
    x = anArrayOfSquares[0];
    x = anArrayOfRectangles[0];
    x = anArray[0];
    // vector with type
  }

  private static function getSquare():Square {
    return new Square();
  }

  private static function getRectangle():Rectangle {
    return new Rectangle();
  }

  private static function getSomething():* {
    return new Rectangle();
  }
}
}