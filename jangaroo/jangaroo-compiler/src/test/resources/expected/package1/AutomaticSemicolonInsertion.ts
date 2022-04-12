

class AutomaticSemicolonInsertion {

  constructor() {
    var a = "A";
    var b = a;
    // auto semicolon insertion works:
    while(false);
    var c = b;
    // auto semicolon insertion does not work:
    var d:string;
  }
}
export default AutomaticSemicolonInsertion;
