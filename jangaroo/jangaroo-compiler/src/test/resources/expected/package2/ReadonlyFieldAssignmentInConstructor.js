/*package package2 {*/

Ext.define("package2.ReadonlyFieldAssignmentInConstructor", function(ReadonlyFieldAssignmentInConstructor) {/*public class ReadonlyFieldAssignmentInConstructor extends Error {

  public*/ function ReadonlyFieldAssignmentInConstructor$(json/*:Object*/) {
    this.super$mUpo(json.message);
    this['method'] = json.method;
    this['errorCode'] = json.errorCode;
    this['errorName'] = json.errorName;
    this['status'] = json.status;
  }/*

  /**
   * The HTTP method of the request that raised this remote error.
   * /
  public native function get method():String;

  /**
   * The HTTP status code of the response that raised this remote error.
   * Defaults to 400.
   * /
  public native function get status():uint;

  /**
   * The name of the RemoteError.
   * /
  public native function get errorName():String;

  /**
   * The error code of the RemoteError.
   * /
  public native function get errorCode():String;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.Error",
      constructor: ReadonlyFieldAssignmentInConstructor$,
      super$mUpo: function() {
        AS3.Error.prototype.constructor.apply(this, arguments);
      },
      requires: ["AS3.Error"]
    };
});
