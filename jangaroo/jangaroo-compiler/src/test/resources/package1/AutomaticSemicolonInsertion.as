package package1 {

public class AutomaticSemicolonInsertion {

  public function AutomaticSemicolonInsertion() {
    var a:String = "A";
    var b:String = a
    // auto semicolon insertion works:
    while(false);
    var c:String = b
    // auto semicolon insertion does not work:
    var d:String;

    var e:String = b
    // auto semicolon insertion does not work:
    function foo() {}
  }
}
}
