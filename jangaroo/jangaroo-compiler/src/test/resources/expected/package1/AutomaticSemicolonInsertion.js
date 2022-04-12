/*package package1 {*/

Ext.define("package1.AutomaticSemicolonInsertion", function(AutomaticSemicolonInsertion) {/*public class AutomaticSemicolonInsertion {

  public*/ function AutomaticSemicolonInsertion$() {
    var a/*:String*/ = "A";
    var b/*:String*/ = a;
    // auto semicolon insertion works:
    while(false);
    var c/*:String*/ = b;
    // auto semicolon insertion does not work:
    var d/*:String*/;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {constructor: AutomaticSemicolonInsertion$};
});
