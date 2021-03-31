/*package package1 {*/
Ext.define("package1.ExtendError", function(ExtendError) {/*public class ExtendError extends Error {

  public*/ function ExtendError$(message/*: String*/) {
    this.super$Keuh(message);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.Error",
      constructor: ExtendError$,
      super$Keuh: function() {
        AS3.Error.prototype.constructor.apply(this, arguments);
      },
      requires: ["AS3.Error"]
    };
});
