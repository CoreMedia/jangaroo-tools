// built-in as well as Ext Error constructor called as function unfortunately always create a new Error object,
// so we have to create a class that uses the original Error prototype, but also works as an Ext class:
Ext.define("AS3.Error", {
  factory: function () {
    var AS3Error = function (message/*String*/, id/*:int*/) {
      this.message = message || "";
      this.id = id || 0;
    };
    AS3Error.$isClass = true;
    AS3Error.$className = "AS3.Error";
    AS3Error.__isInstance__ = function (object) {
      // use built-in Error class for instanceof check:
      return object instanceof Error;
    };
    AS3Error.superclass = Error.prototype;
    AS3Error.prototype = Object.create(Error.prototype);
    AS3Error.prototype.self = AS3Error;
    AS3Error.prototype.constructor = AS3Error;
    AS3Error.prototype.callParent = Ext.Base.prototype.callParent;
    return AS3Error;
  }
});
