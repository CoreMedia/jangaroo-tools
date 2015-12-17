package package1 {
public class TestIdeWithReservedName {
  public function parameterWithReservedName(char:String):String {
    char = char + "!";
    return char;
  }

  public function localVarWithReservedName():String {
    var char:String = "x";
    char = char.substr(0,0) + "u";
    return char;
  }
}
}