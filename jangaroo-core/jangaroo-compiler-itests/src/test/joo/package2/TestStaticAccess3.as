package package2 {

public class TestStaticAccess3 {

  public static function testInitOtherClass():String {
    var otherClass:Class = TestStaticAccess;
    return otherClass['s1'];
  }
}

}