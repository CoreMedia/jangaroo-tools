

class AutomaticSemicolonInsertion {

  constructor() {
    var a = "A";
    var b = a;
    // auto semicolon insertion works:
    while(false);
    var c = b;
    // auto semicolon insertion does not work:
    var d:string;

    var e = b;
    // auto semicolon insertion does not work:
    function foo() {}
  }
}
export default AutomaticSemicolonInsertion;
